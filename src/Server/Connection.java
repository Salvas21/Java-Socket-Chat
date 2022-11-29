package Server;

import Common.ClientPacket;
import Common.ClientCommand;
import Common.ServerCommand;
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
                    out.writeObject(new ServerPacket(username, ServerCommand.LIST_CLIENTS, formatUsersList()));
                } else {
                    out.writeObject(new ServerPacket(username, ServerCommand.ERROR,"username: " + username + " invalide"));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleChat() {
        ClientPacket clientPacket;
        try {
            while ((clientPacket = (ClientPacket) in.readObject()) != null) {
                if (clientPacket.getCommand() == ClientCommand.QUIT) {
                    // TODO : close connection
                    break;
                } else {
                    // TODO : regarder la commande a qui l'envoyer
                    // determiner comment l'envoyer

                    switch (clientPacket.getCommand()) {
                        case ALL_CLIENTS -> {
                            observer.notify(clientPacket.getName(), clientPacket.getContent());
                            out.writeObject(new ServerPacket(username, ServerCommand.MESSAGE, clientPacket.getContent()));
                        }
                        case LIST_CLIENTS -> {
                            out.writeObject(new ServerPacket(username, ServerCommand.LIST_CLIENTS, formatUsersList()));
                        }
                    }
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
            out.writeObject(new ServerPacket(sender, ServerCommand.MESSAGE, content));
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

    private String formatUsersList() {
        return String.join(", ", observer.getUsernames());
    }
}