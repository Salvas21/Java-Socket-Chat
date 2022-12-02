package Client.Model;

import Client.Controller.ClientController;
import Common.ClientCommand;
import Common.ClientPacket;
import Common.ServerCommand;
import Common.ServerPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Client {
    private final String serverIP = "127.0.0.1";
    private final int serverPort = 6666;
    private final ClientController controller;
    private final String name;
    private Socket clientSocket;
    private ArrayList<String> users;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean communicationValid = false;

    public Client(String name, ClientController controller) {
        this.name = name;
        this.controller = controller;
        startConnection(serverIP, serverPort);
        if (!initCommunication(name)) {
            stopConnection();
        }
    }

    public void read(BlockingQueue<String> messages) {
        while (isCommunicationValid()) {
            try {
                ServerPacket serverPacket = (ServerPacket) in.readObject();
                switch (serverPacket.getServerCommand()) {
                    case MESSAGE -> showMessage(messages, serverPacket);
                    case LIST_CLIENTS -> updateClientsList(serverPacket);
                    case ERROR -> System.out.println("error");
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(String content) {
        try {
            handleWriteContent(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopConnection() {
        try {
            communicationValid = false;
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCommunicationValid() {
        return communicationValid;
    }

    private void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean initCommunication(String name) {
        try {
            out.writeObject(new ClientPacket(name, ClientCommand.INIT, ""));
            ServerPacket response = (ServerPacket) in.readObject();
            if (!(response.getServerCommand() == ServerCommand.ERROR)) {
                users = new ArrayList<>(Arrays.asList(response.getContent().split(", ")));
                controller.updateUserList(users);
                communicationValid = true;
                return true;
            }
            return false;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void showMessage(BlockingQueue<String> messages, ServerPacket serverPacket) {
        messages.add(serverPacket.getName() + " : " + serverPacket.getContent());
    }

    private void updateClientsList(ServerPacket serverPacket) {
        users = new ArrayList<>(Arrays.asList(serverPacket.getContent().split(", ")));
        controller.updateUserList(users);
    }

    private void handleWriteContent(String content) throws IOException {
        if (isToSomeClients(content)) {
            sendToSomeClients(content);
        } else if (isListClients(content)) {
            askClientsList();
        } else if (isQuit(content)) {
            quit();
        } else {
            sendToAllClients(content);
        }
    }

    private boolean isToSomeClients(String content) {
        return content.startsWith("@");
    }

    private boolean isListClients(String content) {
        return content.equalsIgnoreCase("list");
    }

    private boolean isQuit(String content) {
        return content.equalsIgnoreCase("quit");
    }

    private void sendToSomeClients(String content) throws IOException {
        List<String> users = new ArrayList<>();
        getUsers(content, users);
        out.writeObject(new ClientPacket(name, ClientCommand.TO_CLIENTS, content, users));
    }

    private void sendToAllClients(String content) throws IOException {
        out.writeObject(new ClientPacket(name, ClientCommand.ALL_CLIENTS, content));
    }

    private void askClientsList() throws IOException {
        out.writeObject(new ClientPacket(name, ClientCommand.LIST_CLIENTS, ""));
    }

    private void quit() throws IOException {
        out.writeObject(new ClientPacket(name, ClientCommand.QUIT, ""));
        stopConnection();
    }

    private List<String> getUsers(String content, List<String> users) {
        int atIndex = content.indexOf("@");
        int spaceIndex = content.indexOf(" ");
        if (spaceIndex <= atIndex) {
            return new ArrayList<>();
        }
        String user = content.substring(atIndex + 1, spaceIndex);
        users.add(user);
        return getUsers(content.substring(spaceIndex + 1), users);
    }

}
