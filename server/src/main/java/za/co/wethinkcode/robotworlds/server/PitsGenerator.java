package za.co.wethinkcode.robotworlds.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PitsGenerator {

    private final List<Obstacle> pits;
    private final int topLeftX;
    private final int topLeftY;
    private final int bottomRightX;
    private final int bottomRightY;
    private final Random random;

    public PitsGenerator(Position topLeft, Position bottomRight) {
        this.topLeftX = topLeft.getX();
        this.topLeftY = topLeft.getY();
        this.bottomRightX = bottomRight.getX();
        this.bottomRightY = bottomRight.getY();
        this.pits = new ArrayList<>();
        this.random = new Random();
        generatePits();
    }

    private void generatePits() {
        squarePit(-4, 4);
        squarePit((topLeftX/2), (topLeftY/2));
        squarePit((topLeftX/2), (bottomRightY/2));
        squarePit((bottomRightX/2), (topLeftY/2));
        squarePit((bottomRightX/2), (bottomRightY/2));
    }

    private void squarePit(int x1, int y) {
        pits.add(new Obstacle(x1, y));
        pits.add(new Obstacle(x1 + 4, y));
        pits.add(new Obstacle(x1, y - 4));
        pits.add(new Obstacle(x1 + 4, y - 4));
    }

    public List<Obstacle> getPits() {
        return this.pits;
    }

    public boolean positionBlocked(Position position) {
        for (Obstacle pit : pits) {
            if (pit.blocksPosition(position))
                return true;
        }
        return false;
    }
}
