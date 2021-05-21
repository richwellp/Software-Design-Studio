// Copyright (c) 2020 CS126SP20. All rights reserved.

#include "snake_app.h"

#include <cinder/Font.h>
#include <cinder/Text.h>
#include <cinder/Vector.h>
#include <cinder/gl/draw.h>
#include <cinder/gl/gl.h>
#include <gflags/gflags.h>
#include <snake/player.h>
#include <snake/segment.h>

#include <algorithm>
#include <chrono>
#include <cmath>
#include <string>

#include <cstdlib>
#include <ctime>
#include <cinder/audio/audio.h>
#include <cinder/ImageIo.h>
#include <cinder/gl/Texture.h>

namespace snakeapp {

using cinder::Color;
using cinder::ColorA;
using cinder::Rectf;
using cinder::TextBox;
using cinder::app::KeyEvent;
using snake::Direction;
using snake::Location;
using snake::Segment;
using std::chrono::duration_cast;
using std::chrono::seconds;
using std::chrono::system_clock;
using std::string;

const double kBackgroundRate = 0.10;
const double kRate = 25;
const size_t kLimit = 3;
const char kDbPath[] = "snake.db";
const seconds kCountdownTime = seconds(10);
#if defined(CINDER_COCOA_TOUCH)
const char kNormalFont[] = "Arial";
const char kBoldFont[] = "Arial-BoldMT";
const char kDifferentFont[] = "AmericanTypewriter";
#elif defined(CINDER_LINUX)
const char kNormalFont[] = "Arial Unicode MS";
const char kBoldFont[] = "Arial Unicode MS";
const char kDifferentFont[] = "Purisa";
#else
const char kNormalFont[] = "Arial";
const char kBoldFont[] = "Arial Bold";
const char kDifferentFont[] = "Papyrus";
#endif

DECLARE_uint32(size);
DECLARE_uint32(tilesize);
DECLARE_uint32(speed);
DECLARE_string(name);

SnakeApp::SnakeApp()
    : engine_{FLAGS_size, FLAGS_size},
      leaderboard_{cinder::app::getAssetPath(kDbPath).string()},
      paused_{false},
      player_name_{FLAGS_name},
      printed_game_over_{false},
      size_{FLAGS_size},
      speed_{FLAGS_speed},
      state_{GameState::kPlaying},
      tile_size_{FLAGS_tilesize},
      time_left_{0} {}

void SnakeApp::setup() {
  cinder::gl::enableDepthWrite();
  cinder::gl::enableDepthRead();
  srand(time(nullptr));  // for random food color
  // set up and play background music
  cinder::audio::SourceFileRef game_file =
      cinder::audio::load(cinder::app::loadAsset("game.mp3"));
  game_sound = cinder::audio::Voice::create(game_file);
  game_sound->start();
  // set up eating sound effect
  cinder::audio::SourceFileRef eat_file =
      cinder::audio::load(cinder::app::loadAsset("eat.wav"));
  eat_sound = cinder::audio::Voice::create(eat_file);
  score = 1;
}

void SnakeApp::update() {
  if (state_ == GameState::kGameOver) {
    if (top_players_.empty()) {
      leaderboard_.AddScoreToLeaderBoard({player_name_, engine_.GetScore()});
      top_players_ = leaderboard_.RetrieveHighScores(kLimit);

      // It is crucial the this vector be populated, given that `kLimit` > 0.
      assert(!top_players_.empty());
    }
    return;
  }

  if (paused_) return;
  const auto time = system_clock::now();

  if (engine_.GetSnake().IsChopped()) {
    if (state_ != GameState::kCountDown) {
      state_ = GameState::kCountDown;
      last_intact_time_ = time;
    }

    // We must be in countdown.
    const auto time_in_countdown = time - last_intact_time_;
    if (time_in_countdown >= kCountdownTime) {
      state_ = GameState::kGameOver;
    }

    using std::chrono::seconds;
    const auto time_left_s =
        duration_cast<seconds>(kCountdownTime - time_in_countdown);
    time_left_ = static_cast<size_t>(
        std::min(kCountdownTime.count() - 1, time_left_s.count()));
  }

  if (time - last_time_ > std::chrono::milliseconds(speed_)) {
    engine_.Step();
    last_time_ = time;
  }
}

void SnakeApp::draw() {
  cinder::gl::enableAlphaBlending();

  if (state_ == GameState::kGameOver) {
    if (!printed_game_over_) cinder::gl::clear(Color(1, 0, 0));
    DrawGameOver();
    return;
  }

  if (paused_) return;

  cinder::gl::clear();
  // TODO: changes here
  auto time_duration = std::chrono::duration_cast<std::chrono::nanoseconds>(last_intact_time_.time_since_epoch());
  double nanoseconds = time_duration.count();
  double food_rate = 1.0 / engine_.GetSnake().Size();
  if (fmod(nanoseconds,food_rate) == 0) {
    DrawBackground();
    // Generate random float 0.0 to 1.0
    // Code was inspired by:https://stackoverflow.com/questions/686353/random-float-number-generation
    float red = static_cast<float>(rand()) / static_cast<float>(RAND_MAX);
    float green = static_cast<float>(rand()) / static_cast<float>(RAND_MAX);
    float blue = static_cast<float>(rand()) / static_cast<float>(RAND_MAX);
    cinder::gl::color(red, green, blue);
    DrawFood();
  }
  // Occasionally draw a background
  if ((double)rand() / RAND_MAX <= kBackgroundRate) {
    cinder::gl::Texture2dRef myImage =
        cinder::gl::Texture2d::create(loadImage(loadAsset("image.jpg")));
    cinder::gl::draw(myImage, cinder::app::getWindowBounds());
  }
  DrawSnake();
  if (score < engine_.GetScore()) {
    eat_sound->start();
    score = engine_.GetScore();
  }
  if (state_ == GameState::kCountDown) DrawCountDown();
}

template <typename C>
void PrintText(const string& text, const C& color, const cinder::ivec2& size,
               const cinder::vec2& loc) {
  cinder::gl::color(color);

  auto box = TextBox()
                 .alignment(TextBox::CENTER)
                 .font(cinder::Font(kNormalFont, 30))
                 .size(size)
                 .color(color)
                 .backgroundColor(ColorA(0, 0, 0, 0))
                 .text(text);

  const auto box_size = box.getSize();
  const cinder::vec2 locp = {loc.x - box_size.x / 2, loc.y - box_size.y / 2};
  const auto surface = box.render();
  const auto texture = cinder::gl::Texture::create(surface);
  cinder::gl::draw(texture, locp);
}

float SnakeApp::PercentageOver() const {
  if (state_ != GameState::kCountDown) return 0.;

  using std::chrono::milliseconds;
  const double elapsed_time =
      duration_cast<milliseconds>(system_clock::now() - last_intact_time_)
          .count();
  const double countdown_time = milliseconds(kCountdownTime).count();
  const double percentage = elapsed_time / countdown_time;
  return static_cast<float>(percentage);
}

void SnakeApp::DrawBackground() const {
  const float percentage = PercentageOver();
  cinder::gl::clear(Color(percentage, 0, 0));
}

void SnakeApp::DrawGameOver() {
  // Lazily print.
  if (printed_game_over_) return;
  if (top_players_.empty()) return;

  const cinder::vec2 center = getWindowCenter();
  const cinder::ivec2 size = {500, 50};
  const Color color = Color::black();

  size_t row = 0;
  PrintText("Game Over :(", color, size, center);
  for (const snake::Player& player : top_players_) {
    std::stringstream ss;
    ss << player.name << " - " << player.score;
    PrintText(ss.str(), color, size, {center.x, center.y + (++row) * 50});
  }

  printed_game_over_ = true;
}

void SnakeApp::DrawSnake() const {
  int num_visible = 0;
  for (const Segment& part : engine_.GetSnake()) {
    const Location loc = part.GetLocation();
    if (part.IsVisibile()) {
      const double opacity = std::exp(-(num_visible++) / kRate);
      cinder::gl::color(ColorA(0, 0, 1, static_cast<float>(opacity)));
    } else {
      const float percentage = PercentageOver();
      cinder::gl::color(Color(percentage, 0, 0));
    }

    cinder::gl::drawSolidRect(Rectf(tile_size_ * loc.Row(),
                                    tile_size_ * loc.Col(),
                                    tile_size_ * loc.Row() + tile_size_,
                                    tile_size_ * loc.Col() + tile_size_));
  }
  const cinder::vec2 center = getWindowCenter();
}

void SnakeApp::DrawFood() const {
  const Location loc = engine_.GetFood().GetLocation();
  cinder::gl::drawSolidRect(Rectf(tile_size_ * loc.Row(),
                                  tile_size_ * loc.Col(),
                                  tile_size_ * loc.Row() + tile_size_,
                                  tile_size_ * loc.Col() + tile_size_));
}

void SnakeApp::DrawCountDown() const {
  const float percentage = PercentageOver();
  const string text = std::to_string(time_left_);
  const Color color = {1 - percentage, 0, 0};
  const cinder::ivec2 size = {50, 50};
  const cinder::vec2 loc = {50, 50};

  PrintText(text, color, size, loc);
}

void SnakeApp::keyDown(KeyEvent event) {
  switch (event.getCode()) {
    case KeyEvent::KEY_UP:
    case KeyEvent::KEY_k:
    case KeyEvent::KEY_w: {
      engine_.SetDirection(Direction::kLeft);
      break;
    }
    case KeyEvent::KEY_DOWN:
    case KeyEvent::KEY_j:
    case KeyEvent::KEY_s: {
      engine_.SetDirection(Direction::kRight);
      break;
    }
    case KeyEvent::KEY_LEFT:
    case KeyEvent::KEY_h:
    case KeyEvent::KEY_a: {
      engine_.SetDirection(Direction::kUp);
      break;
    }
    case KeyEvent::KEY_RIGHT:
    case KeyEvent::KEY_l:
    case KeyEvent::KEY_d: {
      engine_.SetDirection(Direction::kDown);
      break;
    }
    case KeyEvent::KEY_p: {
      paused_ = !paused_;

      if (paused_) {
        last_pause_time_ = system_clock::now();
      } else {
        last_intact_time_ += system_clock::now() - last_pause_time_;
      }
      break;
    }
    case KeyEvent::KEY_r: {
      ResetGame();
      break;
    }
  }
}

void SnakeApp::ResetGame() {
  engine_.Reset();
  paused_ = false;
  printed_game_over_ = false;
  state_ = GameState::kPlaying;
  time_left_ = 0;
  top_players_.clear();
}

}  // namespace snakeapp
