package org.cis1200.game2048;
import java.io.*;
import java.util.ArrayList;
/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Game2048 {

    private int[][] board;
    private boolean gameOver;
    private ArrayList<int[][]> undoHistory = new ArrayList<>();
    private int score;
    private int highScore;
    private int undoRemaining;
    private static final int NUM_UNDOS = 2;
    private final NumberGenerator rng;

    /**
     * Constructor sets up game state.
     */
    public Game2048(NumberGenerator rng) {
        this.rng = rng;
        reset();
    }

    public Game2048() {
        this(new RandomNumberGenerator());
    }



    // Save state method to save current state to be able to undo
    private void saveCurState() {
        int[][] copyState = new int[4][4];
        for (int i = 0; i < 4; i++) {
            copyState[i] = board[i].clone();
        }
        undoHistory.add(copyState);
    }

    public void undo() {
        if (gameOver) {
            System.out.println("Cannot undo after game over!");
            return;
        }
        if (undoRemaining <= 0) {
            System.out.println("No undos remaining!");
            return;
        }
        if (!undoHistory.isEmpty()) {
            int [][] prevState =  undoHistory.remove(undoHistory.size() - 1);
            for (int i = 0; i < 4; i++) {
                board[i] = prevState[i].clone();
            }
            undoRemaining --;
            System.out.println("Undo remaining: " + undoRemaining);
        } else {
            System.out.println("No moves to undo!");
        }
    }

    public int getRemainingUndos() {
        return undoRemaining;
    }


    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n Current Game");
        for (int[] ints : board) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println();
        }
        System.out.println("Score: " + score);
        System.out.println("High Score: " + highScore);
        if (gameOver) {
            System.out.println("Game Over");
        }
    }


    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[4][4];
        gameOver = false;
        score = 0;
        undoHistory.clear();
        undoRemaining = NUM_UNDOS;
        makeTile();
        makeTile();

    }

    private boolean isGameOver() {

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
        }
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == board[row][col + 1]) {
                    return false;
                }
            }
        }
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[row][col] == board[row + 1][col]) {
                    return false;
                }
            }
        }
        gameOver = true;
        checkHighScore();
        return true;
    }

    public int getScore() {
        return score;
    }

    // Function to create the random tiles at the start of the game and throughout
    private void makeTile() {
        if (isGameOver()) {
            gameOver = true;
            System.out.println("Game Over! There are no moves left.");
            if (score > highScore) {
                highScore = score;
                System.out.println("New high score is " + highScore);
            }
            return;
        }

        ArrayList<int[]> emptyTiles = new ArrayList<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == 0) {
                    emptyTiles.add(new int[]{row, col});
                }
            }
        }

        if (!emptyTiles.isEmpty()) {
            int randomIndex = rng.next(emptyTiles.size());
            int[] selectedTile = emptyTiles.get(randomIndex);

            int row = selectedTile[0];
            int col = selectedTile[1];

            int randomNumber = rng.next(10);

            int tileVal;
            if (randomNumber < 9) {
                tileVal = 2;
            } else {
                tileVal = 4;
            }
            board[row][col] = tileVal;

        }
    }

// Move left function which would process the merging of the tiles when you click the left button

    public void moveLeft() {
        saveCurState();
        int[][] initBoard = getBoard();
        for (int row = 0; row < 4; row++) {
            int[] newRow = new int[board[row].length];
            int track = 0;
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != 0) {
                    newRow[track] = board[row][col];
                    track++;
                }
            }
            for (int col = 0; col < track - 1; col++) {
                if (newRow[col] == newRow[col + 1]) {
                    newRow[col] = newRow[col + 1] + newRow[col];
                    score += newRow[col];
                    newRow[col + 1] = 0;
                }
            }

            int[] mergedRow = new int[4];
            int finalTrack = 0;
            for (int col = 0; col < 4; col++) {
                if (newRow[col] != 0) {
                    mergedRow[finalTrack] = newRow[col];
                    finalTrack++;
                }
            }
            board[row] = mergedRow;
        }
        if (!equalBoards(initBoard,board)) {
            makeTile();
            checkHighScore();
        }
    }

    private boolean equalBoards(int[][] board1, int[][] board2) {
        for (int row = 0; row < board1.length; row++) {
            for (int col = 0; col < board1[row].length; col++) {
                if (board1[row][col] != board2[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Move right function which would process the merging of the tiles
    // when you click the left button

    public void moveRight() {
        saveCurState();
        int[][] initBoard = getBoard();
        for (int row = 0 ; row < 4; row++) {
            int[] newRow = new int[4];
            int track = 3;
            for (int col = 3 ; col >= 0 ; col--) {
                if (board[row][col] != 0) {
                    newRow[track] = board[row][col];
                    track--;
                }
            }
            for (int col = 3 ; col > 0 ; col--) {
                if (newRow[col] == newRow[col - 1] && newRow[col] != 0) {
                    newRow[col] = newRow[col] * 2 ;
                    score += newRow[col];
                    newRow[col - 1] = 0;
                }
            }
            int[] mergedRow = new int[4];
            int finalTrack = 3;
            for (int col = 3 ; col >= 0 ; col--) {
                if (newRow[col] != 0) {
                    mergedRow[finalTrack] = newRow[col];
                    finalTrack--;
                }
            }
            board[row] = mergedRow;
        }
        if (!equalBoards(initBoard,board)) {
            makeTile();
            checkHighScore();
        }
    }

    // Move up function which would process the merging of the tiles when you click the up button
    public void moveUp() {
        saveCurState();
        int[][] initBoard = getBoard();
        for (int col = 0 ; col < 4; col++) {
            int[] newCol = new int[board.length];
            int track = 0;
            for (int row = 0; row < 4; row++) {
                if (board[row][col] != 0) {
                    newCol[track] = board[row][col];
                    track++;
                }
            }
            for (int row = 0; row < track - 1; row++) {
                if (newCol[row] == newCol[row + 1]) {
                    newCol[row] = newCol[row + 1] + newCol[row];
                    score += newCol[row];
                    newCol[row + 1] = 0;
                }
            }
            int[] mergedCol = new int[newCol.length];
            int finalTrack = 0;
            for (int row = 0; row < 4; row++) {
                if (newCol[row] != 0) {
                    mergedCol[finalTrack] = newCol[row];
                    finalTrack++;
                }
            }
            for (int row = 0; row < 4; row++) {
                board[row][col] = mergedCol[row]; }

        }
        if (!equalBoards(initBoard,board)) {
            makeTile();
            checkHighScore();
        }
    }


    // Move down function which would process the merging of the tiles
    // when you click the down button
    public void moveDown() {
        saveCurState();
        int[][] initBoard = getBoard();
        for (int col = 3 ; col >= 0; col--) {
            int[] newCol = new int[board.length];
            int track = newCol.length - 1;
            for (int row = 3 ; row >= 0; row--) {
                if (board[row][col] != 0) {
                    newCol[track] = board[row][col];
                    track--;
                }
            }
            for (int row = newCol.length - 1; row > 0 ; row--) {
                if (newCol[row] == newCol[row - 1]) {
                    newCol[row] = newCol[row - 1] + newCol[row];
                    score += newCol[row];
                    newCol[row - 1] = 0;
                }
            }
            int[] mergedCol = new int[newCol.length];
            int finalTrack = newCol.length - 1;
            for (int row = newCol.length - 1 ; row >= 0 ; row--) {
                if (newCol[row] != 0) {
                    mergedCol[finalTrack] = newCol[row];
                    finalTrack--;
                }
            }
            for (int row = 0; row < 4; row++) {
                board[row][col] = mergedCol[row];
            }
        }
        if (!equalBoards(initBoard,board)) {
            makeTile();
            checkHighScore();
        }
    }

    private void checkHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void saveGame(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int[] ints : board) {
                for (int anInt : ints) {
                    writer.write(anInt + " ");

                }
                writer.newLine();
            }
            writer.write(score + "\n");
            writer.write(highScore + "\n");
            if (gameOver) {
                writer.write("1\n");
            } else {
                writer.write("0\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving game- " + e.getMessage());
        }
    }

    public enum LoadStatus {
        SUCCESS,
        FILE_NOT_FOUND,
        INVALID_FORMAT,
        IO_ERROR
    }

    public LoadStatus loadGame(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            for (int row = 0; row < board.length; row++) {
                String line = reader.readLine();
                if (line == null) {
                    return LoadStatus.INVALID_FORMAT;
                }
                String[] values = line.split(" ");
                if (values.length != board[row].length) {
                    return LoadStatus.INVALID_FORMAT;
                }
                for (int col = 0; col < board[row].length; col++) {
                    try {
                        board[row][col] = Integer.parseInt(values[col]);
                    } catch (NumberFormatException e) {
                        return LoadStatus.INVALID_FORMAT;
                    }
                }
            }

            String scoreLine = reader.readLine();
            if (scoreLine == null) {
                return LoadStatus.INVALID_FORMAT;
            }
            try {
                score = Integer.parseInt(scoreLine);
            } catch (NumberFormatException e) {
                return LoadStatus.INVALID_FORMAT;
            }

            String highScoreLine = reader.readLine();
            if (highScoreLine == null) {
                return LoadStatus.INVALID_FORMAT;
            }
            try {
                highScore = Integer.parseInt(highScoreLine);
            } catch (NumberFormatException e) {
                return LoadStatus.INVALID_FORMAT;
            }


            String gameOverLine = reader.readLine();
            if (gameOverLine == null) {
                return LoadStatus.INVALID_FORMAT;
            }
            if (gameOverLine.equals("1")) {
                gameOver = true;
                return LoadStatus.SUCCESS;
            } else if (gameOverLine.equals("0")) {
                gameOver = false;
            } else {
                return LoadStatus.INVALID_FORMAT;
            }
            return LoadStatus.SUCCESS;

        } catch (FileNotFoundException e) {
            System.out.println("Error loading game: " + e.getMessage());
            return LoadStatus.FILE_NOT_FOUND;
        } catch (IOException e) {
            System.out.println("Error in save file format: " + e.getMessage());
            return LoadStatus.IO_ERROR;
        }
    }


    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }

    public boolean checkGameOver() {
        return isGameOver();
    }

    // get board and set board method to make testing easier

    public void setBoard(int[][] newBoard) {
        for (int row = 0; row < 4; row++) {
            board[row] = newBoard[row].clone();
        }
        undoHistory.clear();
        undoRemaining = NUM_UNDOS;
        gameOver = isGameOver();
    }

    public int[][] getBoard() {
        int[][] copy = new int[4][4];
        for (int row = 0; row < 4; row++) {
            copy[row] = board[row].clone();
        }
        return copy;
    }


    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Game2048 t = new Game2048();

        t.printGameState();
        t.moveLeft();
        t.printGameState();
        t.moveUp();
        t.printGameState();
        t.moveRight();
        t.printGameState();
        t.moveDown();
        t.printGameState();

        if (t.isGameOver()) {
            System.out.println("Game over! Score: " + t.score);
        } else {
            System.out.println("Current Score: " + t.score);
        }
    }
}
