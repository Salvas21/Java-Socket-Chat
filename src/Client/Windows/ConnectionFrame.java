package Client.Windows;

import Client.ClientController;

import javax.swing.*;
import java.awt.*;

public class ConnectionFrame extends JFrame {
    ClientController controller;
    Container container = getContentPane();
    JLabel userLabel = new JLabel("Entrer votre nom");
    JTextField userTextField = new JTextField();
    JButton loginButton = new JButton("Connexion");


    public ConnectionFrame(ClientController controller)
    {
        this.controller = controller;
        setLayoutManager();
        setLocationAndSize();
        setStyle();
        setActions();
        addComponentsToContainer();

    }
    public void setLayoutManager()
    {
        container.setLayout(null);
    }
    public void setLocationAndSize()
    {
        userLabel.setBounds(200,130,300,40);
        userTextField.setBounds(150,185,300,30);
        loginButton.setBounds(350,225,100,30);
    }

    public void setStyle()
    {
        this.setTitle("Login Form");
        this.setVisible(true);
        this.setBounds(10,10,600,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        userLabel.setFont(new Font("Calibri", Font.BOLD, 20));
    }

    public void setActions()
    {
        loginButton.addActionListener(e -> controller.startChatRoom(userTextField.getText()));
    }
    public void addComponentsToContainer()
    {
        container.add(userLabel);
        container.add(userTextField);
        container.add(loginButton);
    }
}