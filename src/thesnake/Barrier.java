/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesnake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *  
 * @author user
 */
public class Barrier {
    private final Point position;
    private final int sizeX;
    private final int sizeY;

    public Barrier(Point position, int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.position = position;
    }

    void paint(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(position.x * sizeX + 1, position.y * sizeY + 1, sizeX - 1, sizeY - 1);
    }

    public Point getPosition() {
        return position;
    }
}
