// Copyright (c) 2020 [Richwell Perez]. All rights reserved.

#define CATCH_CONFIG_MAIN

#include <tictactoe/tictactoe.h>

#include <catch2/catch.hpp>
#include <string>

using tictactoe::EvaluateBoard;
using tictactoe::TicTacToeState;

TEST_CASE("Greater sized board", "[invalid-input]") {
  REQUIRE(EvaluateBoard("xOWferfvccp") == TicTacToeState::InvalidInput);
}

TEST_CASE("Lesser sized board", "[invalid-input]") {
  REQUIRE(EvaluateBoard("oXerfgh") == TicTacToeState::InvalidInput);
}

TEST_CASE("X additional turn", "[unreachable-state]") {
  REQUIRE(EvaluateBoard("OxXxOxpXO") == TicTacToeState::UnreachableState);
}

TEST_CASE("O additional turn", "[unreachable-state]") {
  REQUIRE(EvaluateBoard("o-XxXoOpO") == TicTacToeState::UnreachableState);
}

TEST_CASE("X additional turn after O wins", "[unreachable-state]") {
  REQUIRE(EvaluateBoard("xXzoOoXqx") == TicTacToeState::UnreachableState);
}

TEST_CASE("O additional turn after X wins", "[unreachable-state]") {
  REQUIRE(EvaluateBoard("xOoXopxyz") == TicTacToeState::UnreachableState);
}

TEST_CASE("Two winners", "[unreachable-state]") {
  REQUIRE(EvaluateBoard("xxXOoOwyz") == TicTacToeState::UnreachableState);
}

TEST_CASE("No winner", "[no-winner]") {
  REQUIRE(EvaluateBoard("xxoooxxxo") == TicTacToeState::NoWinner);
}

TEST_CASE("No winner 2", "[no-winner]") {
  REQUIRE(EvaluateBoard("O-0lX.x.e") == TicTacToeState::NoWinner);
}

TEST_CASE("X wins twice", "[X-wins]") {
  REQUIRE(EvaluateBoard("XoXoxoXOx") == TicTacToeState::Xwins);
}

TEST_CASE("X wins diagonally", "[X-wins]") {
  REQUIRE(EvaluateBoard("OoXoxXXxO") == TicTacToeState::Xwins);
}

TEST_CASE("X wins horizontally", "[X-wins]") {
  REQUIRE(EvaluateBoard("pqOxXxfOr") == TicTacToeState::Xwins);
}

TEST_CASE("X wins vertically", "[X-wins]") {
  REQUIRE(EvaluateBoard("xOOxkgXfr") == TicTacToeState::Xwins);
}

TEST_CASE("O wins diagonally", "[O-wins]") {
  REQUIRE(EvaluateBoard("Ox..Ox.xO") == TicTacToeState::Owins);
}

TEST_CASE("O wins horizontally", "[O-wins]") {
  REQUIRE(EvaluateBoard("XxwOoOpzX") == TicTacToeState::Owins);
}

TEST_CASE("O wins vertically", "[O-wins]") {
  REQUIRE(EvaluateBoard("XxOzxOpqO") == TicTacToeState::Owins);
}
