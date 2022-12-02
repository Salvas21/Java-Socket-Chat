package Common;


import java.util.List;

public class ClientPacket extends Packet {
    private final String name;
    private final ClientCommand clientCommand;
    private List<String> users;

    public ClientPacket(String name, ClientCommand clientCommand, String content) {
        this.name = name;
        this.clientCommand = clientCommand;
        super.setContent(content);
    }

    public ClientPacket(String name, ClientCommand clientCommand, String content, List<String> users) {
        this.name = name;
        this.clientCommand = clientCommand;
        this.users = users;
        super.setContent(content);
    }

    public List<String> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

    public ClientCommand getCommand() {
        return clientCommand;
    }

}
