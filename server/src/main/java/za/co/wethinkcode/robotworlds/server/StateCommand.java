package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

public class StateCommand extends Command {

    public StateCommand(String name) {
        super("state", name);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {

        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("Server is not yet activated!");
        else
            return new ResponseProtocol(world.getRobotState(getRobotName()));
    }
}
