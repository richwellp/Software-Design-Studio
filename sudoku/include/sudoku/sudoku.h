// Created by richw on 3/24/2020.

#ifndef SUDOKU_SUDOKU_H
#define SUDOKU_SUDOKU_H

#include <iostream>
#include <string>
#include <vector>

class Sudoku {
 public:
  void Initialize();
  // doesn't not solve the puzzle if the puzzle is unsolvable
  void Solve();
  // checks if board is valid
  bool Sudoku::isValidBoard(const std::string& board);
  // adds a puzzle
  void AddPuzzle(const std::string& puzzle) { Puzzles.push_back(puzzle); }
  std::vector<std::string> GetPuzzles() { return Puzzles; }
  friend std::istream& operator>>(std::istream& is, const std::string& puzzle);
  friend std::ostream& operator<<(std::ostream& os, const std::string& puzzle);
  // stores the puzzles in the .spf file
  std::vector<std::string> Puzzles;
};

// initializes the sudoku puzzle
std::istream& operator>> (std::istream& is, const std::string& puzzle);

// prints out the solved/unsolvable puzzle
std::ostream& operator<< (std::ostream& os, const std::string& puzzle);
#endif  // SUDOKU_SUDOKU_H
