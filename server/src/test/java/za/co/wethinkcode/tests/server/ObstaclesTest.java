package za.co.wethinkcode.tests.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.server.Obstacle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObstaclesTest {

    @Test
    public void testObstacle() {
        Obstacle obstacle = new Obstacle(5, 9);
        assertEquals(5, obstacle.getTopLeftX());
        assertEquals(9, obstacle.getTopLeftY());
    }
}
