package Client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    private static void read() throws IOException {
        while(true) {
            System.out.println(in.readLine());
        }
    }

    private static void write(Client client) throws IOException {
        Scanner in = new Scanner(System.in);
        while(true) {
            String s = in.nextLine();
            out.println(s);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startConnection("127.0.0.1", 6666);

//        System.out.println("Enter your name: ");
//        String name = in.nextLine();
//        String initResponse = client.sendMessage("init "+ name);
//        if(!initResponse.equals("error")) {
//            System.out.println(initResponse);
        new Thread(() -> {
            try {
                read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                write(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //}

    }
}
