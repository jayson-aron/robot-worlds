package za.co.wethinkcode.robotworlds.server;

import java.util.ArrayList;

public class Robot {
    private final int SHIELD_STRENGTH;
    private final int SHOTS;
    private final String NAME;
    private OperationalMode status;
    private Position position;
    private Direction currentDirection;
    private int shieldStrength;
    private int shots;
    private int steps;
    private String targetName;
    private final SimpleServer server;
    
    public Robot(String name, int shieldStrength, int shots, Position position) {
        this.NAME = name;
        this.SHIELD_STRENGTH = shieldStrength;
        this.shieldStrength = shieldStrength;
        this.SHOTS = shots;
        this.shots = shots;
        this.position = position;
        this.currentDirection = Direction.NORTH;
        this.status = OperationalMode.NORMAL;
        this.server = null;
    }
    public Robot(String name, int shieldStrength, int shots, Position position, SimpleServer server) {
        this.server = server;
        this.NAME = name;
        this.SHIELD_STRENGTH = shieldStrength;
        this.shieldStrength = shieldStrength;
        this.SHOTS = shots;
        this.shots = shots;
        this.position = position;
        this.currentDirection = Direction.NORTH;
        this.status = OperationalMode.NORMAL;
        configureSteps();
    }

    public SimpleServer getServer() {
        return this.server;
    }

    private void configureSteps() {
        this.steps = (shots > 0) ? (6 - shots) : 0;
    }

    public int getShotSteps() {
        return this.steps;
    }

    public String getName() {
        return this.NAME;
    }

    public Position getCurrentPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public void setDirection(Direction direction) {
        this.currentDirection = direction;
    }

    public void setStatus(OperationalMode status) {
        this.status = status;
    }

    public int getShieldStrength() {
        return this.shieldStrength;
    }

    public int getShots() {
        return this.shots;
    }

    public int getDefaultShots() {
        return this.SHOTS;
    }

    public void lostShot() {
        this.shots -= 1;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public OperationalMode getStatus() {
        return this.status;
    }

    public void setShieldStrength(int shieldStrength) {
        this.shieldStrength = shieldStrength;
    }

    public void resetShieldStrength() {
        shieldStrength = SHIELD_STRENGTH;
    }

    public ArrayList<Integer> getPositionArray() {
        ArrayList<Integer> position = new ArrayList<Integer>();
        position.add(this.position.getX());
        position.add(this.position.getY());
        return position;
    }
    
    public void resetShots() {
        shots = SHOTS;
    }
}
