package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

public class RepairCommand extends Command {

    public RepairCommand(String name) {
        super("repair", name);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {
        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("That's just illegal!");
        else
            return doRepair(world, simpleServer);
    }

    private ResponseProtocol doRepair(World world, SimpleServer simpleServer) {
        Map<String, Object> state;
        Map<String, Object> data = new HashMap<>();

        world.repairRobotShield(getRobotName());

        try {
            simpleServer.delayClient(world.getShieldRepairTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        data.put("message", "Done");
        state = world.getRobotState(getRobotName());
        return new ResponseProtocol("OK", data, state);
    }
}
