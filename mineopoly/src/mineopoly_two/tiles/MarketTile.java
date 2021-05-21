package mineopoly_two.tiles;

import mineopoly_two.game.MinePlayer;
import mineopoly_two.graphics.ImageManager;
import mineopoly_two.graphics.TileRenderLayer;

import javax.swing.*;
import java.awt.*;

public class MarketTile extends Tile {
    private boolean isRedMarket;

    public MarketTile(Point tileLocation, boolean isRedMarket) {
        super(tileLocation);
        this.isRedMarket = isRedMarket;
    }

    @Override
    public TileType getType() {
        return (isRedMarket) ? (TileType.RED_MARKET) : (TileType.BLUE_MARKET);
    }

    @Override
    public void onEnter(MinePlayer playerEnteringTile) {
        super.onEnter(playerEnteringTile);

        boolean isRedPlayerOnRedMarket = isRedMarket && playerEnteringTile.isRedPlayer();
        boolean isBluePlayerOnBlueMarket = !isRedMarket && !playerEnteringTile.isRedPlayer();
        if (isRedPlayerOnRedMarket || isBluePlayerOnBlueMarket) {
            playerEnteringTile.sellItems();
        }
    }

    @Override
    protected Image[] getImageOverlays(ImageManager imageManager) {
        Image[] imageOverlays = super.getImageOverlays(imageManager);

        // Base tile is just red or blue
        String backgroundTileName = (isRedMarket) ? ("red_market_background") : ("blue_market_background");
        if (!imageManager.hasImageWithName(backgroundTileName)) {
            Color backgroundColor = (isRedMarket) ? (new Color(240, 72, 60)) : (new Color(50, 85, 240));
            ImageIcon backgroundImage = imageManager.generateBackgroundImageIcon(backgroundColor);
            imageManager.addImageFromIcon(backgroundTileName, backgroundImage);
        }
        Image baseTileImage = imageManager.getScaledImage(backgroundTileName);
        int baseLayerIndex = TileRenderLayer.LAYER_BASE.ordinal();
        imageOverlays[baseLayerIndex] = baseTileImage;

        // Add player specific market overlay
        String marketImageName = (isRedMarket) ? ("red_market") : ("blue_market");
        int buildingLayerIndex = TileRenderLayer.LAYER_BUILDING.ordinal();
        imageOverlays[buildingLayerIndex] = imageManager.getScaledImage(marketImageName);
        return imageOverlays;
    }
}
