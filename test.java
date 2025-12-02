package org.cis1200.game2048;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Below are some example tests for the TicTacToe game.
 */

public class Game2048Test {
    Game2048 game;

    @BeforeEach
    public void setUp() {
        game = new Game2048();
    }
    @Test
    public void testLeftMerge() {
        Game2048 game = new Game2048();

        int[][] initialBoard = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);


        game.moveLeft();

        // Checking the  merging logic but ignoring the random tile
        int[][] actualBoard = game.getBoard();
        assertEquals(4, actualBoard[0][0], "The first tile in the first row " +
                "should be 4 after merge.");


        int nonEmptyCount = countNonEmptyTiles(actualBoard);
        assertEquals(2, nonEmptyCount, "After merging," +
                " there should be exactly 2 non-empty tiles.");
    }

    @Test
    public void testRightMerge() {
        Game2048 game = new Game2048();

        int[][] initialBoard = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);


        game.moveRight();

        // Checking the  merging logic but ignoring the random tile
        int[][] actualBoard = game.getBoard();
        assertEquals(4, actualBoard[0][3], "The fourth tile in the first row " +
                "should be 4 after merge.");


        int nonEmptyCount = countNonEmptyTiles(actualBoard);
        assertEquals(2, nonEmptyCount, "After merging," +
                " there should be exactly 2 non-empty tiles.");
    }

    @Test
    public void testUpMerge() {
        Game2048 game = new Game2048();

        int[][] initialBoard = {
                {2, 0, 0, 0},
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);


        game.moveUp();

        // Checking the  merging logic but ignoring the random tile
        int[][] actualBoard = game.getBoard();
        assertEquals(4, actualBoard[0][0], "The first tile in the first row " +
                "should be 4 after merge.");


        int nonEmptyCount = countNonEmptyTiles(actualBoard);
        assertEquals(2, nonEmptyCount, "After merging," +
                " there should be exactly 2 non-empty tiles.");
    }

    @Test
    public void testDownMerge() {
        Game2048 game = new Game2048();

        int[][] initialBoard = {
                {2, 0, 0, 0},
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);


        game.moveDown();

        // Checking the  merging logic but ignoring the random tile
        int[][] actualBoard = game.getBoard();
        assertEquals(4, actualBoard[3][0], "The first tile in the fourth row " +
                "should be 4 after merge.");


        int nonEmptyCount = countNonEmptyTiles(actualBoard);
        assertEquals(2, nonEmptyCount, "After merging," +
                " there should be exactly 2 non-empty tiles.");
    }

    @Test
    public void moveDontSpawn() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        game.setBoard(initialBoard);
        game.moveLeft();
        int[][] actualBoard = game.getBoard();
        assertArrayEquals(initialBoard, actualBoard);
    }

    @Test
    public void multipleMerge() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 2, 4, 4},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);
        game.moveLeft();
        int[][] newBoard = game.getBoard();

        assertEquals(4, newBoard[0][0], "Left corner should be 4 after merge.") ;
        assertEquals(8, newBoard[0][1], "Third col, first row" +
                " should be 8 after merge.") ;
    }

    @Test
    public void multipleMergeRight() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 2, 4, 4},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);
        game.moveRight();
        int[][] newBoard = game.getBoard();

        assertEquals(8, newBoard[0][3], "Right corner should be 8 after merge.") ;
        assertEquals(4, newBoard[0][2], "Third col, first row" +
                " should be 4 after merge.") ;
    }
    @Test
    public void multipleMergeUp() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 0, 0, 0},
                {2, 0, 0, 0},
                {4, 0, 0, 0},
                {4, 0, 0, 0}
        };
        game.setBoard(initialBoard);
        game.moveUp();
        int[][] newBoard = game.getBoard();

        assertEquals(4, newBoard[0][0], "Left corner should be 4 after merge.") ;
        assertEquals(8, newBoard[1][0], "First col, Second row" +
                " should be 8 after merge.") ;
    }

    @Test

    public void testDoNotMerge() {
        Game2048 game = new Game2048();

        int[][] initialBoard = {
                {2, 0, 0, 0},
                {4, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        game.setBoard(initialBoard);
        game.moveUp();

        int[][] actualBoard = game.getBoard();
        assertEquals(2, actualBoard[0][0]);
        assertEquals(4, actualBoard[1][0]);

        int nonEmptyCount = countNonEmptyTiles(actualBoard);
        assertEquals(2,nonEmptyCount); // should not create extra tile
    }

    @Test
    public void testGameOver() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 4, 8, 2},
                {4, 8, 2, 4},
                {2, 4, 8, 2},
                {4, 8, 2, 4}
        };
        game.setBoard(initialBoard);
        assertTrue(game.checkGameOver());
    }

    @Test
    public void testUndo() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        game.setBoard(initialBoard);
        game.moveLeft();

        int[][] actualBoard = game.getBoard();
        assertEquals(4, actualBoard[0][0]);

        game.undo();
        int[][] boardWithUndo = game.getBoard();
        assertArrayEquals(boardWithUndo, initialBoard);
    }

    @Test
    public void testUndoWithoutPrevMove() {
        Game2048 game = new Game2048();
        game.undo();

        int[][] boardWithUndo = game.getBoard();
        int nonEmptyCount = countNonEmptyTiles(boardWithUndo);
        assertEquals(2,nonEmptyCount);
        assertFalse(game.checkGameOver());
        assertEquals(2, game.getRemainingUndos());
    }

    @Test
    public void testUndoWithZeroRem() {
        Game2048 game = new Game2048();
        int [][] initialBoard = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);
        game.moveLeft();
        game.undo();
        game.moveLeft();
        game.undo();

        assertEquals(0,game.getRemainingUndos());
        game.undo();

        int[][] boardNow = game.getBoard();

        assertArrayEquals(boardNow, initialBoard);
        assertEquals(0,game.getRemainingUndos());
    }

    @Test
    public void testHighScoreUpdate() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        game.setBoard(initialBoard);
        game.moveLeft();
        assertEquals(4,game.getScore());
        assertEquals(4,game.getHighScore());
    }
    @Test
    public void movesAvailable() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {8, 2, 8, 4},
                {4, 8, 4, 2}
        };
        game.setBoard(initialBoard);
        assertFalse(game.checkGameOver());
    }

    @Test
    public void testReset() {
        Game2048 game = new Game2048();
        int[][] initialBoard = {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {8, 2, 8, 4},
                {4, 8, 4, 2}
        };
        game.setBoard(initialBoard);
        game.reset();
        int[][] resetBoard = game.getBoard();
        assertEquals(0, game.getScore());
        assertEquals(2, countNonEmptyTiles(resetBoard));

    }

    private int countNonEmptyTiles(int[][] board) {
        int count = 0;
        for (int[] row : board) {
            for (int tile : row) {
                if (tile != 0) {
                    count++;
                }
            }
        }
        return count;
    }


}
