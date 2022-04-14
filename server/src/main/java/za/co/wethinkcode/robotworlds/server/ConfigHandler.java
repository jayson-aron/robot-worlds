package za.co.wethinkcode.robotworlds.server;

public class ConfigHandler {
    private final int TOP_LEFT_X;
    private final int TOP_LEFT_Y;
    private final int VISIBILITY;
    private final int SHIELD_REPAIR_TIME;
    private final int WEAPON_RELOAD_TIME;
    private final int MINE_SET_TIME;
    private final int MAX_SHIELD_STRENGTH;

    public ConfigHandler(int x, int y, int v, int shieldRepair, int weapon, int mine, int shieldStrength) {
        this.TOP_LEFT_X = x;
        this.TOP_LEFT_Y = y;
        this.VISIBILITY = v;
        this.SHIELD_REPAIR_TIME = shieldRepair;
        this.WEAPON_RELOAD_TIME = weapon;
        this.MINE_SET_TIME = mine;
        this.MAX_SHIELD_STRENGTH = shieldStrength;
    }

    public Position getTopLeftPosition() {
        return new Position(this.TOP_LEFT_X, this.TOP_LEFT_Y);
    }

    public Position getBottomRightPosition() {
        return new Position(-this.TOP_LEFT_X, -this.TOP_LEFT_Y);
    }

    public int getVisibility() {
        return this.VISIBILITY;
    }

    public int getShieldRepairTime() {
        return this.SHIELD_REPAIR_TIME;
    }

    public int getWeaponReloadTime() {
        return this.WEAPON_RELOAD_TIME;
    }

    public int getMineSetTime() {
        return this.MINE_SET_TIME;
    }

    public int getMaxShieldStrength() {
        return this.MAX_SHIELD_STRENGTH;
    }
}
