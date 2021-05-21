// Copyright 2020 [Richwell Perez]. All rights reserved.

#include <tictactoe/tictactoe.h>

#include <string>

namespace tictactoe {

using std::string;

static const int top_left_index = 0;
static const int top_right_index = 2;
static const int center_index = 4;
static const int bottom_left_index = 6;
static const int bottom_right_index = 8;
static const int dimension = 3;
static const int col_increment = 3;

/**
 * Counts the number of turn occurrences the player has made
 * @param board the case insensitive 3 x 3 tic tac toe board
 * @param player represents X or O player, will be assumed as lower case
 * @return the number of characters the player had marked in the board
 */
int CountTurns(const string& board, const char& player) {
  int counts = 0;
  for (char const& marks : board) {
    if (marks == player || marks == _toupper(player)) {
      counts++;
    }
  }
  return counts;
}

/**
 * Evaluates if the player made a 3 in a row
 * @param board the case insensitive 3 x 3 tic tac toe board
 * @param player represents X or O player, will be assumed as lower case
 * @return true if player made a 3 in a row in the board
 */
bool CheckWinner(const string& board, const char& player) {
  // check vertically for 3 in a row
  for (int index = 0; index < dimension; index++) {
    // indexes move by 1 starting from 0 to 3 and 6
    if (board.at(index) == player || board.at(index) == _toupper(player)) {
      int bottom_index = index + col_increment;
      int second_bottom_index = bottom_index + col_increment;
      if ((player == board.at(bottom_index) ||
           _toupper(player) == board.at(bottom_index)) &&
          (player == board.at(second_bottom_index) ||
           _toupper(player) == board.at(second_bottom_index))) {
        return true;
      }
    }
  }
  // check horizontally
  for (int index = 0; index < board.length(); index = index + col_increment) {
    // indexes move by 3 starting from 0 to 1 and 2
    if (board.at(index) == player || board.at(index) == _toupper(player)) {
      int right_index = index + 1;
      int second_right_index = right_index + 1;
      if ((player == board.at(right_index) ||
           _toupper(player) == board.at(right_index)) &&
          (player == board.at(second_right_index) ||
           _toupper(player) == board.at(second_right_index))) {
        return true;
      }
    }
  }
  // check diagonally
  if (board.at(center_index) == player ||
      board.at(center_index) == _toupper(player)) {
    // from top left to bottom right diagonal or from top right to bottom left
    if (((player == board.at(top_left_index) ||
          _toupper(player) == board.at(top_left_index)) &&
         (player == board.at(bottom_right_index) ||
          _toupper(player) == board.at(bottom_right_index))) ||
        ((player == board.at(top_right_index) ||
          _toupper(player) == board.at(top_right_index)) &&
         (player == board.at(bottom_left_index) ||
          _toupper(player) == board.at(bottom_left_index)))) {
      return true;
    }
  }
  return false;
}

/**
 * Evaluates the tic tac toe board
 * @param board the 3 x 3 tic tac toe board
 * @return a TicTacToeState thatw best describes the board
 */
TicTacToeState EvaluateBoard(const string& board) {
  // check for a valid board
  if (board.length() != dimension * dimension) {
    return TicTacToeState::InvalidInput;
  }
  // number of Xs must be equal or 1 greater to the number of Os
  int count_X = CountTurns(board, 'x');
  int count_O = CountTurns(board, 'o');
  if (count_O != count_X && count_X - count_O != 1) {
    return TicTacToeState ::UnreachableState;
  }
  bool X_wins = CheckWinner(board, 'x');
  bool O_wins = CheckWinner(board, 'o');
  if (X_wins && O_wins) {
    // there cannot be two winners
    return TicTacToeState::UnreachableState;
  } else if (X_wins) {
    // X is the first player it must be one turn ahead of O before winning
    if (count_X - count_O == 1) {
      return TicTacToeState::Xwins;
    } else {
      return TicTacToeState::UnreachableState;
    }
  } else if (O_wins) {
    if (count_O == count_X) {
      return TicTacToeState::Owins;
    } else {
      return TicTacToeState::UnreachableState;
    }
  }
  // no winner was evaluated
  return TicTacToeState::NoWinner;
}

}  // namespace tictactoe
