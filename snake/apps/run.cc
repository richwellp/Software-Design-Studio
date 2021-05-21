// Copyright (c) 2020 CS126SP20. All rights reserved.

#include <cstdlib>
#include <ctime>
#include <string>
#include <vector>

#include <cinder/app/App.h>
#include <cinder/app/RendererGl.h>
#include <gflags/gflags.h>

#include "snake_app.h"

using cinder::app::App;
using cinder::app::RendererGl;
using std::string;
using std::vector;

namespace snakeapp {

// For some reason, it is not a trivial task to manually call the constructor of
// a Cinder App on initialization. So the flags are defined here and declared in
// `snake_app.cc`.
DEFINE_uint32(size, 16, "the number of tiles in each row and column");
DEFINE_uint32(tilesize, 50, "the size of each tile");
DEFINE_uint32(speed, 50, "the speed (delay) of the game");
DEFINE_string(name, "Rich", "the name of the player");

const int kSamples = 8;

void ParseArgs(vector<string>* args) {
  gflags::SetUsageMessage(
      "Play a game of Snake. Pass --helpshort for options.");
  int argc = static_cast<int>(args->size());

  vector<char*> argvs;
  for (string& str : *args) {
    argvs.push_back(&str[0]);
  }

  char** argv = argvs.data();
  gflags::ParseCommandLineFlags(&argc, &argv, true);
}

void SetUp(App::Settings* settings) {
  vector<string> args = settings->getCommandLineArgs();
  ParseArgs(&args);

  const int width = static_cast<int>(FLAGS_size * FLAGS_tilesize);
  const int height = static_cast<int>(FLAGS_size * FLAGS_tilesize);
  settings->setWindowSize(width, height);
  settings->setResizable(false);
  settings->setTitle("CS 126 Snake");

  // Make non-deterministic. Really should use C++ random library.
  // Maybe this can be enhanced?
  std::srand(std::time(0));
}

}  // namespace snakeapp

CINDER_APP(snakeapp::SnakeApp,
           RendererGl(RendererGl::Options().msaa(snakeapp::kSamples)),
           snakeapp::SetUp)
