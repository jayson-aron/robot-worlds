package za.co.wethinkcode.tests.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.server.servercommands.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerCommandsTest {

    @Test
    public void testQuitCommand() {
        ServerCommands command = new QuitCommand();
        assertEquals("quit", command.getCommandName());
    }

    @Test
    public void testRobotsCommand() {
        ServerCommands command = new RobotsCommand();
        assertEquals("robots", command.getCommandName());
    }

    @Test
    public void testPurgeCommand() {
        ServerCommands command = new PurgeCommand("Anton");
        assertEquals("purge", command.getCommandName());
        assertEquals("anton", command.getArgument());
    }

    @Test
    public void testDumpCommand() {
        ServerCommands command = new DumpCommand();
        assertEquals("dump", command.getCommandName());
    }
}
