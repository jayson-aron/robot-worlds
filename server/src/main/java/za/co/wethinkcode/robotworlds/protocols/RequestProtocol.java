package za.co.wethinkcode.robotworlds.protocols;

public class RequestProtocol {
    private String name;
    private String command;
    private String[] arguments;

    public RequestProtocol(String name, String command, String[] arguments) {
        this.name = name;
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return this.command;
    }

    public String getName() {
        return this.name;
    }

    public String[] getArguments() {
        return this.arguments;
    }
}
