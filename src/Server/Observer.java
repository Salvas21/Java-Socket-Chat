package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Observer {
    private final List<Connection> subscribers = new ArrayList<>();

    public void notify(String content) {
        subscribers.forEach(subscriber -> subscriber.update(content));
    }

    public void subscribe(Connection connection) {
        subscribers.add(connection);
    }
}
