package Common;


public class ClientPacket extends Packet {
    private String name;
    private String ip;
    private Command command;

    public ClientPacket(String name, String ip, Command command, String content) {
        this.name = name;
        this.ip = ip;
        this.command = command;
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

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

}
