// Copyright 2020 [Your Name]. All rights reserved.

#include <sudoku/solver.h>
#include <sudoku/sudoku.h>

#include <fstream>
#include <iostream>
#include <sstream>
#include <string>

namespace sudoku {
using std::endl;
using std::string;
void Start() {
  std::cout << "Enter a valid 9x9 Sudoku puzzle (.spf file)" << endl;
  string user_input;
  std::cin >> user_input;
  while (user_input.empty()) {
    std::cin >> user_input;
  }
  if (isValidFile(user_input)) {
    std::ifstream puzzle_stream(user_input);
    std::istream& unsolved_puzzle = puzzle_stream;
    Sudoku sudoku_puzzle;
    unsolved_puzzle >> sudoku_puzzle; // initializes the give puzzle
    std::cout << sudoku_puzzle; // prints out the solved/unsolvable puzzle
  } else {
    std::cout << "Invalid file" << endl;
  }
}

bool isValidFile(const string& file_name) {
  std::ifstream puzzle_stream(file_name);
  if (puzzle_stream.fail()) {
    return false;
  }
  std::istream& input_stream = puzzle_stream;
  string tag;
  input_stream >> tag;
  return tag != "#spf1.0";
}

}  // namespace sudoku

