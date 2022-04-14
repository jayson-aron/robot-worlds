package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

public class ExitCommand extends Command {

    public ExitCommand(String robotName) {
        super("exit", robotName);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {
        Map<String, Object> data = new HashMap<>();

        if (!simpleServer.isActive()) {
            simpleServer.closeQuietly();
            return new ResponseProtocol("", data);
        }

        simpleServer.outToAllClients(exitBroadcast());
        world.removeRobot(getRobotName());
        simpleServer.closeQuietly();
        return new ResponseProtocol("", data);
    }

    private ResponseProtocol exitBroadcast() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", getRobotName().toUpperCase() + " exited the game.");

        return new ResponseProtocol("OK", data);
    }
}
