package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener {
    private ServerSocket serverSocket;
    private List<Server> servers = new ArrayList<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            Observer observer = new Observer();
            Server server = new Server(serverSocket.accept());


            servers.forEach(observer::subscribe);

            servers.forEach(server1 -> server1.getObserver().subscribe(server));








            server.setObserver(observer);
            servers.add(server);
            server.start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        ServerListener server = new ServerListener();
        server.start(6666);
    }
}
