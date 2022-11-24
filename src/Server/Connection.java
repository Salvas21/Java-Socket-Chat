package Server;

import java.net.*;
import java.io.*;

public class Connection extends Thread{
    private final Socket clientSocket;
    private Observer observer;
    private final PrintWriter out;
    private final BufferedReader in;

    public Connection(Socket socket) {
        try {
            this.clientSocket = socket;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        new Thread(this::read).start();
        new Thread(this::write).start();
    }

    private void write() {
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Here");
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
                if ("init".equals(inputLine.substring(0, 4))) {
                    out.println("bienvenue " + inputLine.substring(5));
                    break;
                }
                observer.notify(inputLine);
                out.println(inputLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("out");
    }

    private void read() {
        String message;
        while (true) {
//            message = messages.peek();
//            if(message != null) {
//                out.println(message);
//            }
        }
    }

    public void update(String content) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Observer getObserver() {
        return this.observer;
    }
    public void setObserver(Observer observer) {
        this.observer = observer;
    }
}