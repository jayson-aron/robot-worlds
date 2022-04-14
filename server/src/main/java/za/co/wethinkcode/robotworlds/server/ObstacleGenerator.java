package za.co.wethinkcode.robotworlds.server;

import java.util.ArrayList;
import java.util.List;

public class ObstacleGenerator {
    
    private List<Obstacle> obstacles;
    private final int topLeftX;
    private final int topLeftY;
    private final int bottomRightX;
    private final int bottomRightY;
    
    public ObstacleGenerator(Position topLeft, Position bottomRight) {
        this.topLeftX = topLeft.getX();
        this.topLeftY = topLeft.getY();
        this.bottomRightX = bottomRight.getX();
        this.bottomRightY = bottomRight.getY();
        this.obstacles = new ArrayList<>();
        generateObstacles();
    }

    private void generateObstacles() {
        horizontalObstacleWall(topLeftX + 16, topLeftY - 16, bottomRightX - 16);
        horizontalObstacleWall(topLeftX + 16, bottomRightY + 16, bottomRightX - 16);
        horizontalObstacleWall(topLeftX, 2, -16);
        horizontalObstacleWall(16, 2, bottomRightX);

        verticalObstacleWall(20, topLeftX + 16, topLeftY - 24);
        verticalObstacleWall(20, bottomRightX - 20, topLeftY - 24);
        verticalObstacleWall(bottomRightY + 28, topLeftX + 16, -8);
        verticalObstacleWall(bottomRightY + 28, bottomRightX - 20, -8);
    }

    private void horizontalObstacleWall(int x1, int y, int x2) {
        while (x1 < x2) {
            obstacles.add(new Obstacle(x1, y));
            x1 += 4;
        }
    }

    private void verticalObstacleWall(int y1, int x, int y2) {
        while (y1 < y2) {
            obstacles.add(new Obstacle(x, y1));
            y1 += 4;
        }
    }

    public boolean positionBlocked(Position position) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.blocksPosition(position))
                return true;
        }
        return false;
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }
}
