// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef FINALPROJECT_APPS_MYAPP_H_
#define FINALPROJECT_APPS_MYAPP_H_

#include <cinder/app/App.h>
#include <cinder/gl/gl.h>
#include <mylibrary/particleSystem.h>
#include "cinder/Rand.h"

namespace myapp {

class MyApp : public cinder::app::App {
 public:
  MyApp();
  void setup() override;
  void update() override;
  void draw() override;
 private:
  void DrawBackground() const;
 private:
  simulate::ParticleSystem particleSystem;
  b2Body* groundBody{};
};

}  // namespace myapp

#endif  // FINALPROJECT_APPS_MYAPP_H_
