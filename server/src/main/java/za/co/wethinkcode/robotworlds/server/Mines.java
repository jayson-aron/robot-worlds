package za.co.wethinkcode.robotworlds.server;

import java.util.ArrayList;

public class Mines {
    private final ArrayList<Position> mines;

    public Mines() {
        this.mines = new ArrayList<>();
    }

    public void placeMine(Position position) {
        this.mines.add(position);
    }

    public void removeMine(Position position) {
        this.mines.remove(position);
    }

    public ArrayList<Position> getMines() {
        return this.mines;
    }

    public boolean positionBlocked(Position position) {
        for (Position mine : mines) {
            if (mine.equals(position)) {
                removeMine(mine);
                return true;
            }
        }
        return false;
    }
}
