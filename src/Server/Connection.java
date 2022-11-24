package Server;

import java.net.*;
import java.io.*;

public class Connection extends Thread{
    private Observer observer;
    private final PrintWriter out;
    private final BufferedReader in;

    public Connection(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        new Thread(this::write).start();
    }

    private void write() {
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                } else if ("init".equals(inputLine.substring(0, 4))) {
                    out.println("bienvenue " + inputLine.substring(5));
                } else {
                    observer.notify(inputLine);
                    out.println(inputLine);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String content) {
        out.println(content);
    }

    public Observer getObserver() {
        return this.observer;
    }
    public void setObserver(Observer observer) {
        this.observer = observer;
    }
}