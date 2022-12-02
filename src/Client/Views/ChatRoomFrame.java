package Client.Views;

import Client.Controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChatRoomFrame extends JFrame {

    ClientController controller;
    Container container = getContentPane();
    JTextArea messagesArea = new JTextArea("", 20, 20);
    JTextField userTextField = new JTextField();
    JButton sendButton = new JButton("Envoyer");
    public JList<String> userList = new JList<>();
    JScrollPane listScroller = new JScrollPane();
    JLabel chatLabel = new JLabel("Messages");
    JLabel usersLabel = new JLabel("Utilisateurs connecté");

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
        messagesArea.setBounds(20, 30, 415, 475);
        userTextField.setBounds(15,515,320,50);
        sendButton.setBounds(340,515,100,50);
        listScroller.setBounds(450, 30, 200, 530);
        chatLabel.setBounds(20, 15, 100, 15);
        usersLabel.setBounds(450, 15, 200, 15);
    }

    public void setStyle()
    {
        messagesArea.setEditable(false);
        listScroller.setViewportView(userList);
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
    }

    public void setActions()
    {
        sendButton.addActionListener(e -> {
            if (!userTextField.getText().equals("")) {
                controller.send(userTextField.getText());
                userTextField.setText("");
            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.send("quit");
            }
        });
    }

    public void addComponentsToContainer()
    {
        container.add(messagesArea);
        container.add(userTextField);
        container.add(sendButton);
        container.add(listScroller);
        container.add(chatLabel);
        container.add(usersLabel);
    }
}