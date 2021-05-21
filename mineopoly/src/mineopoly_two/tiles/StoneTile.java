package mineopoly_two.tiles;

import mineopoly_two.graphics.ImageManager;
import mineopoly_two.graphics.TileRenderLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class StoneTile extends Tile {
    public StoneTile(Point tileLocation) {
        super(tileLocation);
    }

    @Override
    protected Image[] getImageOverlays(ImageManager imageManager) {
        Image[] imageOverlays = super.getImageOverlays(imageManager);

        String backgroundTileName = "stone_background";
        if (!imageManager.hasImageWithName(backgroundTileName)) {
            final int defaultGreyValue = 140;
            Color backgroundColor = new Color(defaultGreyValue, defaultGreyValue, defaultGreyValue);
            ImageIcon backgroundImage = imageManager.generateBackgroundImageIcon(backgroundColor);
            imageManager.addImageFromIcon(backgroundTileName, backgroundImage);
        }

        // Get the randomly generated stone texturing overlay
        String stoneTexturingImageName = this.location.x + "_" + location.y + "_stone_texturing";
        if (!imageManager.hasImageWithName(stoneTexturingImageName)) {
            ImageIcon randomTexturing = createRandomTexturing(imageManager.getImageWidth(), imageManager.getImageHeight());
            imageManager.addImageFromIcon(stoneTexturingImageName, randomTexturing);
        }

        Image baseTileImage = imageManager.getScaledImage(backgroundTileName);
        Image texturingOverlay = imageManager.getScaledImage(stoneTexturingImageName);
        int baseLayerIndex = TileRenderLayer.LAYER_BASE.ordinal();
        int texturingLayerIndex = TileRenderLayer.LAYER_TEXTURING.ordinal();

        imageOverlays[baseLayerIndex] = baseTileImage;
        imageOverlays[texturingLayerIndex] = texturingOverlay;
        return imageOverlays;
    }

    private ImageIcon createRandomTexturing(int imageWidth, int imageHeight) {
        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageBrush = bufferedImage.createGraphics();
        final int numRandomTextures = 50;
        final int minGreyValue = 90;
        final int maxGreyValue = 140;
        final int maxTextureSize = 4;

        for (int i = 0; i < numRandomTextures; i++) {
            int randomRGB = (int) (Math.random() * (maxGreyValue - minGreyValue)) + minGreyValue;
            Color randomGreyShade = new Color(randomRGB, randomRGB, randomRGB);
            imageBrush.setColor(randomGreyShade);

            int randomX = (int) (Math.random() * imageWidth);
            int randomY = (int) (Math.random() * imageHeight);
            int randomWidth = (int) (Math.random() * maxTextureSize) + 1;
            int randomHeight = (int) (Math.random() * maxTextureSize) + 1;
            imageBrush.fillRect(randomX, randomY, randomWidth, randomHeight);
        }

        return new ImageIcon(bufferedImage);
    }
}
