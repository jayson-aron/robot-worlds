package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LookCommand extends Command {

    public LookCommand(String name) {
        super("look", name);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {

        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("Server is not yet activated!");
        else
            return doLook(world, simpleServer);
    }

    private ResponseProtocol doLook(World world, SimpleServer simpleServer) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state;

        ArrayList<Map<String, Object>> objects = world.lookAroundTheWorld(getRobotName());
        data.put("objects", objects);

        state = world.getRobotState(getRobotName());
        return new ResponseProtocol("OK", data, state);
    }
}
