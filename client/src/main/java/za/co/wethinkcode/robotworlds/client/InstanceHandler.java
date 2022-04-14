package za.co.wethinkcode.robotworlds.client;

import com.google.gson.Gson;
import za.co.wethinkcode.robotworlds.clientprotocols.RequestProtocol;
import za.co.wethinkcode.robotworlds.clientprotocols.ResponseProtocol;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstanceHandler {
    private boolean robotInWorld;
    private AbstractRobot clientRobot;
    private final PrintStream out;

    public InstanceHandler(PrintStream out) {
        this.out = out;
        this.robotInWorld = false;
    }

    public String parseRequest(String input) {
        Gson gson = new Gson();
        RequestProtocol requestProtocol;
        String[] inputList = input.trim().toLowerCase().split(" ");
        Pattern arg1 = Pattern.compile("recon|sniper|soldier|assassin");

        if ((inputList.length == 1) && !robotInWorld && inputList[0].equals("exit")
                && robotMatch(arg1, "recon"))
            createRobotInstance("", "recon");
        else if ((inputList.length == 3) && !robotInWorld && inputList[0].equals("launch")
                && robotMatch(arg1, inputList[1]))
            createRobotInstance(inputList[2], inputList[1]);

        try {
            switch (inputList.length) {
                case 1:
                    requestProtocol = new RequestProtocol(clientRobot.getName(),
                            inputList[0], new String[]{""});
                    break;
                case 2:
                    requestProtocol = new RequestProtocol(clientRobot.getName(),
                            inputList[0], new String[]{inputList[1]});
                    break;
                case 3:
                    requestProtocol = new RequestProtocol(clientRobot.getName(),
                            inputList[0], clientRobot.getArguments());
                    break;
                default:
                    requestProtocol = new RequestProtocol("", "", new String[]{});
            }

        } catch (NullPointerException e) {
            requestProtocol = new RequestProtocol("", "", new String[]{});
        }

        return gson.toJson(requestProtocol);
    }

    private void createRobotInstance(String name, String kind) {

        switch (kind) {
            case "recon":
                clientRobot = new ReconRobot(name);
                break;
            case "sniper":
                clientRobot = new SniperRobot(name);
                break;
            case "soldier":
                clientRobot = new SoldierRobot(name);
                break;
            case "assassin":
                clientRobot = new AssassinRobot(name);
                break;
            default:
                throw new IllegalArgumentException("Illegal argument!");
        }
    }

    private boolean robotMatch(Pattern arg1, String kind) {
        Matcher matcher = arg1.matcher(kind);

        return matcher.matches();
    }

    public ResponseProtocol parseResponse(String request) {
        Gson gson = new Gson();
        return gson.fromJson(request, ResponseProtocol.class);
    }

    private void printResponseData(ResponseProtocol response) {
        if (!(response.getData() == null)) {
            if (response.getData().size() == 1 && response.getData().containsKey("message")) {
                System.out.print(String.format("%-7s|", "MESSAGE"));
                System.out.println(" " + response.getData().get("message"));
            }
            else {
                if (response.getData().containsKey("message")) {
                    System.out.print(String.format("%-7s|", "MESSAGE"));
                    System.out.println(" " + response.getData().get("message"));
                }
                System.out.print(String.format("%-7s|", "DATA"));
                for (String key : response.getData().keySet()) {
                    if (key.equals("message"))
                        continue;
                    System.out.print(" " + key + ":" + response.getData().get(key));
                }
                System.out.println();
            }
        }
    }

    private void printResponseState(ResponseProtocol response) {
        if (!(response.getState() == null)) {
            System.out.print(String.format("%-7s|", "STATE"));
            for (String key : response.getState().keySet()) {
                System.out.print(" " + key + ":" + response.getState().get(key));
            }
            System.out.println();
        }
    }

    public void printResponse(ResponseProtocol response) {
        System.out.println();
        printResponseData(response);
        printResponseState(response);
        System.out.println();
    }

    public void processResponse(ResponseProtocol response) {
        if (!robotInWorld)
            checkLaunchResult(response);
        else if (robotInWorld)
            checkRobotStatus(response);
    }

    private void checkLaunchResult(ResponseProtocol response) {
        if (response.getResult().equals("OK") &&
                (response.getData() != null) && (response.getState() != null))
            this.robotInWorld = true;
    }

    private void checkRobotStatus(ResponseProtocol response) {
        try {
            if (response.getState().get("status").toString().equals("DEAD")) {
                this.robotInWorld = false;
                System.out.println("Please wait for 30 seconds before you can play again...");
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printWelcomeMessage();
            } else if (response.getState().get("status").toString().equals("SETMINE")) {
                String request = parseRequest("forward 1");
                out.println(request);
            }
        } catch (NullPointerException | IOException e) {

        }
    }

    public void printWelcomeMessage() throws IOException {
        System.out.println("\n\n*************** TOY ROBOT WORLDS ***************\n");
        System.out.println("Help Menu---------------------------------------------------------------------");
        System.out.println("LAUNCH <make> <name> - launch a robot into the world.\n" +
                "STATE - ask the world for the state of the robot.\n" +
                "LOOK - look around in the world.\n" +
                "FORWARD | BACK <steps> - move forward or backward in the world.\n" +
                "TURN <left> | <right> - turn left or right.\n" +
                "REPAIR - instruct the robot to repair its shields.\n" +
                "RELOAD - instruct the robot to reload its weapons.\n" +
                "MINE - instruct the robot to set a mine.\n" +
                "FIRE - instruct the robot to fire its gun.\n" +
                "EXIT - disconnect from the server and exit.\n" +
                "QUIT - remove your robot from the world.");
        System.out.println("------------------------------------------------------------------------------\n");
    }

    public void shutDown() {
        System.out.println("\nExiting Toy Robot Worlds. Thank you for playing...");
        System.exit(0);
    }
}
