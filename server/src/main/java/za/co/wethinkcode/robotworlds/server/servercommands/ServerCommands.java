package za.co.wethinkcode.robotworlds.server.servercommands;

import za.co.wethinkcode.robotworlds.server.World;

public abstract class ServerCommands {
    private String commandName;
    private String argument;

    public abstract boolean execute(World world);

    public ServerCommands(String commandName) {
        this.commandName = commandName.toLowerCase();
    }

    public ServerCommands(String commandName, String argument) {
        this.commandName = commandName.toLowerCase();
        this.argument = argument.toLowerCase();
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getArgument() {
        return this.argument;
    }

    public static ServerCommands create(String commandString) {
        String[] args = commandString.trim().split(" ");
        String command = args[0];
        String robotName;
        try {
            robotName = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            robotName = "";
        }

        switch (command) {
            case "quit":
                return new QuitCommand();
            case "robots":
                return new RobotsCommand();
            case "purge":
                return new PurgeCommand(robotName);
            case "dump":
                return new DumpCommand();
            default:
                throw new IllegalArgumentException("Unsupported command!");
        }
    }
}
