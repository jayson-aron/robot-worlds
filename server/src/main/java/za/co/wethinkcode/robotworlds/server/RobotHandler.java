package za.co.wethinkcode.robotworlds.server;

import java.util.ArrayList;

public class RobotHandler {
    private ArrayList<Robot> robotList;

    public RobotHandler() {
        this.robotList = new ArrayList<>();
    }

    public void addRobot(Robot robot) {
        this.robotList.add(robot);
    }

    public int getRobotCount() {
        return this.robotList.size();
    }

    public boolean robotExists(String name) {

        for (Robot robot : robotList)
            if (robot.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }

    public Robot getRobot(String name) {
        int index;

        for (index = 0; index < this.robotList.size(); index++)
            if (this.robotList.get(index).getName().equalsIgnoreCase(name))
                break;
        return this.robotList.get(index);
    }

    public ArrayList<Robot> getAllRobots() {
        return robotList;
    }

    public boolean removeRobot(Robot robot) {
        return this.robotList.remove(robot);
    }

    public Robot getRobotAt(Position position) {

        for (Robot robot : robotList) {
            if (robot.getCurrentPosition().equals(position)) {
                return robot;
            }
        }
        return null;
    }

    public boolean positionBlocked(Position position) {
        for (Robot robot : robotList) {
            if (robot.getCurrentPosition().equals(position))
                return true;
        }
        return false;
    }
}
