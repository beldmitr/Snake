/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesnake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author user
 */
public class Meal {

    private Point position;
    private final int sizeX;
    private final int sizeY;
    private Color color;
    private final Random rand;

    public Meal(Point position, int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.position = position;
        rand = new Random();
        color = new Color(rand.nextInt(255), 0, rand.nextInt(255));
    }

    void paint(Graphics2D g2) {
        color = new Color(rand.nextInt(255), 0, rand.nextInt(255));
        g2.setColor(color);
        g2.fillRect(position.x * sizeX + 1, position.y * sizeY + 1, sizeX - 1, sizeY - 1);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
        color = new Color(rand.nextInt(255), 0, rand.nextInt(255));
    }

}
