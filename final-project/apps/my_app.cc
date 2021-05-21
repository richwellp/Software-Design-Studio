// Copyright (c) 2020 [Your Name]. All rights reserved.

#include "my_app.h"

#include <cinder/app/App.h>


namespace myapp {

const int kWidth = 800;
const int kHeight = 800;
const float32 kTimeStep = 1.0f / 60.0f;
const int32 kVelocityIterations = 6;
const int32 kPositionIterations = 2;
b2Vec2 gravity(0.0f, 9.8f);
b2World world(gravity);

MyApp::MyApp() = default;

void MyApp::setup() {
  cinder::gl::enableDepthWrite();
  cinder::gl::enableDepthRead();

  // Ground
  b2BodyDef groundBodyDef;
  groundBodyDef.position.Set( 0.0f, 10.0f);
  groundBody = world.CreateBody(&groundBodyDef);
  b2PolygonShape groundBox;
  groundBox.SetAsBox((float)kWidth / 2, (float)kHeight / 2);
  groundBody->CreateFixture(&groundBox, 0.0f);

  // TODO:: Generate particles
  int numParticle = 10;
  for (int i = 0; i < numParticle; i++) {
    float x = ci::Rand::randFloat(0.0f, (float)getWindowWidth());
    float y = ci::Rand::randFloat(0.0f, (float)getWindowHeight());
    float radius = ci::Rand::randFloat(10.0f, 15.0f);
    float mass = radius;
    float force_x = ci::Rand::randFloat(-10000.0f, 10000.0f);
    float force_y = 0.0f;
    float elasticity = ci::Rand::randFloat(0.3f, 1.0f);
    auto* particle = new simulate::Particle(ci::vec2(x, y), ci::vec2(force_x, force_y), radius, mass, elasticity, world);
    particleSystem.addParticle(particle);
  }
}

void MyApp::update() {
  particleSystem.update();
  // simulating the world
  world.Step(kTimeStep, kVelocityIterations, kPositionIterations);
}

void MyApp::draw() {
  DrawBackground();
  particleSystem.draw();
}

void MyApp::DrawBackground() const {
  ci::gl::clear(ci::Color(0, 0, 0));
}

}  // namespace myapp
