// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef SNAKE_PART_H_
#define SNAKE_PART_H_

#include "location.h"

namespace snake {

// Represents a segment of the snake.
class Segment {
 public:
  explicit Segment(const Location& location);
  Location GetLocation() const;
  Location SetLocation(const Location&);
  void SetVisibility(bool visible);
  bool IsVisibile() const;

 private:
  Location location_;
  bool visible_;
};

}  // namespace snake

#endif  // SNAKE_PART_H_
