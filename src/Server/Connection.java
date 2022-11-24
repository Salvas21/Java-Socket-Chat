package Server;

import java.net.*;
import java.io.*;

public class Connection extends Thread{
    private Observer observer;
    private final PrintWriter out;
    private final BufferedReader in;
    private String username = "";

    public Connection(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        new Thread(this::handle).start();
    }

    private void handle() {
        waitForConnection();
        handleChat();
    }

    private void waitForConnection() {
        String inputLine;
        boolean accepted = false;
        try {
            while (!accepted) {
                inputLine = in.readLine();
                if (inputLine != null && inputLine.length() > 4 && inputLine.substring(0, 4).equalsIgnoreCase("init")) {
                    username = inputLine.substring(5);
                    if (!username.equals("") && isAvailable()) {
                        accepted = true;
                        out.println("bienvenue " + username);
                    } else {
                        out.println("username: " + username + " invalide");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleChat() {
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                } else {
                    observer.notify(inputLine);
                    out.println(inputLine);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAvailable() {
        return !observer.getUsernames().contains(username);
    }

    public String getUsername() {
        return username;
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