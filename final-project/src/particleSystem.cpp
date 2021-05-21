//
// Created by richw on 5/4/2020.
//

#include "mylibrary/particleSystem.h"

namespace simulate {

ParticleSystem::~ParticleSystem() {
  for (auto iter = particles.begin(); iter != particles.end(); ++iter) {
    delete *iter;
  }
  particles.clear();
}

void ParticleSystem::update() {
  for (auto iter = particles.begin(); iter != particles.end(); ++iter) {
    (*iter)->update();
  }
}

void ParticleSystem::draw() {
  for (auto iter = particles.begin(); iter != particles.end(); ++iter) {
    (*iter)->draw();
  }
}

void ParticleSystem::addParticle(Particle* particle) {
  particles.push_back(particle);
}

void ParticleSystem::destroyParticle(Particle* particle) {
  auto iter = std::find(particles.begin(), particles.end(), particle);
  delete *iter;
  particles.erase(iter);
}

}