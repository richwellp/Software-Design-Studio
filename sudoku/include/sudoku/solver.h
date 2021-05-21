// Copyright (c) 2020 [Your Name]. All rights reserved.

#ifndef SUDOKU_SOLVER_H_
#define SUDOKU_SOLVER_H_

#include <string>
namespace sudoku {

// begins the interaction between the user
void Start();

// checks if the file given is valid
bool isValidFile(const std::string& file_name);


}  // namespace sudoku

#endif  // SUDOKU_SOLVER_H_

