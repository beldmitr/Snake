/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesnake;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class MainForm extends JFrame {

    private final Game game;

    public MainForm() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("The Snake");
        setResizable(false);

        game = new Game();
        game.setNumberBarriers(25); // Create number of barriers
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // on close window, thread with game will be closed too. 
                // Method close() is synchronized
                game.close();   
            }
        });
        
        setContentPane(game); // A main content of a form is the game
        pack();
        setLocationRelativeTo(null);    // Position of the form is centered
    }

}
