import student.crazyeights.ComplexPlayer;
import student.crazyeights.GameEngine;
import student.crazyeights.PlayerStrategy;
import student.crazyeights.SimplePlayer;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // Create 4 players
        List<PlayerStrategy> players = new ArrayList<>();
        SimplePlayer player1 = new SimplePlayer();
        SimplePlayer player2 = new SimplePlayer();
        ComplexPlayer player3 = new ComplexPlayer();
        ComplexPlayer player4 = new ComplexPlayer();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        // Start a game
        GameEngine game = new GameEngine();
        game.setUpGame(players, System.out);
        game.playTournament();
    }
}