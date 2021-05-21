// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef SNAKE_SNAKE_H_
#define SNAKE_SNAKE_H_

#include <deque>

#include "segment.h"


namespace snake {

class Snake {
 public:
  Snake();

  // Adds a new part to the snake.
  void AddPart(const Segment&);

  // Returns the size of the snake.
  size_t Size() const;

  // Makes some segments invisible.
  // Formally, n * (1-1/c) segments are removed after c collisions.
  void ChopUp();
  bool IsChopped() const;

  Segment Tail() const;
  Segment Head() const;

  std::deque<Segment>::iterator begin();
  std::deque<Segment>::iterator end();
  std::deque<Segment>::const_iterator cbegin() const;
  std::deque<Segment>::const_iterator cend() const;

 private:
  std::deque<Segment> body_;
  int mod_;
  bool is_chopped_;
};

}  // namespace snake

#endif  // SNAKE_SNAKE_H_
