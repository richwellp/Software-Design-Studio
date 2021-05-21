package mineopoly_two.tiles;

import mineopoly_two.game.MinePlayer;
import mineopoly_two.action.TurnAction;

import java.awt.*;

public class EmptyTile extends StoneTile {

    public EmptyTile(Point tileLocation) {
        super(tileLocation);
    }

    @Override
    public TileType getType() {
        return TileType.EMPTY;
    }

    @Override
    public Tile interact(MinePlayer playerOnTile, TurnAction actionOnTile) {
        if (actionOnTile == TurnAction.MINE) {
            // There's really no reason to do this, but sure you can mine empty tiles
            return new CrackedTile(location);
        }
        return this;
    }
}
