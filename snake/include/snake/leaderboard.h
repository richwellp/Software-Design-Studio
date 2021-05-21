// Copyright (c) 2020 CS126SP20. All rights reserved.

#ifndef SNAKE_LEADERBOARD_H_
#define SNAKE_LEADERBOARD_H_

#include "leaderboard.h"
#include "player.h"

#include <sqlite_modern_cpp.h>

#include <string>
#include <vector>

namespace snake {

class LeaderBoard {
 public:
  // Creates a new leaderboard table if it doesn't already exist.
  explicit LeaderBoard(const std::string& db_path);

  // Adds a player to the leaderboard.
  void AddScoreToLeaderBoard(const Player&);

  // Returns a list of the players with the highest scores, in decreasing order.
  // The size of the list should be no greater than `limit`.
  std::vector<Player> RetrieveHighScores(const size_t limit);

  // Returns a list of the players with the highest scores, in decreasing order,
  // that have the same `name` as the input player name.
  // The size of the list should be no greater than `limit`.
  std::vector<Player> RetrieveHighScores(const Player&, const size_t limit);

 private:
  sqlite::database db_;
};

}  // namespace snake

#endif  // SNAKE_LEADERBOARD_H_
