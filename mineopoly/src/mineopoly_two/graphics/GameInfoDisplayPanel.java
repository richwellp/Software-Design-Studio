package mineopoly_two.graphics;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public abstract class GameInfoDisplayPanel extends JPanel implements Observer {
    private static final int DEFAULT_PANEL_HEIGHT = 110;
    protected int preferredWidth;
    protected ImageManager imageManager;

    public GameInfoDisplayPanel(int preferredWidth, ImageManager imageManager) {
        this.preferredWidth = preferredWidth;
        this.imageManager = imageManager;
        this.setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D brush = (Graphics2D) g;
        fillBackground(brush, Color.WHITE);
        drawBorder(brush, Color.BLACK);
    }

    protected void fillBackground(Graphics2D brush, Color backgroundColor) {
        brush.setColor(backgroundColor);
        brush.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    protected void drawBorder(Graphics2D brush, Color borderColor) {
        final int borderWidth = 8;

        // Draw the border around the background
        brush.setColor(borderColor);
        brush.setStroke(new BasicStroke(borderWidth));
        brush.drawRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(preferredWidth, DEFAULT_PANEL_HEIGHT);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
