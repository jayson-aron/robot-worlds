package za.co.wethinkcode.robotworlds.server;

import za.co.wethinkcode.robotworlds.server.servercommands.ServerCommands;
import za.co.wethinkcode.robotworlds.server.guiworldstate.WorldState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MultiServers {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        ServerCommands command;
        String commandInput;
        boolean iterate = true;

        ServerService serverService = new ServerService();
        new Thread(serverService).start();
        printWelcomeMessage();

        World world = ServerService.world;
        ArrayList<SimpleServer> clients = ServerService.clients;

        if (args.length == 1 && args[0].equalsIgnoreCase("-monitor=on")) {
            WorldState worldState = new WorldState(world);
            worldState.showWorldState();
        }

        while (iterate) {
            System.out.print("Enter command: ");
            commandInput = input.nextLine().toLowerCase();
            try {
                command = ServerCommands.create(commandInput);
                iterate = command.execute(world);
            } catch (IllegalArgumentException e) {
                System.out.println("Unsupported command!");
            }
        }
        shuttingDown(clients);
    }

    private static void shuttingDown(ArrayList<SimpleServer> clients) {
        System.out.println("\nToy Robot Worlds Server shutting down...");
        System.exit(0);
    }

    private static void printWelcomeMessage() {
        System.out.println("\n*************** TOY ROBOT WORLDS SERVER ***************\n");
        System.out.println("Help Menu---------------------------------------------------------------------");
        System.out.println("QUIT - disconnects all robots and ends the world.\n" +
                "ROBOTS - lists all robots in the world including the robot’s name and state\n" +
                "PURGE <robot name> -kill the robot and remove it from the game.\n" +
                "DUMP - displays a representation of the world’s state");
        System.out.println("------------------------------------------------------------------------------\n");
    }

}
