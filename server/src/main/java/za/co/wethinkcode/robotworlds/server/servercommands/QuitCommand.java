package za.co.wethinkcode.robotworlds.server.servercommands;

import za.co.wethinkcode.robotworlds.server.World;

public class QuitCommand extends ServerCommands {

    public QuitCommand() {
        super("quit");
    }

    @Override
    public boolean execute(World world) {
        return false;
    }
}
