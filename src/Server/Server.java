package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private final List<Connection> connections = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    private void start() throws IOException {
        serverSocket = new ServerSocket(6666);

        while (true) {
            Connection connection = new Connection(serverSocket.accept(), this);
            Observer observer = new Observer();
            initConnection(observer, connection);
        }
    }

    private void initConnection(Observer observer, Connection newConnection) {
        connections.forEach(observer::subscribe);
        connections.forEach(connection -> connection.getObserver().subscribe(newConnection));
        newConnection.setObserver(observer);
        connections.add(newConnection);
        newConnection.start();
    }

    public void remove(Connection connection) {
        connections.remove(connection);
    }

    private void stop() throws IOException {
        serverSocket.close();
    }
}
