package za.co.wethinkcode.robotworlds.server.servercommands;
import za.co.wethinkcode.robotworlds.server.*;
import java.util.List;
public class DumpCommand extends ServerCommands {
    public DumpCommand() {
        super("dump");
    }
    @Override
    public boolean execute(World world) {
        int x;
        int y;

        List<Obstacle> obstacles = world.getAllObstacles();
        if(!obstacles.isEmpty()) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THESE ARE THE OBSTACLES AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
            for(Obstacle obstacle: obstacles){
                x = obstacle.getTopLeftX();
                y = obstacle.getTopLeftY();
                System.out.println("\tThere is an obstacle at: ("+x+","+y+") to ("+(x+4)+","+(y-4)+").");
            }
        }
        else {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THERE ARE NO OBSTACLES AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
        }

        List<Obstacle> pits = world.getAllPits();
        if(!pits.isEmpty()) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THESE ARE THE PITS AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
            for(Obstacle pit: pits){
                x = pit.getTopLeftX();
                y = pit.getTopLeftY();
                System.out.println("\tThere is a pit at: ("+x+","+y+") to ("+(x+4)+","+(y-4)+").");
            }
        }
        else {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THERE ARE NO PITS AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
        }

        List<Robot> robots = world.getAllRobots();
        if(!robots.isEmpty()) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THESE ARE THE ROBOTS AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
            for(Robot robot: robots){
                System.out.println("\tThere is a robot : ("+robot.getName().toUpperCase()+") -At position : ("+robot.getCurrentPosition().getX()+","+robot.getCurrentPosition().getY()+")");
            }
        }
        else {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THERE ARE NO ROBOTS AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
        }

        List<Position> mines = world.getAllMines();
        if(!mines.isEmpty()) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THESE ARE THE MINES AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
            for(Position mine: mines){
                System.out.println("\tThere is a mine at: ("+mine.getX()+","+mine.getY()+")");
            }
        }
        else {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("     *** THERE ARE NO MINES AVAILABLE ***     ");
            System.out.println("-------------------------------------------------------\n");
        }
        return true;
    }
}