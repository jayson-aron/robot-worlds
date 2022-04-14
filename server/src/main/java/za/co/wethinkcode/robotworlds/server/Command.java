package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.RequestProtocol;
import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

public abstract class Command {
    private String commandName;
    private String robotName;
    private String[] argument;

    public abstract ResponseProtocol execute(World world, SimpleServer simpleServer);

    public Command(String commandName, String robotName) {
        this.commandName = commandName.trim().toLowerCase();
        this.robotName = robotName.trim().toLowerCase();
    }

    public Command(String commandName, String robotName, String[] argument) {
        this.commandName = commandName.trim().toLowerCase();
        this.robotName = robotName.trim().toLowerCase();
        this.argument = argument;
    }

    public String getRobotName() {                                                                           //<2>
        return robotName;
    }

    public String[] getArgument() {
        return this.argument;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public static Command create(RequestProtocol request) {

        switch (request.getCommand()) {
            case "launch":
                return new LaunchCommand(request.getName(), request.getArguments());
            case "forward":
                return new ForwardCommand(request.getName(), request.getArguments());
            case "back":
                return new BackCommand(request.getName(), request.getArguments());
            case "turn":
                return new TurnCommand(request.getName(), request.getArguments());
            case "state":
                return new StateCommand(request.getName());
            case "mine":
                return new MineCommand(request.getName());
            case "repair":
                return new RepairCommand(request.getName());
            case "fire":
                return new FireCommand(request.getName());
            case "look":
                return new LookCommand(request.getName());
            case "reload":
                return new ReloadCommand(request.getName());
            case "exit":
                return new ExitCommand(request.getName());
            case "quit":
                return new QuitCommand(request.getName());
            default:
                throw new IllegalArgumentException("Unsupported command!");
        }
    }
}
