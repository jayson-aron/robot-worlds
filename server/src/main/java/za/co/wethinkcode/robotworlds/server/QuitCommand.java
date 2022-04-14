package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

public class QuitCommand extends Command {

    public QuitCommand(String robotName) {
        super("quit", robotName);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state;

        if (!simpleServer.isActive())
            throw new IllegalArgumentException("Server not active!");

        data.put("message", "You have quit the game.");
        world.getRobot(getRobotName()).setStatus(OperationalMode.DEAD);
        simpleServer.outToAllClients(exitBroadcast());
        state = world.getRobotState(getRobotName());
        world.removeRobot(getRobotName());
        simpleServer.deActivate();
        return new ResponseProtocol("", data, state);
    }

    private ResponseProtocol exitBroadcast() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", getRobotName().toUpperCase() + " exited the game.");

        return new ResponseProtocol("OK", data);
    }
}
