package com.demo.code.animal.ai;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SnakeGame extends Frame {
    private static final int DELAY = 250; // milliseconds between frames
    private static final int BOARD_WIDTH = 300;
    private static final int BOARD_HEIGHT = 300;
    private static final int SNAKE_START_LENGTH = 3;
    private static final int SNAKE_SPEED = 10;
    private static final int FOOD_SIZE = 10;

    private Snake snake;
    private Food food;
    private boolean gameOver;

    public SnakeGame() {
        super("Snake Game");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setResizable(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    snake.setDirection(0, -1);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    snake.setDirection(0, 1);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    snake.setDirection(-1, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    snake.setDirection(1, 0);
                }
            }
        });
        initGame();
        startGame();
    }

    private void initGame() {
        snake = new Snake(SNAKE_START_LENGTH);
        food = new Food(new Point(getRandomX(), getRandomY()));
        gameOver = false;
    }

    private void startGame() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!gameOver) {
                    snake.move();
                    if (snake.collidesWith(food)) {
                        snake.grow();
                        food = new Food(new Point(getRandomX(), getRandomY()));
                    } else if (snake.collidesWithSelf()) {
                        gameOver = true;
                    }
                    repaint();
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        }).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g.setColor(Color.green);
        snake.draw(g);
        g.setColor(Color.red);
        food.draw(g);
    }

    private int getRandomX() {
        return (int) (Math.random() * BOARD_WIDTH);
    }

    private int getRandomY() {
        return (int) (Math.random() * BOARD_HEIGHT);
    }

    public static void main(String[] args) {
        new SnakeGame().setVisible(true);
    }
}
