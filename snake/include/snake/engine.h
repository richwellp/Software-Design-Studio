// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef SNAKE_ENGINE_H_
#define SNAKE_ENGINE_H_

#include <random>
#include <set>

#include "direction.h"
#include "food.h"
#include "snake.h"


namespace snake {

// This is the game engine which is primary way to interact with the game.
class Engine {
 public:
  // Creates a new snake game of the given size.
  Engine(size_t width, size_t height);

  // Creates a new snake game of the given size, seeded.
  Engine(size_t width, size_t height, unsigned seed);

  // Executes a time step: moves the snake, etc.
  void Step();

  // Start the game over.
  void Reset();

  // Changes the direction of the snake for the next time step.
  void SetDirection(Direction);

  size_t GetScore() const;
  Snake GetSnake() const;
  Food GetFood() const;

 private:
  Location GetRandomLocation();
  std::set<Location> GetOccupiedTiles();

 private:
  const size_t width_;
  const size_t height_;
  Snake snake_;
  Food food_;
  Direction direction_;
  Direction last_direction_;
  std::mt19937 rng_;
  std::uniform_real_distribution<double> uniform_;
};

}  // namespace snake

#endif  // SNAKE_ENGINE_H_
