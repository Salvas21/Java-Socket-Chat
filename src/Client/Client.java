package Client;

import Client.Windows.ChatRoomFrame;
import Client.Windows.ConnectionFrame;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Client {
    private ConnectionFrame connection;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String name) {
        startConnection("127.0.0.1", 6666);
        if(!initCommunication(name)) {
            stopConnection();
        }
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean initCommunication(String name) {
        out.println("init "+ name);
        String response;
        try {
            if(!(response = in.readLine()).equalsIgnoreCase("error")) {
                System.out.println(response);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read(BlockingQueue<String> messages) {
        while(true) {
            try {
                messages.add(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(String content) {
        out.println(content);
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
