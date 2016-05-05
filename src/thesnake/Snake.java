/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesnake;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author user
 */
public class Snake {

    private int speed = 10; // It is the snake's speed

    private int length = 3; // Beginning length of the snake
    private Point position = new Point(0, 0);
    private final List<SnakeBlock> blocks = new ArrayList<>();  // Blocks from which the snake is consist
    private final int sizeX;
    private final int sizeY;

    private final List<Direction> directions = new ArrayList<>(); // List of directions, that gamer pushes on a keyboard

    public Snake(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        directions.add(Direction.RIGHT);
        // Make the snake
        for (int i = position.x + length; i > position.x; i--) {
            blocks.add(new SnakeBlock(new Point(i, position.y), sizeX, sizeY));
        }
        Collections.reverse(blocks);
    }

    // Paint the snake
    public void paint(Graphics2D g2) {
        // On moveing the last block of snake removes from the list
        // And adds new one on the new position
        blocks.add(new SnakeBlock(position, sizeX, sizeY));
        blocks.remove(0);

        // Repaint all blocks
        for (SnakeBlock block : blocks) {
            block.paint(g2);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        blocks.add(new SnakeBlock(position, sizeX, sizeY));
        if (speed < 20) {   // With riseing a length of the snake, rises it's speed
            if (length % 10 == 0) {
                speed += 1;
            }
        }
    }

    public List<SnakeBlock> getBlocks() {
        return blocks;
    }

    public List<Direction> getDirections() {
        return directions;
    }
}
