package za.co.wethinkcode.tests.client;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.client.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClientRobots {

    @Test
    public void testSniperRobot() {
        AbstractRobot robot = new SniperRobot("Anton");
        assertEquals("ANTON", robot.getName());
        assertEquals("sniper", robot.getKind());
        assertEquals(5, robot.getShieldStrenth());
        assertEquals(1, robot.getMaxShots());
    }

    @Test
    public void testReconRobot() {
        AbstractRobot robot = new ReconRobot("Anton");
        assertEquals("ANTON", robot.getName());
        assertEquals("recon", robot.getKind());
        assertEquals(4, robot.getShieldStrenth());
        assertEquals(0, robot.getMaxShots());
    }

    @Test
    public void testAssassinRobot() {
        AbstractRobot robot = new AssassinRobot("Anton");
        assertEquals("ANTON", robot.getName());
        assertEquals("assassin", robot.getKind());
        assertEquals(4, robot.getShieldStrenth());
        assertEquals(2, robot.getMaxShots());
    }

    @Test
    public void testSoldierRobot() {
        AbstractRobot robot = new SoldierRobot("Anton");
        assertEquals("ANTON", robot.getName());
        assertEquals("soldier", robot.getKind());
        assertEquals(3, robot.getShieldStrenth());
        assertEquals(3, robot.getMaxShots());
    }
}
