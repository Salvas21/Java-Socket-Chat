package Client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 6666);
        if(client.initCommunication()) {
            new Thread(client::read).start();
            new Thread(client::write).start();
        } else {
            System.out.println("Error while connecting.");
            client.stopConnection();
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

    private boolean initCommunication() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
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

    private void read() {
        while(true) {
            try {
                System.out.println(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void write() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            out.println(scanner.nextLine());
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
