package za.co.wethinkcode.robotworlds.server;

public class Obstacle {
    private final int x;
    private final int y;

    public Obstacle(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getTopLeftX() {
        return this.x;
    }

    public int getTopLeftY() {
        return this.y;
    }

    public boolean blocksPosition(Position position) {
        Position topLeft = new Position(x, y);
        Position bottomRight = new Position(x + 4, y - 4);
        return (position.isIn(topLeft, bottomRight));
    }
}