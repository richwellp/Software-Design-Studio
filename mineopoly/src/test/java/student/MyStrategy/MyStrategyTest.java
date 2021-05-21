package test.java.student.MyStrategy;

import mineopoly_two.game.Economy;
import mineopoly_two.game.GameEngine;
import mineopoly_two.item.InventoryItem;
import mineopoly_two.item.ResourceType;
import mineopoly_two.strategy.MinePlayerStrategy;
import mineopoly_two.strategy.MyStrategy;
import mineopoly_two.strategy.RandomStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MyStrategyTest {
    GameEngine gameEngine;
    MyStrategy myStrategy;
    MinePlayerStrategy randomStrategy;
    Economy economy;

    @Before
    public void setUp() {
        myStrategy = new MyStrategy();
        randomStrategy = new RandomStrategy();
        gameEngine = new GameEngine(14, myStrategy, randomStrategy);
        ResourceType[] resourceTypes = {ResourceType.DIAMOND, ResourceType.EMERALD, ResourceType.RUBY};
        economy = new Economy(resourceTypes);
    }

    @Test
    public void testWinRateBoardSize14() {
        int numTotalRounds = 1000;
        int numRoundsWonByMinScore = 0;
        int rounds = 0;
        gameEngine = new GameEngine(14, myStrategy, randomStrategy);
        boolean isRedPlayer = true;
        while (rounds < numTotalRounds) {
            gameEngine.runGame();
            if (isRedPlayer && gameEngine.getRedPlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            } else if (!isRedPlayer && gameEngine.getBluePlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            }
            long randomSeed = System.currentTimeMillis();
            gameEngine.reset(14, randomSeed, true);
            isRedPlayer = !isRedPlayer;
            rounds++;
        }
        Assert.assertTrue(((double) numRoundsWonByMinScore) / numTotalRounds >= 0.99);
    }

    @Test
    public void testWinRateBoardSize20() {
        int numTotalRounds = 1000;
        int numRoundsWonByMinScore = 0;
        int rounds = 0;
        gameEngine = new GameEngine(20, myStrategy, randomStrategy);
        boolean isRedPlayer = true;
        while (rounds < numTotalRounds) {
            gameEngine.runGame();
            if (isRedPlayer && gameEngine.getRedPlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            } else if (!isRedPlayer && gameEngine.getBluePlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            }
            long randomSeed = System.currentTimeMillis();
            gameEngine.reset(20, randomSeed, true);
            isRedPlayer = !isRedPlayer;
            rounds++;
        }
        Assert.assertTrue(((double) numRoundsWonByMinScore) / numTotalRounds >= 0.99);
    }

    @Test
    public void testWinRateBoardSize26() {
        int numTotalRounds = 1000;
        int numRoundsWonByMinScore = 0;
        int rounds = 0;
        gameEngine = new GameEngine(26, myStrategy, randomStrategy);
        boolean isRedPlayer = true;
        while (rounds < numTotalRounds) {
            gameEngine.runGame();
            if (isRedPlayer && gameEngine.getRedPlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            } else if (!isRedPlayer && gameEngine.getBluePlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            }
            long randomSeed = System.currentTimeMillis();
            gameEngine.reset(26, randomSeed, true);
            isRedPlayer = !isRedPlayer;
            rounds++;
        }
        Assert.assertTrue(((double) numRoundsWonByMinScore) / numTotalRounds >= 0.99);
    }

    @Test
    public void testWinRateBoardSize32() {
        int numTotalRounds = 1000;
        int numRoundsWonByMinScore = 0;
        int rounds = 0;
        gameEngine = new GameEngine(32, myStrategy, randomStrategy);
        boolean isRedPlayer = true;
        while (rounds < numTotalRounds) {
            gameEngine.runGame();
            if (isRedPlayer && gameEngine.getRedPlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            } else if (!isRedPlayer && gameEngine.getBluePlayerScore() >= gameEngine.getMinScoreToWin()) {
                numRoundsWonByMinScore++;
            }
            long randomSeed = System.currentTimeMillis();
            gameEngine.reset(32, randomSeed, true);
            isRedPlayer = !isRedPlayer;
            rounds++;
        }
        Assert.assertTrue(((double) numRoundsWonByMinScore) / numTotalRounds >= 0.99);
    }

    @Test
    public void testReceiveItem() {
        InventoryItem item = new InventoryItem(ResourceType.RUBY);
        myStrategy.onReceiveItem(item);
        ArrayList<InventoryItem> items = myStrategy.getCurrentItems();
        Assert.assertTrue(items.contains(item));
    }

    @Test
    public void testOnSoldInventory() {
        myStrategy.onSoldInventory(0);
        ArrayList<InventoryItem> items = myStrategy.getCurrentItems();
        Assert.assertEquals(items.size(), 0);
    }

    @Test
    public void testEndRound() {
        myStrategy.endRound(0, 30);
        ArrayList<InventoryItem> items = myStrategy.getCurrentItems();
        Assert.assertEquals(items.size(), 0);
        int myScore = myStrategy.getMyCurrentScore();
        Assert.assertEquals(myScore, 0);
    }
}
