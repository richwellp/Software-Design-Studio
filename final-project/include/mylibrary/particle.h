//
// Created by richw on 4/24/2020.
//

#ifndef FINALPROJECT_PARTICLE_H
#define FINALPROJECT_PARTICLE_H

#include "cinder/Color.h"
#include <Box2D/Box2D.h>

namespace simulate {
class Particle {
 public:
  // A particle constructor to be added in the b2World reference
  Particle(const ci::vec2& position, const ci::vec2& force, float radius,
           float mass, float recoil, b2World& w);
  // updates the particle events
  void update();
  // draws the particle as a circle
  void draw();

 private:
  ci::vec2 position;
  ci::vec2 prevPosition;
  ci::vec2 force;
  float radius;
  float mass;
  b2Body* body;  // body from the physics world
  b2CircleShape circle;
  b2World* world;  // pointer from the physics world
};
}
#endif  // FINALPROJECT_PARTICLE_H
