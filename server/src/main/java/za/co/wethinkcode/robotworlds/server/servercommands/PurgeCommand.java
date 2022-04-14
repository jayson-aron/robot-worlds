package za.co.wethinkcode.robotworlds.server.servercommands;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;
import za.co.wethinkcode.robotworlds.server.OperationalMode;
import za.co.wethinkcode.robotworlds.server.SimpleServer;
import za.co.wethinkcode.robotworlds.server.World;

import java.util.HashMap;
import java.util.Map;

public class PurgeCommand extends ServerCommands {

    public PurgeCommand(String robotName) {
        super("purge", robotName);
    }

    @Override
    public boolean execute(World world) {
        if (getArgument().isBlank())
            System.out.println("Invalid argument");
        else if (!world.robotExists(getArgument()))
            System.out.println("Robot is not found in the world!");
        else{
            world.getRobot(getArgument()).setStatus(OperationalMode.DEAD);
            SimpleServer simpleServer = world.getRobotServer(getArgument());
            simpleServer.deActivate();
            simpleServer.outToAllClients(robotPurgedBroadcast());
            simpleServer.outToOneClient(world.getRobotServer(getArgument()), robotPurgedAlert(world));
            world.removeRobot(getArgument());
            System.out.println(getArgument().toUpperCase() + " killed and removed from the world");
        }
        return true;
    }

    private ResponseProtocol robotPurgedBroadcast() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", getArgument().toUpperCase() + " purged from the world!");

        return new ResponseProtocol("OK", data);
    }

    private ResponseProtocol robotPurgedAlert(World world) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state;

        data.put("message", getArgument().toUpperCase() + " purged from the world!");
        state = world.getRobotState(getArgument());

        return new ResponseProtocol("OK", data, state);
    }
}
