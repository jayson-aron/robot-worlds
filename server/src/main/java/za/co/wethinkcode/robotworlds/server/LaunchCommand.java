package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

public class LaunchCommand extends Command {

    public LaunchCommand(String name, String[] arguments) {
        super("launch", name, arguments);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {

        if ((simpleServer.isActive()))
            throw new IllegalArgumentException("That's just illegal!");
        if (world.robotExists(getRobotName()))
            return robotExits();
        else if (world.capacityReached())
            return tooManyRobots();
        else if (!simpleServer.isActive())
            return launchRobot(world, simpleServer);
        else
            return robotExits();
    }

    private ResponseProtocol robotExits() {
        String result;
        Map<String, Object> data = new HashMap<>();

        result = "ERROR";
        data.put("message", "Too many of you in this world");

        return new ResponseProtocol(result, data);
    }

    private ResponseProtocol tooManyRobots() {
        String result;
        Map<String, Object> data = new HashMap<>();

        result = "ERROR";
        data.put("message", "No more space in this world");

        return new ResponseProtocol(result, data);
    }

    private ResponseProtocol launchRobot(World world, SimpleServer simpleServer) {
        String result;
        Map<String, Object> data = new HashMap<>();

        world.addRobot(getRobotName(), Integer.parseInt(getArgument()[1]),
                Integer.parseInt(getArgument()[2]), simpleServer);
        simpleServer.activate();

        result = "OK";
        data.put("position", world.getRobotArrayPosition(getRobotName()));
        data.put("visibility", world.getVisibility());
        simpleServer.outToAllClients(launchBroadcast());

        return new ResponseProtocol(result, data, world.getRobotState(getRobotName()));
    }

    private ResponseProtocol launchBroadcast() {
        String result;
        Map<String, Object> data = new HashMap<>();

        result = "OK";
        data.put("message", getRobotName().toUpperCase() + " launched into the world");

        return new ResponseProtocol(result, data);
    }
}
