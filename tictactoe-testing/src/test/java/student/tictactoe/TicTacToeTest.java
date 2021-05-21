package student.tictactoe;

import student.tictactoe.TicTacToe;
import student.tictactoe.Evaluation;

import org.junit.Test;
import static org.junit.Assert.*;


public class TicTacToeTest {
    @Test
    public void simpleNoWinnerBoard() throws Exception {
        // Test for invalid size
        assertEquals("A null board is invalid.",
                Evaluation.InvalidInput, TicTacToe.evaluateBoard(null));
        assertEquals("A board smaller than 3x3 is invalid.",
                Evaluation.InvalidInput, TicTacToe.evaluateBoard("OOO"));
        assertEquals("A board bigger than 3x3 is invalid.",
                Evaluation.InvalidInput, TicTacToe.evaluateBoard("O...X.X..X"));

        // Test countTurns helper function
        assertEquals("There should be a total of 4 X's or x's in this board.",
                4, TicTacToe.countTurns("o-XxxoOXz",'x'));
        assertEquals("There should be a total of 3 O's or o's in this board.",
                3, TicTacToe.countTurns("o-XxxoO.z",'o'));

        // Test for rule violations
        assertEquals("O had made an additional turn.",
                Evaluation.UnreachableState, TicTacToe.evaluateBoard("o-XxxoOpO"));
        assertEquals("X had made an additional turn.",
                Evaluation.UnreachableState, TicTacToe.evaluateBoard("OxXxOxlxO"));
        assertEquals("There cannot be two winners in the game.",
                Evaluation.UnreachableState, TicTacToe.evaluateBoard("xXxOoOpz/"));
        assertEquals("O should not be able to make a turn once that X wins.",
                Evaluation.UnreachableState, TicTacToe.evaluateBoard("xOoXopxyz"));
        assertEquals("X should not be able to make a turn once that O wins.",
                Evaluation.UnreachableState, TicTacToe.evaluateBoard("xXzoOoXqx"));

        // Test for checkWinner helper function
        assertTrue("X made a 3-in-a-row diagonally",
                TicTacToe.checkWinner("X.,XOOxfe", 'x'));
        assertFalse("There should not be any winner in this board",
                TicTacToe.checkWinner("Xx,XOOofe", 'O'));
        assertTrue("X made a 3-in-a-row horizontally",
                TicTacToe.checkWinner("xOupXorzx", 'X'));
        assertTrue("O made a 3-in-a-row vertically",
                TicTacToe.checkWinner("XxOzOxOpq", 'o'));

        // Test evaluateBoard
        assertEquals("No 3-in-a-row has been made in this board.",
                Evaluation.NoWinner, TicTacToe.evaluateBoard("O...X.X.."));
        assertEquals("No 3-in-a-row has been made in this board.",
                Evaluation.NoWinner, TicTacToe.evaluateBoard("aaabbbccc"));
        assertEquals("X wins by 3-in-a-row diagonally",
                Evaluation.Xwins, TicTacToe.evaluateBoard("OoXoxXXOx"));
        assertEquals("O wins by 3-in-a-row horizontally",
                Evaluation.Owins, TicTacToe.evaluateBoard("XxwOoOpzX"));
        assertEquals("X wins by 3-in-a-row horizontally.",
                Evaluation.Xwins, TicTacToe.evaluateBoard("pqOxXxfOr"));
    }
}
