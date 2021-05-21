package mineopoly_two.action;

import mineopoly_two.game.GameBoard;
import mineopoly_two.game.MinePlayer;
import mineopoly_two.tiles.Tile;

import java.awt.*;

/**
 * A MoveAction is pretty self-explanatory.
 * This Action moves the player one tile in the direction specified by xChange and yChange.
 */
public class MoveAction extends Action {
    private static final double NO_ENERGY_MOVE_CHANCE = 0.25;
    private int xChange;
    private int yChange;

    public MoveAction(int xChange, int yChange) {
        this.xChange = xChange;
        this.yChange = yChange;
    }

    @Override
    public void performAction(GameBoard board, MinePlayer player, TurnAction action) {
        Tile currentTile = player.getCurrentTile();
        Point playerLocation = currentTile.getLocation();

        int nextX = playerLocation.x + xChange;
        int nextY = playerLocation.y + yChange;
        boolean nextXInBounds = (nextX >= 0 && nextX < board.getSize());
        boolean nextYInBounds = (nextY >= 0 && nextY < board.getSize());
        if (!nextXInBounds || !nextYInBounds) {
            // Can't step outside the world
            return;
        }

        Tile nextTile = board.getTileAtLocation(nextX, nextY);
        if (nextTile.getPlayerOnTile() != null) {
            // Can't step on a tile if there's another player there
            return;
        }

        boolean hasEnergyLeft = (player.getCurrentEnergy() > 0);
        boolean canMoveWithNoEnergyLeft = (Math.random() < NO_ENERGY_MOVE_CHANCE);
        if (hasEnergyLeft || canMoveWithNoEnergyLeft) {
            // Move is valid, leave old tile and enter new one
            currentTile.onExit(player);
            nextTile.onEnter(player);
            player.setCurrentTile(nextTile);
            player.useEnergy();
        }
    }
}