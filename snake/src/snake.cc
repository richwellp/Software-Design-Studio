// Copyright (c) 2020 CS126SP20. All rights reserved.

#include <algorithm>
#include <snake/snake.h>


namespace snake {

Snake::Snake() : body_{}, mod_{2}, is_chopped_{false} {}

void Snake::AddPart(const snake::Segment& part) { body_.push_back(part); }

size_t Snake::Size() const {
  return body_.size();
}

std::deque<Segment>::const_iterator Snake::cbegin() const {
  return body_.cbegin();
}

std::deque<Segment>::const_iterator Snake::cend() const { return body_.cend(); }

std::deque<Segment>::iterator Snake::begin() { return body_.begin(); }

std::deque<Segment>::iterator Snake::end() { return body_.end(); }

Segment Snake::Head() const { return body_.front(); }

Segment Snake::Tail() const { return body_.back(); }

bool Snake::IsChopped() const { return is_chopped_; }

void Snake::ChopUp() {
  int rem = 0;
  for (Segment& part : body_) {
    part.SetVisibility(rem == 0);
    rem = (rem + 1) % mod_;
  }

  ++mod_;
  is_chopped_ = true;
}

}  // namespace snake
