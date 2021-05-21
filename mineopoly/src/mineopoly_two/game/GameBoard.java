package mineopoly_two.game;

import mineopoly_two.graphics.ImageManager;
import mineopoly_two.item.InventoryItem;
import mineopoly_two.strategy.PlayerBoardView;
import mineopoly_two.tiles.Tile;
import mineopoly_two.tiles.TileType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    private Tile[][] board;
    private Point redStartLocation;
    private Point blueStartLocation;

    // Variables to pre-compute things about the board and change them quickly on the fly
    private TileType[][] tileView;
    private Map<Point, InventoryItem> itemsOnGround;

    public GameBoard(Tile[][] tiles) {
        board = tiles;

        // Pre-compute the intensive variables to give to a PlayerBoardView
        itemsOnGround = new HashMap<>();
        tileView = new TileType[board.length][board.length];
        for (int i = 0; i < tileView.length; i++) {
            for (int j = 0; j < tileView[i].length; j++) {
                tileView[i][j] = board[i][j].getType();

                InventoryItem itemOnTile = board[i][j].getItemOnTile();
                if (itemOnTile != null) {
                    Point itemLocation = board[i][j].getLocation();
                    Point itemLocationCopy = new Point(itemLocation.x, itemLocation.y);
                    itemsOnGround.put(itemLocationCopy, itemOnTile);
                }
            }
        }
    }

    public int getSize() {
        return board.length;
    }

    public Point getRedStartTileLocation() {
        return redStartLocation;
    }

    public Point getBlueStartTileLocation() {
        return blueStartLocation;
    }

    public void setRedStartLocation(Point redStartLocation) {
        this.redStartLocation = redStartLocation;
    }

    public void setBlueStartLocation(Point blueStartLocation) {
        this.blueStartLocation = blueStartLocation;
    }

    public void trackItemOnPoint(Point pointWithItem, InventoryItem itemOnPoint) {
        itemsOnGround.put(pointWithItem, itemOnPoint);
    }

    public void markItemRemovedFromPoint(Point pointWithItemRemoved) {
        itemsOnGround.remove(pointWithItemRemoved);
    }

    /**
     * Gets the tile at the specified location in Cartesian (x, y) coordinates with (0, 0) as the bottom left tile
     *  and (boardSize - 1, boardSize - 1) as the top right tile
     *
     * @param location A Point representing (x, y) coordinates of the tile to get
     * @return The Tile at the specified location on the board
     */
    public Tile getTileAtLocation(Point location) {
        return getTileAtLocation(location.x, location.y);
    }

    /**
     * Gets the tile at the specified location in Cartesian (x, y) coordinates with (0, 0) as the bottom left tile
     *  and (boardSize - 1, boardSize - 1) as the top right tile
     *
     * @param x The x coordinate of the tile to get
     * @param y The y coordinate of the tile to get
     * @return The Tile at the specified location on the board
     */
    public Tile getTileAtLocation(int x, int y) {
        if (isValidLocation(x, y)) {
            return board[(board.length - 1) - y][x];
        }
        return null;
    }

    /**
     * Updates the Tile at the parameter tile's location to be the parameter tile. This function handles
     *  logic like calling Tile.onEnter() if necessary
     *
     * @param newTile The Tile that will be set at the Point specified by newTile.getLocation()
     */
    public void setTileAtTileLocation(Tile newTile) {
        // Because tiles know their location, we don't need to pass it in
        int x = newTile.getLocation().x;
        int y = newTile.getLocation().y;

        if (isValidLocation(x, y)) {
            Tile oldTile = board[(board.length - 1) - y][x];
            MinePlayer playerOnTile = oldTile.getPlayerOnTile();

            if (playerOnTile != null) {
                oldTile.onExit(playerOnTile);
                newTile.onEnter(playerOnTile);
                playerOnTile.setCurrentTile(newTile);
            }
            board[(board.length - 1) - y][x] = newTile;
            tileView[(board.length - 1) - y][x] = newTile.getType();
        }
    }

    private boolean isValidLocation(Point location) {
        return isValidLocation(location.x, location.y);
    }

    private boolean isValidLocation(int x, int y) {
        int xIndex = x;
        int yIndex = (board.length - 1) - y;

        boolean xIndexInBounds = (xIndex >= 0 && xIndex < board.length);
        boolean yIndexInBounds = (yIndex >= 0 && yIndex < board.length);
        return xIndexInBounds && yIndexInBounds;
    }

    /**
     * Called every turn for the board to update its internal state
     */
    public void update() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Tile currentTile = board[i][j];
                currentTile.update();
            }
        }
    }

    /**
     * Converts this GameBoard into a restricted information view of the board relative to what one player is
     *  allowed to know
     *
     * @param playerReceivingView The player who will receive this restricted information view
     * @param otherPlayer The other player, so the player receiving the view can know score information
     * @return A PlayerBoardView that contains all the information about this GameBoard for this turn which a
     *          player strategy is allowed to know
     */
    public PlayerBoardView convertToView(MinePlayer playerReceivingView, MinePlayer otherPlayer) {
        Point otherPlayerLocation = otherPlayer.getCurrentTile().getLocation();
        int otherPlayerScore = otherPlayer.getScore();
        return this.convertToView(playerReceivingView, otherPlayerLocation, otherPlayerScore);
    }

    /**
     * Converts this GameBoard into a restricted information view of the board relative to what one player is
     *  allowed to know
     *
     * @param playerReceivingView The player who will receive this restricted information view
     * @param otherPlayerLocation The opposing player's location
     * @param otherPlayerScore The opposing player's current score
     * @return A PlayerBoardView that contains all the information about this GameBoard for this turn which a
     *          player strategy is allowed to know
     */
    public PlayerBoardView convertToView(MinePlayer playerReceivingView, Point otherPlayerLocation,
                                         int otherPlayerScore) {
        // Because we're passing around references, we don't want one strategy to change what the other sees
        //  so we need to be careful and make copies of data being passed by reference
        Map<Point, InventoryItem> itemsOnGroundCopy = new HashMap<>();
        for (Point pointWithItem : itemsOnGround.keySet()) {
            Point pointCopy = new Point(pointWithItem.x, pointWithItem.y);
            itemsOnGroundCopy.put(pointCopy, itemsOnGround.get(pointWithItem));
        }

        // Make copies of these locations so they may not be modified by a strategy
        Point playerLocation = playerReceivingView.getCurrentTile().getLocation();
        Point playerLocationCopy = new Point(playerLocation.x, playerLocation.y);
        Point otherLocationCopy = new Point(otherPlayerLocation.x, otherPlayerLocation.y);
        // We don't need to copy tileView because it is never given to the strategy by reference
        return new PlayerBoardView(tileView, itemsOnGroundCopy, playerLocationCopy, otherLocationCopy, otherPlayerScore);
    }

    /**
     * Specifies how to render this GameBoard on the Graphics2D object passed in
     *
     * @param brush The Graphics2D object on which to render this GameBoard
     * @param imageManager The ImageManager object that manages all images for the JPanel component rendering this GameBoard
     */
    public void paint(Graphics2D brush, ImageManager imageManager) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Tile currentTile = board[i][j];
                currentTile.paint(brush, board.length, imageManager);
            }
        }
    }
}
