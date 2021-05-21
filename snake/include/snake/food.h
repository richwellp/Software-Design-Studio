// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef SNAKE_FOOD_H_
#define SNAKE_FOOD_H_

#include "location.h"


namespace snake {

// Represents a food item.
class Food {
 public:
  explicit Food(const Location&);

  // Rule of Five.
  Food(const Food&);
  Food(Food&&) noexcept;
  Food& operator=(const Food&);
  Food& operator=(Food&&) noexcept;
  ~Food();

  Location GetLocation() const;

 private:
  Location location_;
};

}  // namespace snake

#endif  // SNAKE_FOOD_H_
