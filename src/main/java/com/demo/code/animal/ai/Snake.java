package com.demo.code.animal.ai;

import com.sun.javafx.scene.traversal.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private Point head;
    private Direction direction;

    public Snake(int length) {
        body = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            body.add(new Point(0, 0));
        }
        head = body.get(0);
        direction = Direction.UP;
    }

    public void move() {
        Point newHead = new Point(head.x + direction, head.y + direction.y);
        body.add(0, newHead);
        head = body.get(0);
        body.remove(body.size() - 1);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void grow() {
        body.add(0, head);
    }

    public boolean collidesWith(Food food) {
        return head.equals(food.getLocation());
    }

    public boolean collidesWithSelf() {
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.green);
        for (Point point : body) {
            g.fillRect(point.x, point.y, 10, 10);
        }
    }
}
