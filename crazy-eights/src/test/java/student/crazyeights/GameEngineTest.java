package student.crazyeights;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.assertEquals;

public class GameEngineTest {

    private List<Card> deck;
    private List<Card> gameDeck;
    private List<PlayerStrategy> players;
    private ByteArrayOutputStream outputStream;
    private GameEngine game;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        players = new ArrayList<PlayerStrategy>();
        game = new GameEngine();
        SimplePlayer player1 = new SimplePlayer();
        SimplePlayer player2 = new SimplePlayer();
        ComplexPlayer player3 = new ComplexPlayer();
        ComplexPlayer player4 = new ComplexPlayer();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        deck = Card.getDeck();
        game.setUpGame(players, outputStream);
        gameDeck = game.getGameDeck();
    }

    @Test
    public void testCardShuffled() {
        boolean isShuffled = false;
        for (int i = 0; i < deck.size(); i++) {
            if (!gameDeck.get(i).equals(deck.get(i))) {
                isShuffled = true;
                break;
            }
        }
        Assert.assertTrue(isShuffled);
    }

    @Test
    public void testPlayerReceiveInitialCards() {
        boolean passed = true;
        for (Integer numberOfCards: game.getNumberOfPlayerCards()) {
            if (numberOfCards != 5) {
                passed = false;
            }
        }
        Assert.assertTrue(passed);
    }


    @Test
    public void testTournament() {
        String output = outputStream.toString();
        game.playTournament();
        Assert.assertThat(output, CoreMatchers.containsString("Tournament ended."));
    }

    @Test
    public void testCheating() {
        String output = outputStream.toString();
        game.playRound();
        Assert.assertThat(output, CoreMatchers.containsString("Cheat"));
    }

    @Test
    public void testGame() {
        String output = outputStream.toString();
        game.playGame();
        Assert.assertThat(output, CoreMatchers.containsString("Game ended."));
    }
}
