package Client.Controller;

import Client.Model.Client;
import Client.Views.ChatRoomFrame;
import Client.Views.ConnectionFrame;

import javax.swing.*;
import java.util.ArrayList;
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
        connectionFrame.setBounds(10, 10, 600, 400);
        connectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectionFrame.setResizable(false);
        connectionFrame.setLocationRelativeTo(null);
    }

    public void startChatRoom(String name) {
        // TODO : voir si la connexion est bonne avant de continuer
        chatRoomFrame = new ChatRoomFrame(this, messages);
        chatRoomFrame.setTitle("Chat room " + name);
        chatRoomFrame.setVisible(true);
        chatRoomFrame.setBounds(10, 10, 670, 600);
        chatRoomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatRoomFrame.setResizable(false);
        chatRoomFrame.setLocationRelativeTo(null);
        connectionFrame.setVisible(false);

        client = new Client(name, this);
        if (client.isCommunicationValid()) new Thread(this::read).start();
    }

    public void updateUserList(ArrayList<String> users) {
        chatRoomFrame.userList.setListData(users.toArray(new String[0]));
    }

    public void read() {
        if (client.isCommunicationValid()) client.read(messages);
    }

    public void send(String content) {
        if (client.isCommunicationValid()) client.write(content);
    }
}
