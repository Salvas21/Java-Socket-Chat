package Server;

import java.util.ArrayList;
import java.util.List;

public class Observer {

    /**
     * Cette classe représente le système de communication entre les différentes connection d'un serveur.
     * Elle permet d'avoir une liste d'abonnés à qui l'on peut envoyer du contenu, dans notre contexte, des paquets.
     * Le principe de ce fonctionnement est décrit par le patron de conception Observateur : https://refactoring.guru/design-patterns/observer
     *
     * Dans ce cas-ci, une connection contient un objet Observer, qui lui contient la liste de toutes les autres connexions du Server,
     * ce qui permet de pouvoir envoyer les données des paquets entre les différents threads des connexions.
     */

    private final List<Connection> subscribers = new ArrayList<>();

    /**
     * envoie un message à tous les abonnés
     * @param sender
     * @param content
     */
    public void notify(String sender, String content) {
        subscribers.forEach(subscriber -> subscriber.update(sender, content));
    }

    /**
     * envoie un message à un client spécifique selon son nom dans les clients abonnés à notre observer
     * @param sender
     * @param content
     * @param name
     */
    public void notifySpecificClient(String sender, String content, List<String> name) {
        for (Connection connection : subscribers) {
            if (name.contains(connection.getUsername())) {
                connection.update(sender, content);
            }
        }
    }

    /**
     * envoie la liste des clients à tous les abonnés
     */
    public void notifyUserList() {
        subscribers.forEach(subscriber -> subscriber.sendListClient());
    }

    /**
     * abonne une nouvelle connection à notre observer
     * @param connection
     */
    public void subscribe(Connection connection) {
        subscribers.add(connection);
    }

    /**
     * désabonne l'abonné de toutes les connexions selon son nom d'utilisateur
     * (dans le cas qu'un client ferme la connexion, le supprime de toutes les connections existantes)
     */
    public void unsubscribe(String username) {
        for (Connection connection : subscribers) {
            removeUsernameFromOther(connection.getObserver(), username);
        }
    }

    /**
     * retourne la liste de tout les noms d'utilisateurs des abonnés
     * @return
     */
    public ArrayList<String> getUsernames() {
        var usernames = new ArrayList<String>();
        subscribers.forEach(subscriber -> usernames.add(subscriber.getUsername()));
        return usernames;
    }

    /**
     * supprime la connexion de la liste d'un abonné
     * @param observer
     * @param username
     */
    private void removeUsernameFromOther(Observer observer, String username) {
        Connection toDelete = findUsernameConnection(observer.subscribers, username);
        if (toDelete != null) observer.subscribers.remove(toDelete);
    }

    /**
     * retourne la connection associé au nom d'utilisateur à supprimer
     * @param subscribers
     * @param username
     * @return
     */
    private Connection findUsernameConnection(List<Connection> subscribers, String username) {
        for (Connection subscriber : subscribers) {
            if (subscriber.getUsername().equals(username)) return subscriber;
        }
        return null;
    }
}
