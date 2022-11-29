package Client.Controller;

import Client.Model.Client;
import Client.Views.ChatRoomFrame;
import Client.Views.ConnectionFrame;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientController {
    Client client;
    BlockingQueue<String> messages = new LinkedBlockingQueue<>();
    ConnectionFrame connectionFrame;
    ChatRoomFrame chatRoomFrame;
    public ClientController() {
        startLogin();
    }

    public void startLogin() {
        connectionFrame = new ConnectionFrame(this);
        connectionFrame.setTitle("Connection");
        connectionFrame.setVisible(true);
        connectionFrame.setBounds(10,10,600,400);
        connectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectionFrame.setResizable(false);
        connectionFrame.setLocationRelativeTo(null);
    }

    public void startChatRoom(String name) {
        client = new Client(name);
        // TODO : voir si la connexion est bonne avant de continuer
        // TODO : et fermer les connexion lorsqu'on quitte
        new Thread(this::read).start();

        chatRoomFrame = new ChatRoomFrame(this, messages);
        chatRoomFrame.setTitle("Chat room " + name);
        chatRoomFrame.setVisible(true);
        chatRoomFrame.setBounds(10,10,670,600);
        chatRoomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatRoomFrame.setResizable(false);
        chatRoomFrame.setLocationRelativeTo(null);
        chatRoomFrame.userList.setListData(client.getUsers().toArray(new String[0]));
        connectionFrame.setVisible(false);
    }

    public void read() {
        client.read(messages);
    }

    public void send(String content) {
        client.write(content);
    }
}