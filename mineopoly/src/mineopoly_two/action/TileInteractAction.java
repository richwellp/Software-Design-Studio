package mineopoly_two.action;

import mineopoly_two.game.GameBoard;
import mineopoly_two.game.MinePlayer;
import mineopoly_two.item.InventoryItem;
import mineopoly_two.tiles.Tile;

/**
 * A TileInteractAction represents any Action that is performed on a Tile that may change
 *  properties of the Tile after being performed. Current examples of this are mining a tile
 *  or picking up an item on the tile.
 */
public class TileInteractAction extends Action {
    @Override
    public void performAction(GameBoard board, MinePlayer player, TurnAction action) {
        Tile currentPlayerTile = player.getCurrentTile();
        InventoryItem itemBeforeAction = currentPlayerTile.getItemOnTile();
        Tile tileAfterAction = currentPlayerTile.interact(player, action);
        InventoryItem itemAfterAction = tileAfterAction.getItemOnTile();

        if (tileAfterAction != currentPlayerTile) {
            // Tile has changed as a result of the action
            board.setTileAtTileLocation(tileAfterAction);
            player.setCurrentTile(tileAfterAction);
        }

        // If the item on the tile changed, make sure the board keeps track
        if (itemAfterAction != null && itemBeforeAction != itemAfterAction) {
            // Item was added or changed
            board.trackItemOnPoint(tileAfterAction.getLocation(), itemAfterAction);
        } else if (itemAfterAction == null && itemBeforeAction != null) {
            // Item was removed
            board.markItemRemovedFromPoint(tileAfterAction.getLocation());
        }
    }
}
