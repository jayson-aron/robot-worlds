package za.co.wethinkcode.tests.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.server.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobotTest {

    @Test
    public void testDefaultDirectionRobot() {
        Robot robot = new Robot("Anton", 5, 2, new Position(0, 0));
        assertEquals(Direction.NORTH, robot.getCurrentDirection());
    }

    @Test
    public void testRobotName() {
        Robot robot = new Robot("Anton", 5, 2, new Position(0, 0));
        assertEquals("Anton", robot.getName());
    }

    @Test
    public void testRobotPosition() {
        Robot robot = new Robot("Anton", 5, 2, new Position(0, 0));
        assertEquals(new Position(0,0),robot.getCurrentPosition());
    }

    @Test
    public void testSetDirection() {
        Robot robot = new Robot("Anton", 5, 2, new Position(0, 0));
        robot.setDirection(Direction.EAST);
        assertEquals(Direction.EAST, robot.getCurrentDirection());
    }
}
