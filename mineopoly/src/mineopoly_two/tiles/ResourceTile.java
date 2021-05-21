package mineopoly_two.tiles;

import mineopoly_two.game.MinePlayer;
import mineopoly_two.action.TurnAction;
import mineopoly_two.graphics.ImageManager;
import mineopoly_two.graphics.TileRenderLayer;
import mineopoly_two.item.InventoryItem;
import mineopoly_two.item.ResourceType;

import java.awt.*;

public class ResourceTile extends StoneTile {
    private ResourceType tileResource;
    private int numTimesMined;

    public ResourceTile(Point tileLocation, ResourceType tileResource) {
        super(tileLocation);
        this.tileResource = tileResource;
        this.numTimesMined = 0;
    }

    @Override
    public TileType getType() {
        return tileResource.getResourceTileType();
    }

    @Override
    public Tile interact(MinePlayer playerOnTile, TurnAction actionOnTile) {
        super.interact(playerOnTile, actionOnTile);

        if (actionOnTile == TurnAction.MINE) {
            numTimesMined++;

            if (numTimesMined >= tileResource.getTurnsToMine()) {
                Tile minedTile = new CrackedTile(location);
                minedTile.itemOnTile = new InventoryItem(tileResource);
                return minedTile;
            }
        }
        return this;
    }

    @Override
    protected Image[] getImageOverlays(ImageManager imageManager) {
        Image[] imageOverlays = super.getImageOverlays(imageManager);

        // Add texture for resource
        Image resourceTileOverlay = imageManager.getScaledImage(tileResource.getTileImageName());
        int resourceTypeLayerIndex = TileRenderLayer.LAYER_RESOURCE_TYPE.ordinal();
        imageOverlays[resourceTypeLayerIndex] = resourceTileOverlay;

        // Add crack textures if this has been mined
        Image crackOverlay = imageManager.getScaledImage("crack_" + numTimesMined);
        int crackLayerIndex = TileRenderLayer.LAYER_CRACK.ordinal();
        imageOverlays[crackLayerIndex] = crackOverlay;
        return imageOverlays;
    }
}
