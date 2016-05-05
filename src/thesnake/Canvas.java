/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesnake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class Canvas extends JPanel {

    private final int width;
    private final int height;
    private final int sizeX;
    private final int sizeY;

    private final Snake snake;
    private final Meal meal;
    private List<Barrier> barriers;

    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
    }
    private boolean isBorder;
    private int score = 0;
    private boolean isLines;
    private boolean isAim;

    private boolean gameOver;
    private boolean pause;
    private boolean started;

    public Canvas(int width, int height, int sizeX, int sizeY, Snake snake, Meal meal, List<Barrier> barriers,
            boolean isBorder, boolean isLines, boolean isAim) {

        this.width = width;
        this.height = height;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.snake = snake;
        this.meal = meal;
        this.barriers = barriers;

        this.isBorder = isBorder;
        this.isLines = isLines;
        this.isAim = isAim;

        setPreferredSize(new Dimension(width + 1, height + 1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.green);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw squared field 
        if (isLines) {
            g2.setColor(Color.black);
            for (int i = 0; i < getWidth(); i += sizeX) {
                g2.drawLine(i, 0, i, getHeight());
            }
            for (int i = 0; i < getHeight(); i += sizeY) {
                g2.drawLine(0, i, getWidth(), i);
            }
        }

        if (!pause) {
            // Draw aims
            if (isAim) {
                for (int i = 0; i < getHeight(); i += sizeY) {
                    g2.setColor(Color.red);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(snake.getPosition().x * sizeX + sizeX / 2, i + sizeY / 2, snake.getPosition().x * sizeX + sizeX / 2, i + sizeY / 2);
                }

                for (int i = 0; i < getWidth(); i += sizeX) {
                    g2.setColor(Color.red);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(i + sizeX / 2, snake.getPosition().y * sizeY + sizeY / 2, i + sizeX / 2, snake.getPosition().y * sizeY + sizeY / 2);
                }
            }
            // Draw the snake
            snake.paint(g2);
        }

        // Draw a meal
        meal.paint(g2);

        // Draw barriers if exist
        if (barriers.size() > 0) {
            for (Barrier barrier : barriers) {
                barrier.paint(g2);
            }
        }

        // Draw border around the field
        if (isBorder) {
            g2.setColor(Color.red);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
        }

        // Draw text 
        
        // Write score
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g2.drawString("Score: " + Integer.toString(score), 10, getHeight() - 10);

        // if pause, write "Pause"
        if (pause) {
            g2.setColor(Color.white);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 68));
            g2.drawString("PAUSE", getWidth() / 2 - 100, getHeight() / 2);
        }

        // If the game isn't started, it prints "Press s to start"
        if (!started) {
            g2.setColor(Color.white);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 68));
            g2.drawString("Press S to start", getWidth() / 2 - 180, getHeight() / 2);
        }
        
        // Prints "Game Over"
        if (gameOver) {
            g2.setColor(Color.white);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 68));
            g2.drawString("GAME OVER", getWidth() / 2 - 200, getHeight() / 2);
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isStarted() {
        return started;
    }

    public void setGameOver() {
        gameOver = true;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setIsAim(boolean isAim) {
        this.isAim = isAim;
    }

    public boolean getIsAim() {
        return isAim;
    }

    public boolean getIsLines() {
        return isLines;
    }

    public void setIsLines(boolean isLines) {
        this.isLines = isLines;
    }

    public void setIsBorder(boolean isBorder) {
        this.isBorder = isBorder;
    }

}
