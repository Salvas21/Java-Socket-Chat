package Server;

import java.net.*;
import java.io.*;

public class Server extends Thread{
    private final Socket clientSocket;
    private Observer observer;

    public Server(Socket socket) {
        this.clientSocket = socket;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Observer getObserver() {
        return this.observer;
    }

    public void update(String content) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println(content);
    }

    private void write() throws IOException {
        System.out.println("Start writing");
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            observer.notify(inputLine);
//            if (".".equals(inputLine)) {
//                out.println("bye");
//                break;
//            }
//            if("init".equals(inputLine.substring(0, 4))) {
//                out.println("bienvenue " + inputLine.substring(5));
//                break;
//            }
            out.println(inputLine);
        }
    }

    private void read() throws IOException {
        System.out.println("Start reading");
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        String message;
        while (true) {
//            message = messages.peek();
//            if(message != null) {
//                out.println(message);
//            }
        }
    }

    public void run() {
        new Thread(() -> {
            try {
                read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                write();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}


/*

try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            String test = null;
            while ((inputLine = in.readLine()) != null || (test = messages.peek()) != null) {
                if(test != null) {
                    out.println(test);
                }

                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
                messages.add(inputLine);
                out.println(inputLine);
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


 */