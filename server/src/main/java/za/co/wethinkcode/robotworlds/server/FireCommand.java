package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

public class FireCommand extends Command {

    public FireCommand(String name) {
        super("fire", name);
    }

    @Override
    public ResponseProtocol execute(World world, SimpleServer simpleServer) {
        if (!(simpleServer.isActive()))
            throw new IllegalArgumentException("That's just illegal!");
        else
            return doFire(world, simpleServer);
    }

    private ResponseProtocol doFire(World world, SimpleServer simpleServer) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = new HashMap<>();

        if (world.robotCanPlaceMines(getRobotName()))
            return robotHasNoGun();
        else if (world.getRobotShots(getRobotName()) <= 0)
            return outOfAmmo(world);

        if (world.shootABullet(getRobotName()).equals(UpdateResponse.MISS)) {
            data.put("message", "Miss");
        } else {
            Robot target = world.getRobot(world.getTargetRobotName(getRobotName()));
            Robot robot = world.getRobot(getRobotName());

            target.setShieldStrength(target.getShieldStrength() - 1);
            if (target.getShieldStrength() < 0)
                target.setStatus(OperationalMode.DEAD);

            int xDistance = target.getCurrentPosition().getX() - robot.getCurrentPosition().getX();
            int yDistance = target.getCurrentPosition().getY() - robot.getCurrentPosition().getY();

            int distance = (xDistance + yDistance > 0) ? (xDistance + yDistance) : -(xDistance + yDistance);

            data.put("message", "Hit");
            data.put("distance", distance);
            data.put("robot", target.getName());
            data.put("state", world.getRobotState(target.getName()));

            SimpleServer client = target.getServer();
            client.outToOneClient(client, new ResponseProtocol(world.getRobotState(target.getName())));
            if (target.getShieldStrength() < 0) {
                world.removeRobot(target.getName());
                client.deActivate();
                client.outToAllClients(deathByFireBroadcast(target.getName()));
            }
        }

        world.robotShotABullet(getRobotName());

        state.put("shots", world.getRobotShots(getRobotName()));
        return new ResponseProtocol("OK", data, state);
    }

    private ResponseProtocol outOfAmmo(World world) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = new HashMap<>();

        data.put("message", "Out of ammo");
        state.put("shots", world.getRobotShots(getRobotName()));

        return new ResponseProtocol("ERROR", data, state);
    }

    private ResponseProtocol robotHasNoGun() {
        Map<String, Object> data = new HashMap<>();

        data.put("message", "Robot has no gun");

        return new ResponseProtocol("ERROR", data);
    }

    private ResponseProtocol deathByFireBroadcast(String name) {
        Map<String, Object> data = new HashMap<>();

        data.put("message", name.toUpperCase() + " got shot and died.");

        return new ResponseProtocol("OK", data);
    }
}
