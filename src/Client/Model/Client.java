package Client.Model;

import Common.ClientPacket;
import Common.ClientCommand;
import Common.ServerPacket;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Client {
    private Socket clientSocket;
    private final String name;
    private ArrayList<String> users;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String name) {
        this.name = name;
        startConnection("127.0.0.1", 6666);
        if(!initCommunication(name)) {
            stopConnection();
        }
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean initCommunication(String name) {
        ServerPacket response;
        try {
            out.writeObject(new ClientPacket(name, "127.0.0.1", ClientCommand.INIT, ""));
            // TODO : ajouter un code erreur dans le server packet ?
            // est ce que le server envoit une string "error" ou pas du tout
            if(!(response = (ServerPacket) in.readObject()).getContent().equalsIgnoreCase("error")) {
                users = new ArrayList<>(Arrays.asList(response.getContent().split(",")));
                return true;
            }
            return false;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void read(BlockingQueue<String> messages) {
        while(true) {

            try {
                ServerPacket serverPacket = (ServerPacket) in.readObject();
                switch (serverPacket.getServerCommand()) {
                    case MESSAGE -> messages.add(serverPacket.getName() + " : " + serverPacket.getContent());
                    case LIST_CLIENTS -> users = new ArrayList<>(Arrays.asList(serverPacket.getContent().split(",")));
                    case ERROR -> System.out.println("error"); // TODO : implement error check
                }
                // TODO : receives correct list, just need to update it in UI
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(String content) {
        try {
            if (content.equalsIgnoreCase("list")) {
                out.writeObject(new ClientPacket(name, "127.0.0.1", ClientCommand.LIST_CLIENTS, ""));
            } else {
                out.writeObject(new ClientPacket(name, "127.0.0.1", ClientCommand.ALL_CLIENTS, content));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getUsers() {
        return users;
    }
}
