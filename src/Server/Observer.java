package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Observer {
    private List<Server> subsribers = new ArrayList<>();

    public void notify(String content) {
        subsribers.forEach(server -> {
            try {
                server.update(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void subscribe(Server server) {
        subsribers.add(server);
    }
}
