package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final List<Connection> connections = new ArrayList<>();
    private ServerSocket serverSocket;

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
        subscribeConnectionsToNewConnection(observer);
        subscribeConnectionToAllConnections(newConnection);

        newConnection.setObserver(observer);
        connections.add(newConnection);

        newConnection.start();
    }

    /**
     * abonne toutes les connexions existantes à notre nouvelle connexion (notre observer)
     * permet donc d'envoyer les messages aux autres connexions
     * @param observer
     */
    private void subscribeConnectionsToNewConnection(Observer observer) {
        connections.forEach(observer::subscribe);
    }

    /**
     * abonne la nouvelle connexion à toutes les connexions existantes
     * permet donc de recevoir les messages des autres connexions
     * @param newConnection
     */
    private void subscribeConnectionToAllConnections(Connection newConnection) {
        connections.forEach(connection -> connection.getObserver().subscribe(newConnection));
    }

    /**
     * supprimer une connexion dans le cas d'une fermeture de connexion
     * @param connection
     */
    public void remove(Connection connection) {
        connections.remove(connection);
    }

    private void stop() throws IOException {
        serverSocket.close();
    }
}
