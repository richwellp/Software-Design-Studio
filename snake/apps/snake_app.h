// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef SNAKE_SNAKEAPP_H_
#define SNAKE_SNAKEAPP_H_

#include <cinder/app/App.h>
#include <cinder/audio/Voice.h>
#include <cinder/gl/gl.h>
#include <snake/engine.h>
#include <snake/leaderboard.h>
#include <snake/player.h>

#include <random>
#include <string>
#include <vector>

namespace snakeapp {

enum class GameState {
  kPlaying,
  kCountDown,
  kGameOver,
};

class SnakeApp : public cinder::app::App {
 public:
  SnakeApp();
  void setup() override;
  void update() override;
  void draw() override;
  void keyDown(cinder::app::KeyEvent) override;

 private:
  void DrawBackground() const;
  void DrawCountDown() const;
  void DrawFood() const;
  void DrawGameOver();
  void DrawSnake() const;
  float PercentageOver() const;
  void ResetGame();

 private:
  snake::Engine engine_;
  std::chrono::time_point<std::chrono::system_clock> last_intact_time_;
  std::chrono::time_point<std::chrono::system_clock> last_pause_time_;
  std::chrono::time_point<std::chrono::system_clock> last_time_;
  snake::LeaderBoard leaderboard_;
  bool paused_;
  const std::string player_name_;
  bool printed_game_over_;
  const size_t size_;
  const size_t speed_;
  GameState state_;
  const size_t tile_size_;
  size_t time_left_;
  std::vector<snake::Player> top_players_;
  cinder::audio::VoiceRef game_sound;
  cinder::audio::VoiceRef eat_sound;
  size_t score;
};

}  // namespace snakeapp

#endif  // SNAKE_SNAKEAPP_H_
