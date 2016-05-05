/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesnake;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author user
 */
public class Game extends JComponent {

    private final int WIDTH = 800;  // Game width
    private final int HEIGHT = 640; // Game height
    private final int SIZEX = 16;   // Width of a square
    private final int SIZEY = 16;   // Height of a square

    private Canvas canvas;
    private Snake snake;
    private Meal meal;
    private List<Barrier> barriers;

    private final Random rand = new Random();
    private volatile boolean stop = false;       // stops the game thread
    private boolean eating = false;     // Shows that the snake s eating
    private boolean isBorder = true;    // Is border or snake may goes threw
    private boolean isLines = false;    // The game is divided by squares
    private boolean isAim = false;      // Aim lines
    private int numberBarriers = 25;    // Number of barriers

    private int score = 0;
    private boolean pause = false;      // Game is paused or not
    private boolean started = false;    // Shows that game is started or is before starting
    private final Thread gameThread;

    // Stops game thread
    public synchronized void close() {
        stop = true;
    }

    public Game() {
        setLayout(new BorderLayout());

        snake = new Snake(SIZEX, SIZEY);
        meal = new Meal(new Point(rand.nextInt(WIDTH / SIZEX), rand.nextInt(HEIGHT / SIZEY)), SIZEX, SIZEY);
        barriers = new ArrayList<>();

        for (int i = 0; i < numberBarriers; i++) {
            barriers.add(new Barrier(new Point(rand.nextInt(WIDTH / SIZEX), rand.nextInt(HEIGHT / SIZEY)), SIZEX, SIZEY));
        }

        canvas = new Canvas(WIDTH, HEIGHT, SIZEX, SIZEY, snake, meal, barriers, isBorder, isLines, isAim);
        add(canvas, BorderLayout.CENTER);

        // Key Events
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "BTN_UP");
        getActionMap().put("BTN_UP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirections().get(snake.getDirections().size() - 1) != Direction.UP
                        && snake.getDirections().get(snake.getDirections().size() - 1) != Direction.DOWN) {
                    snake.getDirections().add(Direction.UP);
                }
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "BTN_RIGHT");
        getActionMap().put("BTN_RIGHT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirections().get(snake.getDirections().size() - 1) != Direction.RIGHT
                        && snake.getDirections().get(snake.getDirections().size() - 1) != Direction.LEFT) {
                    snake.getDirections().add(Direction.RIGHT);
                }
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "BTN_DOWN");
        getActionMap().put("BTN_DOWN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirections().get(snake.getDirections().size() - 1) != Direction.DOWN
                        && snake.getDirections().get(snake.getDirections().size() - 1) != Direction.UP) {
                    snake.getDirections().add(Direction.DOWN);
                }
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "BTN_LEFT");
        getActionMap().put("BTN_LEFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirections().get(snake.getDirections().size() - 1) != Direction.LEFT
                        && snake.getDirections().get(snake.getDirections().size() - 1) != Direction.RIGHT) {
                    snake.getDirections().add(Direction.LEFT);
                }
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "BTN_PAUSE");
        getActionMap().put("BTN_PAUSE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause = !pause;
                canvas.setPause(pause);
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "BTN_START");
        getActionMap().put("BTN_START", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setStarted(true);
                started = true;
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "BTN_AIM");
        getActionMap().put("BTN_AIM", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAim = !isAim;
                canvas.setIsAim(isAim);

            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "BTN_LINES");
        getActionMap().put("BTN_LINES", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isLines = !isLines;
                canvas.setIsLines(!canvas.getIsLines());
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "BTN_BORDER");
        getActionMap().put("BTN_BORDER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBorder = !isBorder;
                canvas.setIsBorder(isBorder);
            }
        });

        gameThread = new Thread(() -> {
            while (!stop) {
                if (!pause && started) {
                    Direction direction = snake.getDirections().get(0);
                    if (snake.getDirections().size() > 1) {
                        snake.getDirections().remove(0);
                    }
                    if (direction == Direction.LEFT) {
                        snake.setPosition(new Point(snake.getPosition().x - 1, snake.getPosition().y));
                    }
                    if (direction == Direction.RIGHT) {
                        snake.setPosition(new Point(snake.getPosition().x + 1, snake.getPosition().y));
                    }
                    if (direction == Direction.UP) {
                        snake.setPosition(new Point(snake.getPosition().x, snake.getPosition().y - 1));
                    }
                    if (direction == Direction.DOWN) {
                        snake.setPosition(new Point(snake.getPosition().x, snake.getPosition().y + 1));
                    }

                    onBehindBorder(isBorder); // define what to do if the snake is behind the main game field

                    onEat();    // The snake does on eating

                    // check if the snake cuts into barriers
                    if (numberBarriers > 0) {
                        for (Barrier barrier : barriers) {
                            if (snake.getPosition().x == barrier.getPosition().x
                                    && snake.getPosition().y == barrier.getPosition().y) {
                                onHit();
                            }
                        }
                    }

                    // check if the snake cuts into itself
                    // Cycle starts with a value 3, becase 
                    // if a length of the snake < 3, it is impossible the snake cuts into itself
                    for (int i = 3; i < snake.getLength(); i++) {
                        SnakeBlock block = snake.getBlocks().get(i);
                        if (snake.getPosition().x == block.getPosition().x
                                && snake.getPosition().y == block.getPosition().y
                                && !eating) {
                            onHit();
                        }
                    }

                    // if the snake eat a meal, new one will appear and it will not on some of barrier positions
                    while (eating && numberBarriers > 0) {
                        for (Barrier barrier : barriers) {
                            if (meal.getPosition().x == barrier.getPosition().x
                                    && meal.getPosition().y == barrier.getPosition().y) {
                                meal.setPosition(new Point(rand.nextInt(WIDTH / SIZEX), rand.nextInt(HEIGHT / SIZEY)));
                            } else {
                                eating = false;
                            }
                        }
                    }
                    eating = false;
                }
                canvas.repaint(); // repaint canvas

                try {
                    Thread.sleep((int) (1000 / snake.getSpeed()));  // Speed of the game
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        gameThread.start();
    }

    /**
     * Sets number of barriers and makes a list barriers after it sends this
     * list to canvas
     *
     * @param numberBarriers
     */
    public void setNumberBarriers(int numberBarriers) {
        this.numberBarriers = numberBarriers;
        barriers = new ArrayList<>();
        for (int i = 0; i < numberBarriers; i++) {
            barriers.add(new Barrier(new Point(rand.nextInt(WIDTH / SIZEX), rand.nextInt(HEIGHT / SIZEY)), SIZEX, SIZEY));
        }
        canvas.setBarriers(barriers);
    }

    /*
        Defines what to do if the snake cuts into something
     */
    private void onHit() {
        stop = true;
        canvas.setGameOver();
    }

    /*
        Defnes the snake do if it's position is behind the gmae field 
     */
    private void onBehindBorder(boolean isBorder) {
        if (snake.getPosition().x > (WIDTH / SIZEX) - 1) {
            // If it is border, then the snake hits
            // else will continue in other side of the field
            if (isBorder) {
                onHit();
            } else {
                snake.setPosition(new Point(0, snake.getPosition().y));
            }
        }
        if (snake.getPosition().x < 0) {
            if (isBorder) {
                onHit();
            } else {
                snake.setPosition(new Point((WIDTH / SIZEX) - 1, snake.getPosition().y));
            }
        }
        if (snake.getPosition().y > (HEIGHT / SIZEY) - 1) {
            if (isBorder) {
                onHit();
            } else {
                snake.setPosition(new Point(snake.getPosition().x, 0));
            }
        }
        if (snake.getPosition().y < 0) {
            if (isBorder) {
                onHit();
            } else {
                snake.setPosition(new Point(snake.getPosition().x, (HEIGHT / SIZEY) - 1));
            }
        }
    }

    // Defines what to do after the snake eats a meal
    private void onEat() {
        if (snake.getPosition().x == meal.getPosition().x
                && snake.getPosition().y == meal.getPosition().y) {
            snake.setLength(snake.getLength() + 1); // To rise a length of the snake
            meal.setPosition(new Point(rand.nextInt(WIDTH / SIZEX), rand.nextInt(HEIGHT / SIZEY))); // create someone else meal
            eating = true;
            score += 1; // to rise a score by 1
            canvas.setScore(score); // Sends score to canvas
        }
    }

}
