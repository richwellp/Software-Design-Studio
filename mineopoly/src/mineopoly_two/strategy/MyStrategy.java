package mineopoly_two.strategy;

import mineopoly_two.action.TurnAction;
import mineopoly_two.game.Economy;
import mineopoly_two.item.InventoryItem;
import mineopoly_two.item.ResourceType;
import mineopoly_two.tiles.TileType;
import mineopoly_two.util.DistanceUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MyStrategy implements MinePlayerStrategy {
    /**
     * The game board size is N x N.
     */
    private int boardSize;

    /**
     * The number of gems a player can hold.
     */
    private int maxInventorySize;

    /**
     * The max number of steps a player can do without recharging.
     */
    private int maxCharge;

    /**
     * The target score to win the game.
     */
    private int winningScore;

    /**
     * The board's state.
     */
    private PlayerBoardView currentBoard;

    /**
     * The players current location represented as a Point.
     */
    private Point myLocation;

    /**
     * True if player is marked as red.
     */
    private boolean isRedPlayer;

    /**
     * Keeps track of the gems the player is currently holding.
     */
    private ArrayList<InventoryItem> currentItems = new ArrayList<>();

    /**
     * Self track of the player's score.
     */
    private int myCurrentScore;

    /**
     * Called at the start of every round
     *
     * @param boardSize         The length and width of the square game board
     * @param maxInventorySize  The maximum number of items that your player can carry at one time
     * @param maxCharge         The amount of charge your robot starts with (number of tile moves before needing to recharge)
     * @param winningScore      The first player to reach this score wins the round
     * @param startingBoard     A view of the GameBoard at the start of the game. You can use this to pre-compute fixed
     *                          information, like the locations of market or recharge tiles
     * @param startTileLocation A Point representing your starting location in (x, y) coordinates
     *                          (0, 0) is the bottom left and (boardSize - 1, boardSize - 1) is the top right
     * @param isRedPlayer       True if this strategy is the red player, false otherwise
     * @param random            A random number generator, if your strategy needs random numbers you should use this.
     */
    @Override
    public void initialize(int boardSize, int maxInventorySize, int maxCharge, int winningScore, PlayerBoardView startingBoard, Point startTileLocation, boolean isRedPlayer, Random random) {
        this.boardSize = boardSize;
        this.maxInventorySize = maxInventorySize;
        this.maxCharge = maxCharge;
        this.winningScore = winningScore;
        this.currentBoard = startingBoard;
        this.myLocation = startTileLocation;
        this.isRedPlayer = isRedPlayer;
        currentItems.clear();
        myCurrentScore = 0;
    }

    /**
     * The main part of your strategy, this method returns what action your player should do on this turn
     *
     * @param boardView     A PlayerBoardView object representing all the information about the board and the other player
     *                      that your strategy is allowed to access
     * @param economy       The GameEngine's economy object which holds current prices for resources
     * @param currentCharge The amount of charge your robot has (number of tile moves before needing to recharge)
     * @param isRedTurn     For use when two players attempt to move to the same spot on the same turn
     *                      If true: The red player will move to the spot, and the blue player will do nothing
     *                      If false: The blue player will move to the spot, and the red player will do nothing
     * @return The TurnAction enum for the action that this strategy wants to perform on this game turn
     */
    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, Economy economy, int currentCharge, boolean isRedTurn) {
        currentBoard = boardView;
        myLocation = currentBoard.getYourLocation();
        Map<Point, InventoryItem> itemsOnGround = boardView.getItemsOnGround();
        if (canWin(economy)) {
            return goNearestMarket();
        } else if (currentItems.size() == maxInventorySize) {
            return goNearestMarket();
        } else if (itemsOnGround.get(myLocation) != null) {
            // try to pickup
            return TurnAction.PICK_UP;
        } else if (currentBoard.getTileTypeAtLocation(myLocation).equals(TileType.RECHARGE)) {
            if (((float) currentCharge) / maxCharge <= 0.90) {
                // charge more
                return null;
            }
        } else if (((float) currentCharge) / maxCharge <= 0.15) {
            // try to charge
            return goNearestChargingStation();
        }
        return mineNearby();
    }

    /**
     * Called when the player receives an item from performing a TurnAction that gives an item.
     * At the moment this is only from using PICK_UP on top of a mined resource
     *
     * @param itemReceived The item received from the player's TurnAction on their last turn
     */
    @Override
    public void onReceiveItem(InventoryItem itemReceived) {
        currentItems.add(itemReceived);
    }

    /**
     * Called when the player steps on a market tile with items to sell. Tells your strategy how much all
     * of the items sold for.
     *
     * @param totalSellPrice The combined sell price for all items in your strategy's inventory
     */
    @Override
    public void onSoldInventory(int totalSellPrice) {
        currentItems.clear();
        myCurrentScore += totalSellPrice;
    }

    /**
     * Gets the name of this strategy. The amount of characters that can actually be displayed on a screen varies,
     * although by default at screen size 750 it's about 16-20 characters depending on character size
     *
     * @return The name of your strategy for use in the competition and rendering the scoreboard on the GUI
     */
    @Override
    public String getName() {
        return "Rich well";
    }

    /**
     * Called at the end of every round to let players reset, and tell them how they did if the strategy does not
     * track that for itself
     *
     * @param pointsScored         The total number of points this strategy scored
     * @param opponentPointsScored The total number of points the opponent's strategy scored
     */
    @Override
    public void endRound(int pointsScored, int opponentPointsScored) {
        currentItems.clear();
        myCurrentScore = 0;
    }

    /**
     * Checks if the player will win if it reaches the market tile by calculating the potential scores of the currentItems
     *
     * @param economy holds the current prices for the resources
     * @return true if the player will win when it reaches the market tile
     */
    private boolean canWin(Economy economy) {
        Map<ResourceType, Integer> prices = economy.getCurrentPrices();
        int evaluatedScore = 0;
        for (InventoryItem item : currentItems) {
            ResourceType myResource = item.getItemType();
            evaluatedScore += prices.get(myResource);
        }
        return evaluatedScore + myCurrentScore >= winningScore;
    }

    /**
     * Finds the nearest market that contains the acceptable color
     *
     * @return the TurnAction to move by one closer to the nearest market
     */
    private TurnAction goNearestMarket() {
        Point marketLocation = findNearestTile(false, true, false);
        if (myLocation.getX() > marketLocation.getX()) {
            return TurnAction.MOVE_LEFT;
        } else if (myLocation.getX() < marketLocation.getX()) {
            return TurnAction.MOVE_RIGHT;
        } else if (myLocation.getY() > marketLocation.getY()) {
            return TurnAction.MOVE_DOWN;
        } else if (myLocation.getY() < marketLocation.getY()) {
            return TurnAction.MOVE_UP;
        }
        return null;
    }

    /**
     * Finds the nearest charging station
     *
     * @return the TurnAction to move by one closer to the nearest charging station
     */
    private TurnAction goNearestChargingStation() {
        Point chargingLocation = findNearestTile(false, false, true);
        if (myLocation.getX() > chargingLocation.getX()) {
            return TurnAction.MOVE_LEFT;
        } else if (myLocation.getX() < chargingLocation.getX()) {
            return TurnAction.MOVE_RIGHT;
        } else if (myLocation.getY() > chargingLocation.getY()) {
            return TurnAction.MOVE_DOWN;
        } else if (myLocation.getY() < chargingLocation.getY()) {
            return TurnAction.MOVE_UP;
        }
        return null;
    }

    /**
     * Locates the nearest resource tile and move one step towards it
     *
     * @return the TurnAction to move one step closer to the targeted resource tile or mine the current tile.
     */
    private TurnAction mineNearby() {
        Point miningLocation = findNearestTile(true, false, false);
        if (myLocation.equals(miningLocation)) {
            return TurnAction.MINE;
        } else if (myLocation.getX() > miningLocation.getX()) {
            return TurnAction.MOVE_LEFT;
        } else if (myLocation.getX() < miningLocation.getX()) {
            return TurnAction.MOVE_RIGHT;
        } else if (myLocation.getY() > miningLocation.getY()) {
            return TurnAction.MOVE_DOWN;
        } else if (myLocation.getY() < miningLocation.getY()) {
            return TurnAction.MOVE_UP;
        } else {
            return null;
        }
    }

    /**
     * Finds the nearest and wanted tile
     *
     * @param resource true if it wants to find a resource tile
     * @param market   true if it wants to find a market tile
     * @param recharge true if it wants to find a recharge tile
     * @return the Point of the nearest tile from the current location
     */
    private Point findNearestTile(boolean resource, boolean market, boolean recharge) {
        Point endPoint = new Point(boardSize - 1, boardSize - 1);
        Point startPoint = new Point(0, 0);
        int smallestDistance = DistanceUtil.getManhattanDistance(startPoint, endPoint);
        Point location = new Point(0, 0);
        for (int x = 0; x < boardSize - 1; x++) {
            for (int y = 0; y < boardSize - 1; y++) {
                TileType currentTile = currentBoard.getTileTypeAtLocation(x, y);
                if (resource && (currentTile.equals(TileType.RESOURCE_DIAMOND) || currentTile.equals(TileType.RESOURCE_EMERALD)
                        || currentTile.equals(TileType.RESOURCE_RUBY))) {
                    int distance = DistanceUtil.getManhattanDistance(new Point(x, y), myLocation);
                    if (smallestDistance >= distance) {
                        smallestDistance = distance;
                        location = new Point(x, y);
                    }
                } else if (market) {
                    if (isRedPlayer && currentBoard.getTileTypeAtLocation(x, y).equals(TileType.RED_MARKET)) {
                        int distance = DistanceUtil.getManhattanDistance(new Point(x, y), myLocation);
                        if (smallestDistance >= distance) {
                            smallestDistance = distance;
                            location = new Point(x, y);
                        }
                    } else if (!isRedPlayer && currentBoard.getTileTypeAtLocation(x, y).equals(TileType.BLUE_MARKET)) {
                        int distance = DistanceUtil.getManhattanDistance(new Point(x, y), myLocation);
                        if (smallestDistance >= distance) {
                            smallestDistance = distance;
                            location = new Point(x, y);
                        }
                    }
                } else if (recharge && currentBoard.getTileTypeAtLocation(x, y).equals(TileType.RECHARGE)) {
                    int distance = DistanceUtil.getManhattanDistance(new Point(x, y), myLocation);
                    if (smallestDistance >= distance) {
                        smallestDistance = distance;
                        location = new Point(x, y);
                    }
                }
            }
        }
        return location;
    }

    public ArrayList<InventoryItem> getCurrentItems() {
        return currentItems;
    }

    public int getMyCurrentScore() {
        return myCurrentScore;
    }
}
