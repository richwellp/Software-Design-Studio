// Copyright (c) 2020 CS126SP20. All rights reserved.

#define CATCH_CONFIG_MAIN

#include <vector>

#include <snake/engine.h>
#include <catch2/catch.hpp>

using snake::Direction;
using snake::Engine;
using snake::Location;

const unsigned kSeed = 2020;

// These are generally not sufficient tests.
// You do NOT need to add any tests for this assignment.
TEST_CASE("Location vector operations", "[location]") {
  SECTION("Modulo positive") {
    Location loc1{12, 9};
    Location loc2{7, 3};

    Location result = loc1 % loc2;
    REQUIRE(result == Location{5, 0});
  }

  SECTION("Modulo positive") {
    Location loc1{-1, 3};
    Location loc2{7, 2};

    Location result = loc1 % loc2;
    REQUIRE(result == Location{6, 1});
  }
}

TEST_CASE("Scoring Function", "[score]") {
  Engine engine{5, 5, kSeed};

  SECTION("Scoring") {
    REQUIRE(engine.GetScore() == 1);

    // Test eating food.
    const Location food_loc = engine.GetFood().GetLocation();
    const Location snake_head = engine.GetSnake().Head().GetLocation();

    const Location d_loc = snake_head - food_loc;
    const Direction vertical =
        d_loc.Row() > 0 ? Direction::kUp : Direction::kDown;
    const Direction horizontal =
        d_loc.Col() > 0 ? Direction::kLeft : Direction::kRight;

    std::vector<Direction> steps;
    for (int i = 0; i < std::abs(d_loc.Row()); ++i) {
      steps.push_back(vertical);
    }
    for (int i = 0; i < std::abs(d_loc.Col()); ++i) {
      steps.push_back(horizontal);
    }

    for (Direction direction : steps) {
      REQUIRE(engine.GetScore() == 1);

      engine.SetDirection(direction);
      engine.Step();
    }

    REQUIRE(engine.GetScore() == 2);
  }
}
