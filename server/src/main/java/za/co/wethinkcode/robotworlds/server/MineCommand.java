package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

public class MineCommand extends Command {

    public MineCommand(String name) {
        super("mine", name);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {
        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("Server is not yet activated!");
        return setMine(world, simpleServer);
    }

    private ResponseProtocol setMine(World world, SimpleServer simpleServer) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state;

        if (!world.robotCanPlaceMines(getRobotName()))
            return robotNotAllowedToPlaceMines();

        int robotShieldStrength = world.getRobotShieldStrength(getRobotName());
        world.setMine(getRobotName());

        try {
            simpleServer.delayClient(world.getMineSetTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        world.setRobotShieldStrength(getRobotName(), robotShieldStrength);

        data.put("message", "Done");
        state = world.getRobotState(getRobotName());
        return new ResponseProtocol("OK", data, state);
    }

    private ResponseProtocol robotNotAllowedToPlaceMines() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", "Not allowed to place mines");

        return new ResponseProtocol("ERROR", data);
    }
}
