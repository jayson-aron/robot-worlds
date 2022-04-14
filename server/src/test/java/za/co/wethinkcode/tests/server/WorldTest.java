package za.co.wethinkcode.tests.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.server.ConfigHandler;
import za.co.wethinkcode.robotworlds.server.Direction;
import za.co.wethinkcode.robotworlds.server.World;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    @Test
    public void testWorldAddRobot() {
        World world = new World(new ConfigHandler(-100, 200, 10, 10, 10,10, 10));
        world.addRobot("Anton", 5, 2, null);
        world.addRobot("Eren", 4, 0, null);
        assertEquals( 2, world.getRobotCount());
    }

    @Test
    public void testNameExits() {
        World world = new World(new ConfigHandler(-100, 200, 10, 10, 10,10, 10));
        world.addRobot("Anton", 5, 25, null);
        assertTrue(world.robotExists("Anton"));
    }

    @Test
    public void testNameDoesNotExits() {
        World world = new World(new ConfigHandler(-100, 200, 10, 10, 10,10, 10));
        world.addRobot("Anton", 5, 25, null);
        assertFalse(world.robotExists("Hul"));
    }

    @Test
    public void testWorldConfigurations() {
        World world = new World(new ConfigHandler(-100, 200, 10, 5, 10,15, 5));
        assertEquals(10, world.getVisibility());
        assertEquals(5, world.getShieldRepairTime());
        assertEquals(10, world.getWeaponsReloadTime());
        assertEquals(15, world.getMineSetTime());
    }

    @Test
    public void testChangeRobotDirection() {
        World world = new World(new ConfigHandler(-100, 200, 10, 10, 10,10, 10));
        world.addRobot("Anton", 5, 2, null);
        world.addRobot("Eren", 4, 0, null);
        world.setRobotDirection("anton", "left");
        world.setRobotDirection("eren", "right");
        assertEquals(Direction.WEST, world.getRobot("anton").getCurrentDirection());
        assertEquals(Direction.EAST, world.getRobot("eren").getCurrentDirection());
    }

}
