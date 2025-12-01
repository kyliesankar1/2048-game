package org.cis1200.game2048;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class Run2048 implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Undo button
        final JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            board.performUndo();
            board.requestFocusInWindow();
        });
        control_panel.add(undoButton);
        board.setUndoButton(undoButton);

        // Read me

        final JButton readMe = new JButton("?");
        readMe.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame," Welcome to 2048!\n If you've played this"
                    + " game before the usual rules apply:\n" +
                    " Use the keys to shift the tiles" +
                    "\n The goal is to get to make bigger tiles by combining tiles of the " +
                    "same value " +
                    "\n The game ends when the board is filled " +
                    "with tiles and there are no adjacent tiles which can merge\n" +
                    " However, there is an addition: " +
                    "\n There is an undo feature which can be used up to 2 times " +
                    "to reverse a move that you have done\n " +
                    "Note: you cannot " +
                    "undo after you have already lost\n Enjoy the game!");

            board.requestFocusInWindow();
        });

        control_panel.add(readMe);

        // Save button
        final JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String filename = JOptionPane.showInputDialog(frame,
                    "Enter a file name to save (for eg: game1.txt):");
            if (filename != null && !filename.trim().isEmpty()) {
                board.getGame2048().saveGame(filename.trim());
                board.requestFocusInWindow();
                JOptionPane.showMessageDialog(frame, "Game was saved!", "Save game",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        control_panel.add(saveButton);

        //Load button
        final JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            String filename = JOptionPane.showInputDialog(frame,
                    "Enter the file name to load (for eg: game1):");
            if (filename != null && !filename.trim().isEmpty()) {
                boolean success = false;
                board.getGame2048().loadGame(filename.trim());
                board.repaint();
                Game2048 game = board.getGame2048();
                Game2048.LoadStatus loadStat = game.loadGame(filename.trim());
                switch (loadStat) {
                    case SUCCESS  ->
                    JOptionPane.showMessageDialog(frame, "Game was Loaded!",
                            "Load Game", JOptionPane.INFORMATION_MESSAGE);
                    case FILE_NOT_FOUND -> JOptionPane.showMessageDialog(frame,
                            "Error: File not found. Please ensure the file exists and try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    case INVALID_FORMAT -> JOptionPane.showMessageDialog(frame,
                            "Error: Invalid save file format. " +
                                    "Ensure the file is a valid save file.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    case IO_ERROR -> JOptionPane.showMessageDialog(frame,
                            "Error: An unexpected I/O error occurred while loading the file.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    default -> JOptionPane.showMessageDialog(frame,
                            "Error: An unknown error occurred while loading the file.",
                            "Error", JOptionPane.ERROR_MESSAGE); }

                board.requestFocusInWindow();
                if (loadStat == Game2048.LoadStatus.SUCCESS) {
                    board.repaint();
                    board.requestFocusInWindow();
                }
            }
        });

        control_panel.add(loadButton);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
