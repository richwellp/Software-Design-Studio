package mineopoly_two.game;

import mineopoly_two.action.TurnAction;
import mineopoly_two.graphics.ImageManager;
import mineopoly_two.item.InventoryItem;
import mineopoly_two.strategy.MinePlayerStrategy;
import mineopoly_two.tiles.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is the GameEngine's internal representation of all data it needs to know about a player.
 * Therefore, do not try to use this class in your strategy.
 */
public class MinePlayer {
    protected static final int MAX_ENERGY = 80;
    protected static final int MAX_ITEMS = 5;
    private static final Map<TurnAction, String> moveToDirectionMap = new HashMap<>();
    static {
        moveToDirectionMap.put(TurnAction.MOVE_UP, "back");
        moveToDirectionMap.put(TurnAction.MOVE_DOWN, "front");
        moveToDirectionMap.put(TurnAction.MOVE_LEFT, "left");
        moveToDirectionMap.put(TurnAction.MOVE_RIGHT, "right");
    }

    private MinePlayerStrategy strategy;
    private List<InventoryItem> inventory;
    private Tile currentTile;
    private boolean isRedPlayer;
    private int score;
    private TurnAction lastMove;
    private List<TurnAction> actions;
    private Economy economy;
    private int currentEnergy;

    protected MinePlayer(MinePlayerStrategy strategy, Tile startingTile, Economy economy, boolean isRedPlayer) {
        this.strategy = strategy;
        this.inventory = new ArrayList<>(MAX_ITEMS);
        this.currentTile = startingTile;
        this.isRedPlayer = isRedPlayer;
        this.score = 0;
        this.lastMove = TurnAction.MOVE_DOWN;
        this.actions = new LinkedList<>();
        this.economy = economy;
        this.currentEnergy = MAX_ENERGY;
    }

    public MinePlayerStrategy getStrategy() {
        return strategy;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile newTile) {
        this.currentTile = newTile;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    /**
     * Adds an item to the player's inventory, if the inventory isn't full
     *
     * @param itemToAdd The item to be added to the inventory
     * @return True if the item is successfully added, false otherwise
     */
    public boolean addItemToInventory(InventoryItem itemToAdd) {
        if (inventory.size() >= MAX_ITEMS) {
            // Inventory full, could not add the item
            return false;
        }

        // Let the strategy know this player received an item
        strategy.onReceiveItem(itemToAdd);
        inventory.add(itemToAdd);
        return true;
    }

    public void sellItems() {
        int totalItemSellPrice = economy.sellResources(inventory);
        inventory.clear();

        if (totalItemSellPrice > 0) {
            score += totalItemSellPrice;
            // Let the strategy know this player sold all items
            strategy.onSoldInventory(totalItemSellPrice);
        }
    }

    public boolean isRedPlayer() {
        return isRedPlayer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void addTurnAction(TurnAction actionTaken) {
        actions.add(actionTaken);
        if (moveToDirectionMap.containsKey(actionTaken)) {
            lastMove = actionTaken;
        }
    }

    public List<TurnAction> getAllTurnActions() {
        return actions;
    }

    public float getChargePercent() {
        return ((float) currentEnergy) / MAX_ENERGY;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void useEnergy() {
        if (currentEnergy > 0) {
            currentEnergy--;
        }
    }

    public void rechargeEnergy(float percentToRecharge) {
        assert percentToRecharge >= 0;
        currentEnergy += (MAX_ENERGY * percentToRecharge);
        if (currentEnergy > MAX_ENERGY) {
            currentEnergy = MAX_ENERGY;
        }
    }

    /**
     * Gets the Image corresponding the direction the player is currently facing
     *
     * @param imageManager The ImageManager object that manages all images for the JPanel component rendering this Player
     * @return An Image for the direction the player is facing
     */
    public Image getImage(ImageManager imageManager) {
        String playerColor = isRedPlayer ? "red_bot_" : "blue_bot_";
        String directionName = moveToDirectionMap.get(lastMove);
        return imageManager.getScaledImage(playerColor + directionName);
    }
}
