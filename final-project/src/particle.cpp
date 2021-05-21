//
// Created by richw on 4/24/2020.
//

#include <cinder/Rand.h>
#include <cinder/gl/gl.h>
#include <mylibrary/particle.h>

namespace simulate {

Particle::Particle(const ci::vec2& position, const ci::vec2& force, float radius, float mass, float recoil, b2World& w) {
  this->position = position;
  this->force = force;
  this->radius = radius;
  this->mass = mass;
  world = &w;
  prevPosition = position;
  // Initialize dynamic body
  b2BodyDef bodyDef;
  bodyDef.type = b2_dynamicBody;
  bodyDef.position.Set(position.x, position.y);
  body = w.CreateBody(&bodyDef);
  circle.m_p.Set(position.x, position.y);
  circle.m_radius = radius;
  // Fixture using circle
  b2FixtureDef fixtureDef;
  fixtureDef.shape = &circle;
  fixtureDef.density = 1.0f;
  fixtureDef.friction = 0.3f;
  fixtureDef.restitution = recoil; // elastic
  body->CreateFixture(&fixtureDef);
  body->ApplyForceToCenter(b2Vec2(force.x, force.y));
  body->SetLinearVelocity(b2Vec2(force.x, force.y));
  body->SetAwake(true);
}

void Particle::update() {
  /** verlet algorithm
  ci::vec2 temp = position;
  ci::vec2 vel = (position - prevPosition) * 9.5f;
  position += vel + force / mass;
  prevPosition = temp;
  force.x = 0;
  force.y = 0;
  */
  // position = ci::vec2(circle.m_p.x, circle.m_p.y);
  b2Vec2 velocity = body->GetLinearVelocity();
  body->SetLinearVelocity(b2Vec2(velocity.x, velocity.y));
}

void Particle::draw() {
  b2Vec2 pos = body->GetPosition();
  ci::gl::drawSolidCircle(ci::vec2(pos.x, pos.y), radius);
}
}