package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

public class ReloadCommand extends Command {

    public ReloadCommand(String name) {
        super("reload", name);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {
        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("That's just illegal!");
        else
            return doReload(world, simpleServer);
    }

    private ResponseProtocol doReload(World world, SimpleServer simpleServer) {
        Map<String, Object> state;
        Map<String, Object> data = new HashMap<>();

        world.reloadRobotWeapons(getRobotName());

        try {
            simpleServer.delayClient(world.getWeaponsReloadTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        data.put("message", "Done");
        state = world.getRobotState(getRobotName());
        return new ResponseProtocol("OK", data, state);
    }
}
