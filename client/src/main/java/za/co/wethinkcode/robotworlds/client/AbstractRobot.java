package za.co.wethinkcode.robotworlds.client;

public abstract class AbstractRobot {
    private String name;
    private String kind;
    private int[] position;
    private String direction;
    private String status;
    private String shieldStrength;
    private String maxShots;

    public AbstractRobot(String kind, String name, int shieldStrength, int maxShots) {
        this.name = name.toUpperCase();
        this.kind = kind.toLowerCase();
        this.maxShots = String.valueOf(maxShots);
        this.shieldStrength = String.valueOf(shieldStrength);
    }

    public String getName() {
        return this.name;
    }

    public String getKind() {
        return this.kind;
    }

    public int getMaxShots() {
        return Integer.parseInt(maxShots);
    }

    public int getShieldStrenth() {
        return Integer.parseInt(shieldStrength);
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public String[] getArguments() {
        return new String[] {this.kind, this.shieldStrength, this.maxShots};
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
