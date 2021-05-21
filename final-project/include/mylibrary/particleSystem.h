//
// Created by richw on 5/4/2020.
//

#ifndef FINALPROJECT_PARTICLESYSTEM_H
#define FINALPROJECT_PARTICLESYSTEM_H

#include "cinder/Vector.h"
#include "cinder/gl/gl.h"
#include "particle.h"

namespace simulate {
// Class used for managing a vector of particles
class ParticleSystem {
 public:
  ~ParticleSystem();
  // updates each particles
  void update();
  // draws each particles
  void draw();
  // adds a particle in the particles vector
  void addParticle(Particle *particle);
  // deletes a particle in the particles vector
  void destroyParticle(Particle *particle);
 private:
  std::vector<Particle*> particles;
};
}
#endif  // FINALPROJECT_PARTICLESYSTEM_H
