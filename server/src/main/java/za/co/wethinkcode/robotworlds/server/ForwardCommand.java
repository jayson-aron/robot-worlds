package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForwardCommand extends Command {

    public ForwardCommand(String name, String[] arguments) {
        super("forward", name, arguments);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {

        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("That's just illegal!");
        if (!(isValidArgument()))
            return invalidArgument();
        else
            return doForward(world, simpleServer);
    }

    private boolean isValidArgument() {
        Pattern arg1 = Pattern.compile("\\d{1,3}");

        Matcher matcher = arg1.matcher(getArgument()[0]);

        return matcher.matches();
    }

    private ResponseProtocol invalidArgument() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", "Could not parse arguments");

        return new ResponseProtocol("ERROR", data);
    }

    private ResponseProtocol doForward(World world, SimpleServer simpleServer) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state;

        switch (world.updateRobotPosition(getRobotName(), Integer.parseInt(getArgument()[0]))) {
            case SUCCESS:
                data.put("message", "Done");
                break;
            case OBSTRUCTED:
                data.put("message", "Obstructed");
                break;
            case FELL:
                data.put("message", "Fell");
                simpleServer.outToAllClients(deathByPitBroadcast());
                simpleServer.deActivate();
                break;
            case MINE:
                data.put("message", "Mine");
                simpleServer.outToAllClients(deathByMineBroadcast());
                simpleServer.deActivate();
            case MINEBUTALIVE:
                data.put("message", "Mine");
                break;
            default:
                throw new IllegalArgumentException("WTF!");
        }
        state = world.getRobotState(getRobotName());

        if (world.getRobot(getRobotName()).getStatus().equals(OperationalMode.DEAD))
            world.removeRobot(getRobotName());

        return new ResponseProtocol("OK", data, state);
    }

    private ResponseProtocol deathByPitBroadcast() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", getRobotName().toUpperCase() + " fell into a pit.");

        return new ResponseProtocol("OK", data);
    }

    private ResponseProtocol deathByMineBroadcast() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", getRobotName().toUpperCase() + " stepped on a mine and died.");

        return new ResponseProtocol("OK", data);
    }
}
