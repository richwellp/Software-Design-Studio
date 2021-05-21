// Copyright (c) 2020 CS126SP20. All rights reserved.

#include <snake/food.h>

namespace snake {

Food::Food(const Location& location) : location_(location) {}

Location Food::GetLocation() const { return location_; }

Food::Food(const Food& food) = default;
Food::Food(snake::Food&& food) noexcept = default;
Food& Food::operator=(const Food& food) = default;
Food& Food::operator=(snake::Food&& food) noexcept = default;
Food::~Food() = default;

}  // namespace snake
