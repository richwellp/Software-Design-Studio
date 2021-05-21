// Copyright (c) 2020 CS126SP20. All rights reserved.

#include <algorithm>
#include <cstdlib>
#include <random>
#include <set>
#include <stdexcept>

#include <snake/direction.h>
#include <snake/engine.h>

namespace snake {

// Converts a direction into a delta location.
Location FromDirection(const Direction direction) {
  switch (direction) {
    case Direction::kUp:
      return {-1, 0};
    case Direction::kDown:
      return {+1, 0};
    case Direction::kLeft:
      return {0, -1};
    case Direction::kRight:
      return {0, +1};
  }

  throw std::out_of_range("switch statement not matched");
}

// Determines if the given directions are complementary.
bool IsOpposite(const Direction lhs, const Direction rhs) {
  return ((lhs == Direction::kUp && rhs == Direction::kDown) ||
          (lhs == Direction::kDown && rhs == Direction::kUp) ||
          (lhs == Direction::kLeft && rhs == Direction::kRight) ||
          (lhs == Direction::kRight && rhs == Direction::kLeft));
}

Snake Engine::GetSnake() const { return snake_; }

void Engine::Reset() {
  snake_ = {};
  Location location = GetRandomLocation();
  snake_.AddPart(Segment(location));
}

Engine::Engine(size_t width, size_t height)
    : Engine{width, height, static_cast<unsigned>(std::rand())} {}

Engine::Engine(size_t width, size_t height, unsigned seed)
    : width_{width},
      height_{height},
      food_{GetRandomLocation()},
      direction_{Direction::kRight},
      last_direction_{Direction::kUp},
      rng_{seed},
      uniform_{0, 1} {
  Reset();
}

void Engine::Step() {
  // Snake can't move directly into itself.
  if (snake_.Size() > 1 && IsOpposite(direction_, last_direction_)) {
    direction_ = last_direction_;
  }

  Location d_loc = FromDirection(direction_);
  Location new_head_loc =
      (snake_.Head().GetLocation() + d_loc) % Location(height_, width_);

  const std::set<Location> old_occupied_tiles = GetOccupiedTiles();

  // Did a collision occur?
  for (const Segment& part : snake_) {
    if (part.GetLocation() == new_head_loc && part.IsVisibile()) {
      snake_.ChopUp();
      break;
    }
  }

  Location leader = new_head_loc;
  for (Segment& part : snake_) {
    Location old = part.GetLocation();
    part.SetLocation(leader);
    leader = old;
  }

  last_direction_ = direction_;

  // Was food consumed?
  const std::set<Location> new_occupied_tiles = GetOccupiedTiles();
  if (new_occupied_tiles.find(food_.GetLocation()) !=
      new_occupied_tiles.end()) {
    Segment old_tail = snake_.Tail();
    Segment new_tail = Segment(old_tail.GetLocation() - d_loc);
    snake_.AddPart(new_tail);
    food_ = Food(GetRandomLocation());
  }
}

size_t Engine::GetScore() const {
  return snake_.Size();
}

std::set<Location> Engine::GetOccupiedTiles() {
  std::set<Location> occupied_tiles;

  for (const Segment& part : snake_) {
    occupied_tiles.insert(part.GetLocation());
  }

  return occupied_tiles;
}


// Retrieves a random location not occupied by the snake.
// This method uses Reservoir sampling.
Location Engine::GetRandomLocation() {
  std::set<Location> occupied_tiles = GetOccupiedTiles();

  int num_open = 0;
  Location final_location(0, 0);

  for (size_t row = 0; row < height_; ++row) {
    for (size_t col = 0; col < width_; ++col) {
      Location loc(row, col);
      if (occupied_tiles.find(loc) != occupied_tiles.end()) continue;

      if (uniform_(rng_) <= 1./(++num_open)) {
        final_location = loc;
      }
    }
  }

  return final_location;
}

Food Engine::GetFood() const { return food_; }

void Engine::SetDirection(const snake::Direction direction) {
  direction_ = direction;
}

}  // namespace snake

