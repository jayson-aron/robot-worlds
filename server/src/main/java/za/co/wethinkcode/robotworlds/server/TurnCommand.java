package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TurnCommand extends Command {

    public TurnCommand(String name, String[] arguments) {
        super("turn", name, arguments);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {

        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("That's just illegal!");
        else if (!isValidArgument())
            return invalidArgument();
        else
            return turnRobot(world);
    }

    private boolean isValidArgument() {
        Pattern arg1 = Pattern.compile("left|right");

        Matcher matcher = arg1.matcher(getArgument()[0]);

        return matcher.matches();
    }

    private ResponseProtocol invalidArgument() {
        String result;
        Map<String, Object> data = new HashMap<>();

        result = "ERROR";
        data.put("message", "Could not parse arguments");

        return new ResponseProtocol(result, data);
    }

    private ResponseProtocol turnRobot(World world) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state;

        world.setRobotDirection(getRobotName(), getArgument()[0]);
        data.put("message", "Done");

        return new ResponseProtocol("OK", data, world.getRobotState(getRobotName()));
    }
}
