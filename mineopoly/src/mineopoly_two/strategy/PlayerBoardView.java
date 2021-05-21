package mineopoly_two.strategy;

import mineopoly_two.item.InventoryItem;
import mineopoly_two.tiles.TileType;

import java.awt.*;
import java.util.Map;

@SuppressWarnings("unused")
public class PlayerBoardView {
    private TileType[][] tiles;
    private Map<Point, InventoryItem> itemsOnGround;
    private Point thisPlayerLocation;
    private Point otherPlayerLocation;
    private int otherPlayerScore;

    public PlayerBoardView(TileType[][] tiles, Map<Point, InventoryItem> itemsOnGround,
                           Point thisPlayerLocation, Point otherPlayerLocation, int otherPlayerScore) {
        this.tiles = tiles;
        this.itemsOnGround = itemsOnGround;
        this.thisPlayerLocation = thisPlayerLocation;
        this.otherPlayerLocation = otherPlayerLocation;
        this.otherPlayerScore = otherPlayerScore;
    }

    /**
     * Gets the type of tile at the specified location
     *
     * @param location The Point at which to get the type of the tile
     * @return The type of tile at the location, or null if the location is outside the board
     */
    public TileType getTileTypeAtLocation(Point location) {
        return this.getTileTypeAtLocation(location.x, location.y);
    }

    /**
     * Gets the type of tile at the specified (x, y) coordinates
     *
     * @param x The x coordinate of the tile to get
     * @param y The y coordinate of the tile to get
     * @return The type of tile at the (x, y) Cartesian coordinates, or null if the coordinates are outside the board
     */
    public TileType getTileTypeAtLocation(int x, int y) {
        // The index on the board, and the Cartesian coordinates are mirror opposites in the y direction
        int xIndex = x;
        int yIndex = (tiles.length - 1) - y;

        boolean xIndexInBounds = (xIndex >= 0 && xIndex < tiles.length);
        boolean yIndexInBounds = (yIndex >= 0 && yIndex < tiles.length);
        if (xIndexInBounds && yIndexInBounds) {
            return tiles[yIndex][xIndex];
        }

        return null;
    }

    /**
     * Gets a map from every Point with an item on the ground to the InventoryItem on theground
     *
     * @return The mapping for all locations with InventoryItems to those items
     */
    public Map<Point, InventoryItem> getItemsOnGround() {
        return itemsOnGround;
    }

    /**
     * Gives you your location so you don't have to keep track of when you actually successfully move or not
     *
     * @return A Point representing your player's location, (0, 0) is the bottom left of the board
     */
    public Point getYourLocation() {
        return this.thisPlayerLocation;
    }

    /**
     * Gets the other player's exact location but only if they are within viewing range of your player.
     *
     * @return A Point representing your opponent's location, or null if they are outside viewing range
     */
    public Point getOtherPlayerLocation() {
        // Viewing range for Mine-opoly is the whole board, this will never be null
        return otherPlayerLocation;
    }

    /**
     * Gets the score of the other player.
     * If you want to know your score you have to track that for yourself
     *
     * @return The current score of the other player
     */
    public int getOtherPlayerScore() {
        return otherPlayerScore;
    }
}