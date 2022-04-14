package za.co.wethinkcode.robotworlds.server.servercommands;

import za.co.wethinkcode.robotworlds.server.Robot;
import za.co.wethinkcode.robotworlds.server.World;

import java.util.Map;

public class RobotsCommand extends ServerCommands {

    public RobotsCommand() {
        super("robots");
    }

    @Override
    public boolean execute(World world) {
        String robotName;
        if (world.getAllRobots().isEmpty()) {
            System.out.println("The are currently no robots in the world");
            return true;
        }
        int count = 1;
        for (Robot robot : world.getAllRobots()) {
            robotName = robot.getName();
            Map<String, Object> state = world.getRobotState(robotName);

            System.out.println(count + ". " + robotName.toUpperCase() +
                    " --> Position: " + state.get("position") +
                    " Direction: " + state.get("direction") +
                    " Shields: " + state.get("shields") +
                    " Shots: " + state.get("shots") +
                    " Status: " + state.get("status"));
            count += 1;
        }
        return true;
    }
}
