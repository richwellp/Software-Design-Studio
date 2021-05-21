package mineopoly_two.graphics;

import mineopoly_two.game.Economy;
import mineopoly_two.item.ResourceType;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class EconomyDisplayPanel extends GameInfoDisplayPanel {
    private static final int RESOURCE_IMAGE_SIZE = 60;
    private Economy gameEconomy;

    public EconomyDisplayPanel(int preferredWidth, Economy gameEconomy, ImageManager imageManager) {
        super(preferredWidth, imageManager);
        this.gameEconomy = gameEconomy;
        imageManager.rescaleImages(RESOURCE_IMAGE_SIZE, RESOURCE_IMAGE_SIZE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D brush = (Graphics2D) g;
        this.drawEconomyInfo(brush);
        super.drawBorder(brush, Color.BLACK);
    }

    private void drawEconomyInfo(Graphics2D brush) {
        brush.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        brush.setColor(Color.BLACK);

        // Sort ResourceTypes in descending order by price
        Map<ResourceType, Integer> resourcePrices = gameEconomy.getCurrentPrices();
        List<ResourceType> resourcesSortedByPrice = resourcePrices.entrySet().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        FontMetrics fontMetrics = brush.getFontMetrics();
        for (int i = 0; i < resourcesSortedByPrice.size(); i++) {
            ResourceType currentResource = resourcesSortedByPrice.get(i);
            String resourcePriceText = "- " + String.valueOf(resourcePrices.getOrDefault(currentResource, 0));
            int resourcePriceWidth = fontMetrics.stringWidth(resourcePriceText);

            int resourceXPosition = (this.getWidth() - (RESOURCE_IMAGE_SIZE + resourcePriceWidth)) / 2;
            brush.drawImage(imageManager.getScaledImage(currentResource.getItemImageName()), resourceXPosition, (33 * i) - 10, null);
            brush.drawString(resourcePriceText, resourceXPosition + RESOURCE_IMAGE_SIZE - 7, (i + 1) * this.getHeight() / 4 + (7 * i));
        }
    }
}
