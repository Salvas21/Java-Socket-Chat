package Common;

public class ServerPacket extends Packet {
    private final String name;

    public ServerPacket(String name, String content) {
        this.name = name;
        super.setContent(content);
    }

    public String getName() {
        return name;
    }
}
