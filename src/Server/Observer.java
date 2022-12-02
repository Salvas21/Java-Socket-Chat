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

    /**
     * unsubscribes the username from all other connections
     */
    public void unsubscribe(String username) {
        for (Connection connection : subscribers) {
            removeUsernameFromOther(connection.getObserver(), username);
        }
    }

    public ArrayList<String> getUsernames() {
        var usernames = new ArrayList<String>();
        subscribers.forEach(subscriber -> usernames.add(subscriber.getUsername()));
        return usernames;
    }

    private void removeUsernameFromOther(Observer observer, String username) {
        Connection toDelete = findUsernameConnection(observer.subscribers, username);
        if (toDelete != null) observer.subscribers.remove(toDelete);
    }

    private Connection findUsernameConnection(List<Connection> subscribers, String username) {
        for (Connection subscriber : subscribers) {
            if (subscriber.getUsername().equals(username)) return subscriber;
        }
        return null;
    }
}
