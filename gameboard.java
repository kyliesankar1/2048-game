package org.cis1200.game2048;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private final Game2048 ttt; // model for the game
    private final JLabel status; // current status text
    private JButton undoButton;

    // Game constants
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        ttt = new Game2048(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for keyclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        ttt.moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        ttt.moveDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        ttt.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        ttt.moveRight();
                        break;
                    default: return;
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    public void performUndo() {
        if (ttt.getRemainingUndos() > 0) {
            ttt.undo();
            status.setText("Undo completed! You have " + ttt.getRemainingUndos() + " undos left.");
        } else {
            status.setText("No undos remaining :(");
        }
        repaint();
    }

    public Game2048 getGame2048() {
        return ttt;
    }

    public void setUndoButton(JButton undoButtonInit) {
        this.undoButton = undoButtonInit;
    }
    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        ttt.reset();
        status.setText("Score: 0");
        repaint();
        if (undoButton != null) {
            undoButton.setEnabled(true);
        }

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        status.setText("Score: " + ttt.getScore());
        if (ttt.checkGameOver()) {
            status.setText("Game Over! Final Score: " + ttt.getScore());
            undoButton.setEnabled(false);
            JOptionPane.showMessageDialog(
                    this,
                    "Game Over!\n" +
                            "Your Score: " + ttt.getScore() + "\n" +
                            "High Score: " + ttt.getHighScore(),
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        int unitWidth = BOARD_WIDTH / 4;
        int unitHeight = BOARD_HEIGHT / 4;
        int padding = 8;

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i < 4; i++) {
            g.drawLine(unitWidth * i, 0, unitWidth * i, BOARD_HEIGHT);
            g.drawLine(0, unitHeight * i, BOARD_WIDTH, unitHeight * i);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int state = ttt.getCell(j, i);
                int x = j * unitWidth + padding;
                int y = i * unitHeight + padding;
                int tileWidth = unitWidth - 2 * padding;
                int tileHeight = unitHeight - 2 * padding;

                g.setColor(getRainbowColor(state));
                g.fillRect(x, y, tileWidth, tileHeight);

                if (state > 0) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, tileHeight / 3));
                    FontMetrics fm = g.getFontMetrics();
                    String text = String.valueOf(state);
                    int center1 = x + (tileWidth - fm.stringWidth(text)) / 2;
                    int center2 = y + (tileHeight + fm.getAscent() - fm.getDescent()) / 2;

                    g.drawString(text, center1, center2);


                }
            }
        }
    }

    private Color getRainbowColor(int state) {
        return switch (state) {
            case 2 -> Color.RED;
            case 4 -> Color.ORANGE;
            case 8 -> Color.YELLOW;
            case 16 -> Color.GREEN;
            case 32 -> Color.BLUE;
            case 64 -> Color.MAGENTA;
            case 128 -> Color.CYAN;
            case 256 -> Color.PINK;
            case 512 -> Color.DARK_GRAY;
            case 1024 -> Color.BLACK;
            case 2048 -> Color.WHITE;
            default -> new Color(205, 193, 180);
        };
    }
    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
