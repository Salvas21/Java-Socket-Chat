package Common;


public class ClientPacket extends Packet {
    private String name;
    private String ip;
    private ClientCommand clientCommand;

    public ClientPacket(String name, String ip, ClientCommand clientCommand, String content) {
        this.name = name;
        this.ip = ip;
        this.clientCommand = clientCommand;
        super.setContent(content);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ClientCommand getCommand() {
        return clientCommand;
    }

    public void setCommand(ClientCommand clientCommand) {
        this.clientCommand = clientCommand;
    }

}
