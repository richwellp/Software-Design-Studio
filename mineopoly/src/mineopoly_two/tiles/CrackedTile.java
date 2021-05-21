package mineopoly_two.tiles;

import mineopoly_two.graphics.ImageManager;
import mineopoly_two.graphics.TileRenderLayer;

import java.awt.*;

public class CrackedTile extends StoneTile {
    public CrackedTile(Point tileLocation) {
        super(tileLocation);
    }

    @Override
    public TileType getType() {
        return TileType.EMPTY;
    }

    @Override
    protected Image[] getImageOverlays(ImageManager imageManager) {
        Image[] imageOverlays = super.getImageOverlays(imageManager);
        Image crackOverlay = imageManager.getScaledImage("crack_3");
        int crackLayerIndex = TileRenderLayer.LAYER_CRACK.ordinal();
        imageOverlays[crackLayerIndex] = crackOverlay;
        return imageOverlays;
    }
}
