package mineopoly_two.strategy;

import mineopoly_two.game.Economy;
import mineopoly_two.action.TurnAction;
import mineopoly_two.item.InventoryItem;

import java.awt.*;
import java.util.Random;

public interface MinePlayerStrategy {

    /**
     * Called at the start of every round
     *
     * @param boardSize The length and width of the square game board
     * @param maxInventorySize The maximum number of items that your player can carry at one time
     * @param maxCharge The amount of charge your robot starts with (number of tile moves before needing to recharge)
     * @param winningScore The first player to reach this score wins the round
     * @param startingBoard A view of the GameBoard at the start of the game. You can use this to pre-compute fixed
     *                       information, like the locations of market or recharge tiles
     * @param startTileLocation A Point representing your starting location in (x, y) coordinates
 *                              (0, 0) is the bottom left and (boardSize - 1, boardSize - 1) is the top right
     * @param isRedPlayer True if this strategy is the red player, false otherwise
     * @param random A random number generator, if your strategy needs random numbers you should use this.
     */
    void initialize(int boardSize, int maxInventorySize, int maxCharge, int winningScore,
                    PlayerBoardView startingBoard, Point startTileLocation, boolean isRedPlayer, Random random);

    /**
     * The main part of your strategy, this method returns what action your player should do on this turn
     *
     * @param boardView A PlayerBoardView object representing all the information about the board and the other player
     *                   that your strategy is allowed to access
     * @param economy The GameEngine's economy object which holds current prices for resources
     * @param currentCharge The amount of charge your robot has (number of tile moves before needing to recharge)
     * @param isRedTurn For use when two players attempt to move to the same spot on the same turn
     *                   If true: The red player will move to the spot, and the blue player will do nothing
     *                   If false: The blue player will move to the spot, and the red player will do nothing
     * @return The TurnAction enum for the action that this strategy wants to perform on this game turn
     */
    TurnAction getTurnAction(PlayerBoardView boardView, Economy economy, int currentCharge, boolean isRedTurn);

    /**
     * Called when the player receives an item from performing a TurnAction that gives an item.
     * At the moment this is only from using PICK_UP on top of a mined resource
     *
     * @param itemReceived The item received from the player's TurnAction on their last turn
     */
    void onReceiveItem(InventoryItem itemReceived);

    /**
     * Called when the player steps on a market tile with items to sell. Tells your strategy how much all
     *  of the items sold for.
     *
     * @param totalSellPrice The combined sell price for all items in your strategy's inventory
     */
    void onSoldInventory(int totalSellPrice);

    /**
     * Gets the name of this strategy. The amount of characters that can actually be displayed on a screen varies,
     *  although by default at screen size 750 it's about 16-20 characters depending on character size
     *
     * @return The name of your strategy for use in the competition and rendering the scoreboard on the GUI
     */
    String getName();

    /**
     * Called at the end of every round to let players reset, and tell them how they did if the strategy does not
     *  track that for itself
     *
     * @param pointsScored The total number of points this strategy scored
     * @param opponentPointsScored The total number of points the opponent's strategy scored
     */
    void endRound(int pointsScored, int opponentPointsScored);
}
