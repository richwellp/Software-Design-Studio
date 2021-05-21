// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef SNAKE_PLAYER_H_
#define SNAKE_PLAYER_H_

#include <string>

namespace snake {

struct Player {
  Player(const std::string& name, size_t score) : name(name), score(score) {}
  std::string name;
  size_t score;
};

}  // namespace snake

#endif  // SNAKE_PLAYER_H_
