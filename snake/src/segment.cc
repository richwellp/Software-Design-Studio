// Copyright (c) 2020 CS126SP20. All rights reserved.

#include <snake/location.h>
#include <snake/segment.h>

namespace snake {

Segment::Segment(const Location& location)
    : location_(location), visible_{true} {}

Location Segment::GetLocation() const { return location_; }

Location Segment::SetLocation(const snake::Location& location) {
  location_ = location;
  return location_;
}

void Segment::SetVisibility(bool visible) { visible_ = visible; }

bool Segment::IsVisibile() const { return visible_; }

}  // namespace snake
