package mineopoly_two.replay;

import mineopoly_two.game.Economy;
import mineopoly_two.action.TurnAction;
import mineopoly_two.item.InventoryItem;
import mineopoly_two.strategy.PlayerBoardView;
import mineopoly_two.strategy.MinePlayerStrategy;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Because we make use of an interface in our design of Mine-opoly, we can do cool things with the contract.
 * This class implements all the necessary functions for a MinePlayerStrategy, but when prompted for an action
 *  on its turn, it just returns the action performed on the same turn number in the game being replayed.
 */
public class ReplayStrategy implements MinePlayerStrategy {
    private Replay gameToReplay;
    private Queue<TurnAction> actionsToReplay;
    private boolean exceptionThrown;
    private boolean isRedPlayer;

    public ReplayStrategy(Replay gameToReplay) {
        this.gameToReplay = gameToReplay;
    }

    @Override
    public void initialize(int boardSize, int maxInventorySize, int maxCharge, int winningScore,
                           PlayerBoardView startingBoard, Point startTileLocation, boolean isRedPlayer, Random random) {
        if (isRedPlayer) {
            actionsToReplay = new LinkedList<>(gameToReplay.getRedPlayerActions());
            exceptionThrown = gameToReplay.redThrewException();
        } else {
            actionsToReplay = new LinkedList<>(gameToReplay.getBluePlayerActions());
            exceptionThrown = gameToReplay.blueThrewException();
        }

        this.isRedPlayer = isRedPlayer;
    }

    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, Economy economy, int currentCharge, boolean isRedTurn) {
        if (actionsToReplay.isEmpty() && exceptionThrown) {
            String exceptionPlayer = isRedPlayer ? "Red" : "Blue";
            throw new RuntimeException("An exception from the " + exceptionPlayer + " Player happened on this turn");
        }
        return actionsToReplay.poll();
    }

    @Override
    public String getName() {
        return "ReplayBot";
    }

    @Override
    public void onReceiveItem(InventoryItem itemReceived) {
        // Don't care
    }

    @Override
    public void onSoldInventory(int totalSellPrice) {
        // Doesn't matter
    }

    @Override
    public void endRound(int totalRedPoints, int totalBluePoints) {
        // Don't need it
    }
}
