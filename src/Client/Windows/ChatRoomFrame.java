package Client.Windows;

import Client.ClientController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChatRoomFrame extends JFrame {

    ClientController controller;
    Container container = getContentPane();
    JTextArea messagesArea = new JTextArea("", 20, 20);
    JTextField userTextField = new JTextField();
    JButton sendButton = new JButton("Envoyer");

    BlockingQueue<String> messages;



    public ChatRoomFrame(ClientController controller, BlockingQueue<String> messages)
    {
        this.controller = controller;
        this.messages = messages;
        new Thread(this::updateMessage).start();
        setLayoutManager();
        setLocationAndSize();
        setStyle();
        setActions();
        addComponentsToContainer();

    }

    private void updateMessage() {
        while (true) {
            try {
                String msg = messages.poll(100, TimeUnit.MILLISECONDS);
//                System.out.println(msg);
                if (msg != null) {
                    messagesArea.append(msg + "\n");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setLayoutManager()
    {
        container.setLayout(null);
    }
    public void setLocationAndSize()
    {
        messagesArea.setBounds(20, 15, 415, 490);
        messagesArea.setEditable(false);
        userTextField.setBounds(15,515,320,50);
        sendButton.setBounds(340,515,100,50);
    }

    public void setStyle()
    {

    }

    public void setActions()
    {
        sendButton.addActionListener(e -> {
            controller.send(userTextField.getText());
            userTextField.setText("");
        });
    }

    public void addComponentsToContainer()
    {
        container.add(messagesArea);
        container.add(userTextField);
        container.add(sendButton);
    }
}