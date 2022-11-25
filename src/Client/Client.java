package Client;

import Common.ClientPacket;
import Common.Command;
import Common.ServerPacket;

import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;

public class Client {
    private Socket clientSocket;
    private final String name;
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
            out.writeObject(new ClientPacket(name, "127.0.0.1", Command.INIT, ""));
            if(!(response = (ServerPacket) in.readObject()).getContent().equalsIgnoreCase("error")) {
                System.out.println(response);
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
                messages.add(((ServerPacket)in.readObject()).getContent());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(String content) {
        try {
            out.writeObject(new ClientPacket(name, "127.0.0.1", Command.ALL_CLIENTS, content));
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
}
