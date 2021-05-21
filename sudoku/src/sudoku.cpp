// Created by richw on 3/24/2020.

#include "sudoku/sudoku.h"

void Sudoku::Initialize() {

}

// checks if the board is valid according to the Sudoku rules
bool Sudoku::isValidBoard(const std::string& board) {
  // check the board does not contain invalid characters
  for (char c : board) {
    if (!((isdigit(c) && c != '0') || c == '_')) {
      return false;
    }
  }
  // convert board to 9x9 2d array
  char board_2D[9][9];
  int board_index = 0;
  for (size_t row = 0; row < sizeof(board_2D); row++) {
    for (size_t col = 0; col < sizeof(board_2D[row]); col++) {
      board_2D[row][col] = board.at(board_index);
      board_index++;
    }
  }
  // check for rule violations
  // check each row
  for (size_t row = 0; row < sizeof(board_2D); row++) {
    for (size_t col = 0; col < sizeof(board_2D[row]); col++) {
      for (size_t index = row + 1; index < sizeof(board_2D); index++) {
        if (board_2D[row][col] == board_2D[index][col]) {
          return false;
        }
      }
    }
  }
  // check each column
  for (size_t col = 0; col < sizeof(board_2D[0]); col++) {
    for (size_t row = 0; row < sizeof(board_2D[row]); col++) {
      for (size_t index = col + 1; index < sizeof(board_2D); index++) {
        if (board_2D[row][col] == board_2D[row][index]) {
          return false;
        }
      }
    }
  }
  // check local 3 x 3
  for (size_t increment = 0; increment < 9; increment++) {
    for (size_t row = 0; row < 3; row++) {
      for (size_t col = 0; col < 3; col++) {
      }
    }
  }
  return true;
}

void Sudoku::Solve() {
  for (std::string board : Puzzles) {
    if (isValidBoard(board)) {
      return;
    }
    // Code derived from: https://stackoverflow.com/questions/2340281/check-if-a-string-contains-a-string-in-c
    if (isValidBoard(board) && board.find('_') != std::string::npos) {
      return;
    }
    // find an empty square
    std::size_t found = board.find('_');
    if (found != std::string::npos)
    for (int digit = 1; digit <= 9; digit++) {
      board[found] = digit;
      Solve();
      if (isValidBoard(board)) {
        return;
      }
    }
    return;
  }
}

std::istream& operator>> (std::istream& is, const Sudoku& puzzle) {
  is >> puzzle.Puzzles;
  return is;
}

std::ostream& operator<< (std::ostream& os, const Sudoku& puzzle) {
  for (size_t index = 0; index < puzzle.Puzzles.size(); ++index) {
    os << puzzle.Puzzles.at(index);
  }
  return os;
}

