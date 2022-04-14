package za.co.wethinkcode.tests.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.server.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandTest {

    @Test
    public void testForwardCommand() {
        Command command = new ForwardCommand("Anton", new String[]{"10"});
        assertEquals("forward", command.getCommandName());
    }

    @Test
    public void testBackCommand() {
        Command command = new BackCommand("Anton", new String[]{"10"});
        assertEquals("back", command.getCommandName());
    }

    @Test
    public void testLaunchCommand() {
        Command command = new LaunchCommand("Anton", new String[]{});
        assertEquals("launch", command.getCommandName());
    }

    @Test
    public void testStateCommand() {
        Command command = new StateCommand("Anton");
        assertEquals("state", command.getCommandName());
    }

    @Test
    public void testTurnCommand() {
        Command command = new TurnCommand("Anton", new String[]{});
        assertEquals("turn", command.getCommandName());
    }

    @Test
    public void testMineCommand() {
        Command command = new MineCommand("Anton");
        assertEquals("mine", command.getCommandName());
    }

    @Test
    public void testRepairCommand() {
        Command command = new RepairCommand("Anton");
        assertEquals("repair", command.getCommandName());
    }

    @Test
    public void testFireCommand() {
        Command command = new FireCommand("Anton");
        assertEquals("fire", command.getCommandName());
    }

    @Test
    public void testReloadCommand() {
        Command command = new ReloadCommand("Anton");
        assertEquals("reload", command.getCommandName());
    }

    @Test
    public void testLookCommand() {
        Command command = new LookCommand("Anton");
        assertEquals("look", command.getCommandName());
    }

    @Test
    public void testExitCommand() {
        Command command = new ExitCommand("Anton");
        assertEquals("exit", command.getCommandName());
    }

    @Test
    public void testQuitCommand() {
        Command command = new QuitCommand("Anton");
        assertEquals("quit", command.getCommandName());
    }
}
