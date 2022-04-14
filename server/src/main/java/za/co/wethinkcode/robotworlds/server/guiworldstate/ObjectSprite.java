package za.co.wethinkcode.robotworlds.server.guiworldstate;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ObjectSprite {
    private double x, y, size;
    private Color color;

    public ObjectSprite(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public void drawSprite(Graphics2D graphics2D) {
        Rectangle2D.Double square = new Rectangle2D.Double(x, y, size, size);
        graphics2D.setColor(color);
        graphics2D.fill(square);
    }
}
