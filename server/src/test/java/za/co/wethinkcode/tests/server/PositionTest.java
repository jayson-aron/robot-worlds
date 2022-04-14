package za.co.wethinkcode.tests.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.server.Position;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionTest {

    @Test
    public void testPosition() {
        Position position = new Position(5,9);
        assertEquals(new Position(5, 9), position);
    }

    @Test
    public void testPositionInList() {
        ArrayList<Position> positions = new ArrayList<>();
        Position position = new Position(5, 9);
        Position position1 = new Position(12,21);
        positions.add(position);
        positions.add(position1);
        assertTrue(positions.contains(new Position(5,9)));
    }
}
