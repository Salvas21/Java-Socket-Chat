package Server;

import Common.ClientPacket;
import Common.Command;
import Common.ServerPacket;

import java.net.*;
import java.io.*;

public class Connection extends Thread{
    private Observer observer;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private String username = "";

    public Connection(Socket socket) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        new Thread(this::handle).start();
    }

    private void handle() {
        waitForConnection();
        handleChat();
    }

    private void waitForConnection() {
        ClientPacket clientPacket;
        boolean accepted = false;
        try {
            while (!accepted) {
                clientPacket = (ClientPacket) in.readObject();
                username = clientPacket.getName();
                if (!username.equals("") && isAvailable()) {
                    accepted = true;
                    out.writeObject(new ServerPacket(username, String.join(", ", observer.getUsernames())));
                } else {
                    out.writeObject(new ServerPacket(username, "username: " + username + " invalide"));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleChat() {
        System.out.println("HandleChat");
        ClientPacket clientPacket;
        try {
            while ((clientPacket = (ClientPacket) in.readObject()) != null) {
                System.out.println("While");
                if (clientPacket.getCommand() == Command.QUIT) {
                    // TODO : close connection
                    break;
                } else {
                    System.out.println(clientPacket.getContent());
                    // TODO : regarder la commande a qui l'envoyer
                    // determiner comment l'envoyer
                    observer.notify(clientPacket.getName(), clientPacket.getContent());
                    out.writeObject(new ServerPacket(username, clientPacket.getContent()));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO : lorsqu'un client quitte présentement ça throw l'exception
            throw new RuntimeException(e);
        }
    }

    private boolean isAvailable() {
        return !observer.getUsernames().contains(username);
    }

    public String getUsername() {
        return username;
    }

    public void update(String sender, String content) {
        try {
            out.writeObject(new ServerPacket(sender, content));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Observer getObserver() {
        return this.observer;
    }
    public void setObserver(Observer observer) {
        this.observer = observer;
    }
}