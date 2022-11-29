package Common;

public class ServerPacket extends Packet {
    private final String name;
    private final ServerCommand serverCommand;

    public ServerPacket(String name, ServerCommand serverCommand, String content) {
        this.name = name;
        this.serverCommand = serverCommand;
        super.setContent(content);
    }

    public String getName() {
        return name;
    }

    public ServerCommand getServerCommand() {
        return serverCommand;
    }
}
