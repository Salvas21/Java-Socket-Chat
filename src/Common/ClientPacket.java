package Common;


import java.util.List;

public class ClientPacket extends Packet {
    private String name;
    private String ip;
    private ClientCommand clientCommand;
    private List<String> users;

    public ClientPacket(String name, String ip, ClientCommand clientCommand, String content) {
        this.name = name;
        this.ip = ip;
        this.clientCommand = clientCommand;
        super.setContent(content);
    }
    public ClientPacket(String name, String ip, ClientCommand clientCommand, String content, List<String> users) {
        this.name = name;
        this.ip = ip;
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
