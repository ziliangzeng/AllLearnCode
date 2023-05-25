package com.demo.code.animal.ai;

import java.awt.*;

public class Food {

    private Point location;
    private int size;

    public Food(Point location) {
        this.location = location;
        size = 10;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(location.x, location.y, size, size);
    }
}
