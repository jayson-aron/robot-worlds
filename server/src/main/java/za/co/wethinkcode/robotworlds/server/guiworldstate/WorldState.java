package za.co.wethinkcode.robotworlds.server.guiworldstate;

import za.co.wethinkcode.robotworlds.server.Obstacle;
import za.co.wethinkcode.robotworlds.server.Position;
import za.co.wethinkcode.robotworlds.server.Robot;
import za.co.wethinkcode.robotworlds.server.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class WorldState extends JFrame {

    private int width, height;
    private Container contentPane;
    private DrawingComponent drawingComponent;
    private ArrayList<ObjectSprite> staticObjects;
    private ArrayList<ObjectSprite> nonStaticObjects;
    private Timer updateTimer;
    private World world;

    public WorldState(World world) {
        this.world  = world;
        this.width = world.getConfigurations().getBottomRightPosition().getX() * 4;
        this.height = world.getConfigurations().getTopLeftPosition().getY() * 4;
        staticObjects = new ArrayList<>();
        nonStaticObjects = new ArrayList<>();
        getStaticWorldObjects();
    }

    private void setUpdateTimer() {
        int interval = 1000;
        ActionListener actionListener = e -> {
            nonStaticObjects = new ArrayList<>();
            getNonStaticWorldObjects();
            drawingComponent.repaint();
        };
        updateTimer = new Timer(interval, actionListener);
        updateTimer.start();
    }

    private class DrawingComponent extends JComponent {
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            for (ObjectSprite objectSprite : staticObjects)
                objectSprite.drawSprite(graphics2D);
            for (ObjectSprite objectSprite : nonStaticObjects)
                objectSprite.drawSprite(graphics2D);
        }
    }

    public void showWorldState() {
        contentPane = this.getContentPane();
        this.setTitle("Robot World Monitor");
        contentPane.setPreferredSize(new Dimension(width, height));
        drawingComponent = new DrawingComponent();
        contentPane.add(drawingComponent);
        this.pack();
        this.setVisible(true);

        setUpdateTimer();
    }

    private void getStaticWorldObjects() {
        int x;
        int y;

       List<Obstacle> obstacles = world.getAllObstacles();
        if(!obstacles.isEmpty()) {
            for(Obstacle obstacle: obstacles){
                x = obstacle.getTopLeftX();
                y = obstacle.getTopLeftY();
                addStaticWorldObject(addObjectToGUI(x, y, 8, width, height, Color.RED));
            }
        }

        List<Obstacle> pits = world.getAllPits();
        if(!pits.isEmpty()) {
            for(Obstacle pit: pits){
                x = pit.getTopLeftX();
                y = pit.getTopLeftY();
                addStaticWorldObject(addObjectToGUI(x, y, 8, width, height, Color.BLACK));
            }
        }
    }

    private void getNonStaticWorldObjects() {
        int x;
        int y;

        List<Robot> robots = world.getAllRobots();
        if(!robots.isEmpty()) {
            for(Robot robot: robots){
                x = robot.getCurrentPosition().getX();
                y = robot.getCurrentPosition().getY();
                addNonStaticWorldObject(addObjectToGUI(x, y, 4, width, height, Color.BLUE));
            }
        }

        List<Position> mines = world.getAllMines();
        if(!mines.isEmpty()) {
            for(Position mine: mines){
                x = mine.getX();
                y = mine.getY();
                addNonStaticWorldObject(addObjectToGUI(x, y, 4, width, height, Color.RED));
            }
        }
    }

    private ObjectSprite addObjectToGUI(int x, int y, int size, int width, int height, Color color) {
        x = (x >= 0) ? ((width/2) + (x * 2)): ((width/2) + (x * 2));
        y = (y >= 0) ? height - ((height/2) + (y * 2)) : ((height/2) - (y * 2));
        return new ObjectSprite(x, y, size, color);
    }

    public void addStaticWorldObject(ObjectSprite objectSprite) {
        staticObjects.add(objectSprite);
    }

    public void addNonStaticWorldObject(ObjectSprite objectSprite) {
        nonStaticObjects.add(objectSprite);
    }
}
