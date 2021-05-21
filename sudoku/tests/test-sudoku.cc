// Copyright (c) 2020 [Your Name]. All rights reserved.

#define CATCH_CONFIG_MAIN

#include <sudoku/solver.h>
#include <sudoku/sudoku.h>

#include <catch2/catch.hpp>

TEST_CASE("Puzzle is added", "parsed") {
  Sudoku sudoku_puzzle;
  std::vector<std::string> puzzles = sudoku_puzzle.GetPuzzles();
  REQUIRE(sudoku::isValidFile("tests/data/invalid.spf") == true);
  REQUIRE(puzzles.at(0) == "85___24__72______9__4_________1_7__23_5___9___4___________8__7__17__________36_4_");
  REQUIRE(puzzles.at(1) == "___8_5____3__6___7_9___38___4795_3______71_9____2__5__1____248___9____5______6___");
}


TEST_CASE("Empty puzzle", "parsed") {
  REQUIRE(sudoku::isValidFile("tests/data/empty.spf") == true);
}

TEST_CASE("Simple puzzle", "parsed") {
  REQUIRE(sudoku::isValidFile("tests/data/simple.spf") == true);
}

TEST_CASE("Non .spf file", "invalid") {
  REQUIRE(sudoku::isValidFile("tests/data/invalid.spf") == false);
}

TEST_CASE("Invalid length", "invalid") {
  REQUIRE(sudoku::isValidFile("tests/data/invalid2.spf") == false);
}

TEST_CASE("Repeated number horizontal", "rule-violation") {
  Sudoku s;
  REQUIRE(s.isValidBoard("88___24__72______9__4_________1_7__23_5___9___4______"
                         "_____8__7__17__________36_4_") == false);
}

TEST_CASE("Repeated number vertical", "rule-violation") {
  Sudoku s;
  // 8 is repeated at index 0 and 9
  REQUIRE(s.isValidBoard("85___24__82______9__4_________1_7__23_5___9___4______"
                         "_____8__7__17__________36_4_") == false);
}

TEST_CASE("Repeated number local 3x3", "rule-violation") {
  Sudoku s;
  // 8 is repeated on the first local 3x3
  REQUIRE(s.isValidBoard("85___24__728_____9__4_________1_7__23_5___9___4______"
                         "_____8__7__17__________36_4_") == false);
}
