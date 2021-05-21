package student.tictactoe;

import student.tictactoe.Evaluation;

public class TicTacToe {

    /**
     * Checks if the Tic-Tac-Toe board is played according to the rules and evaluates for a winner.
     * @param boardState    the current Tic-Tac-Toe board
     * @return an Evaluation that best describes the board
     */
    public static Evaluation evaluateBoard(String boardState) {
        // board must have 9 characters
        if (boardState == null || boardState.length() != 9) {
            return Evaluation.InvalidInput;
        }
        // number of Xs must be equal or 1 greater to the number of Os
        int countXs = countTurns(boardState, 'X');
        int countOs = countTurns(boardState, 'O');
        if (countOs != countXs && countXs - countOs != 1) {
            return Evaluation.UnreachableState;
        }
        // evaluate for a winner
        boolean XWins = checkWinner(boardState, 'X');
        boolean OWins = checkWinner(boardState, 'O');
        // the game can only have 1 winner
        if (XWins && OWins) {
            return Evaluation.UnreachableState;
        } else if (XWins) {
            // the number of O turns should be 1 less than X turns
            if (countXs - countOs == 1) {
                return Evaluation.Xwins;
            } else {
                return Evaluation.UnreachableState;
            }
        } else if (OWins) {
            // the number of O turns should be equal to X turns
            if (countOs == countXs) {
                return Evaluation.Owins;
            } else {
                return Evaluation.UnreachableState;
            }
        }
        // no winner was evaluated
        return Evaluation.NoWinner;
    }

    /**
     * Function for counting the turns taken by the players.
     * @param board     the Tic-Tac-Toe board
     * @param player    the player whose turns are counted
     * @return the number of turns a player has made.
     */
    static int countTurns(String board, char player) {
        // board is case-insensitve
        board = board.toLowerCase();
        player = Character.toLowerCase(player);
        int turnCount = 0;
        for (int index = 0; index < board.length(); index++) {
            if (board.charAt(index) == player) {
                turnCount++;
            }
        }
        return turnCount;
    }

    /**
     * Function for evaluating a winner horizontally, vertically, and diagonally.
     * @param board     the Tic-Tac-Toe board
     * @param player    the player whose being checked as a winner
     * @return whether the player has placed 3 marks in a row.
     */
    static boolean checkWinner(String board, char player) {
        // board is case-insensitve
        board = board.toLowerCase();
        player = Character.toLowerCase(player);
        // check vertically: indexes move by 1 starting from 0, 3, 6
        for (int index = 0; index < 3; index++) {
            if (board.charAt(index) != player) {
                continue;
            }
            if (board.charAt(index) == board.charAt(index + 3)
                    && board.charAt(index) == board.charAt(index + 6)) {
                return true;
            }
        }
        // check horizontally: indexes move by 3 starting from 0, 1, 2
        for (int index = 0; index < 9; index = index + 3) {
            if (board.charAt(index) != player) {
                continue;
            }
            if (board.charAt(index) == board.charAt(index + 1)
                    && board.charAt(index) == board.charAt(index + 2)) {
                return true;
            }
        }
        // check diagonally
        if (board.charAt(4) == player) {
            // from top-left diagonal indexes: 0, 4, 8
            // from top-right diagonal indexes: 2, 4, 6
            if ((board.charAt(0) == player && board.charAt(8) == player)
                    || (board.charAt(2) == player && board.charAt(6) == player)) {
                return true;
            }
        }
        return false;
    }
}
