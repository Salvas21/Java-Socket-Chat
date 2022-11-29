package Server;

import java.util.ArrayList;
import java.util.List;

public class Observer {
    private final List<Connection> subscribers = new ArrayList<>();

    public void notify(String sender, String content) {
        subscribers.forEach(subscriber -> subscriber.update(sender, content));
    }

    public void notifySpecificClient(String sender, String content, List<String> name) {
        for (Connection connection : subscribers) {
            if (name.contains(connection.getUsername())) {
                connection.update(sender, content);
            }
        }
    }

    public void notifyUserList() {
        subscribers.forEach(subscriber -> subscriber.sendListClient());
    }

    public void subscribe(Connection connection) {
        subscribers.add(connection);
    }

    public void unsubscribe(String username) {
        // get all connections this user is subscribed to
        for (Connection connection : subscribers) {
            // get the observer of a connection (remote client)
            Observer observer = connection.getObserver();
            Connection toDelete = null;
            // get all the connection of this observer (all connection remote client subscribed to)
            for (Connection subscriber : observer.subscribers) {
                // find ourselves
                if (subscriber.getUsername().equals(username)) {
                    toDelete = subscriber;
                }
            }
            if (toDelete != null) {
                observer.subscribers.remove(toDelete);
            }
        }
    }

    public ArrayList<String> getUsernames() {
        var usernames = new ArrayList<String>();

        subscribers.forEach(subscriber -> usernames.add(subscriber.getUsername()));
        return usernames;
    }
}
