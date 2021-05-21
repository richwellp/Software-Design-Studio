package mineopoly_two.graphics;

import mineopoly_two.game.Economy;
import mineopoly_two.game.GameBoard;
import mineopoly_two.game.GameEngine;
import mineopoly_two.game.MinePlayer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class UserInterface {
    /**
     * Instantiates a JFrame and all the JPanel components necessary to render the game in real time.
     * This method must be called from the Event Dispatch Thread
     *
     * @param engine The GameEngine to be rendered in the JFrame created
     * @param preferredGuiWidth The preferred width of the JFrame
     */
    public static void instantiateGUI(GameEngine engine, int preferredGuiWidth) {
        assert SwingUtilities.isEventDispatchThread();

        ImageManager boardImageManager;
        ImageManager economyImageManager;
        ImageManager playerInfoImageManager;
        try {
            boardImageManager = createDefaultImageManager();
            economyImageManager = createDefaultImageManager();
            playerInfoImageManager = createDefaultImageManager();
        } catch (IOException e) {
            System.err.println("Error loading image resources");
            e.printStackTrace();

            // If we don't have images, we can't render a game
            engine.setGuiEnabled(false);
            return;
        }

        // Ensure the GUI width is an exact multiple of the board size
        GameBoard board = engine.getBoard();
        int numExtraPixels = (preferredGuiWidth % board.getSize());
        if (numExtraPixels != 0) {
            preferredGuiWidth += board.getSize() - numExtraPixels;
        }

        // Create the application window itself
        JFrame gameDisplayFrame = createApplicationWindowWithBoxLayout();

        // Create the game info panels first so the BoxLayout renders them on top
        JPanel topInfoDisplayPanel = createTopGameInfoPanel(engine, preferredGuiWidth, economyImageManager, playerInfoImageManager);
        gameDisplayFrame.add(topInfoDisplayPanel);

        // Create the panel that actually renders the game board
        GameBoardDisplayPanel gameBoardDisplayPanel = new GameBoardDisplayPanel(preferredGuiWidth, board, boardImageManager);
        engine.addObserver(gameBoardDisplayPanel);
        gameDisplayFrame.add(gameBoardDisplayPanel);

        // Tell the application window to resize its contents to their preferred dimensions
        gameDisplayFrame.pack();
    }

    private static JFrame createApplicationWindowWithBoxLayout() {
        JFrame gameDisplayFrame = new JFrame("Mine-opoly");
        gameDisplayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameDisplayFrame.setLayout(new BoxLayout(gameDisplayFrame.getContentPane(), BoxLayout.Y_AXIS));
        gameDisplayFrame.setResizable(false);
        gameDisplayFrame.setVisible(true);
        return gameDisplayFrame;
    }

    private static ImageManager createDefaultImageManager() throws IOException {
        String currentDirectoryPath = System.getProperty("user.dir");
        String[] directoriesToImages = new String[]{currentDirectoryPath, "src", "mineopoly_two", "image_files/"};
        String imagesDirectoryPath = String.join(File.separator, directoriesToImages);
        return new ImageManager(imagesDirectoryPath);
    }

    private static JPanel createTopGameInfoPanel(GameEngine engine, int preferredGuiWidth,
                                                 ImageManager playerInfoImageManager, ImageManager economyImageManager) {
        MinePlayer redPlayer = engine.getRedPlayer();
        Economy gameEconomy = engine.getEconomy();
        MinePlayer bluePlayer = engine.getBluePlayer();

        // Create 3 subpanels, sized to completely fill the top bar
        final int playerInfoPanelWidth = (preferredGuiWidth * 3) / 8;
        final int economyInfoPanelWidth = preferredGuiWidth - (2 * playerInfoPanelWidth);
        PlayerInfoDisplayPanel redDisplayPanel;
        EconomyDisplayPanel economyDisplayPanel;
        PlayerInfoDisplayPanel blueDisplayPanel;
        redDisplayPanel = new PlayerInfoDisplayPanel(playerInfoPanelWidth, redPlayer, playerInfoImageManager);
        economyDisplayPanel = new EconomyDisplayPanel(economyInfoPanelWidth, gameEconomy, economyImageManager);
        blueDisplayPanel = new PlayerInfoDisplayPanel(playerInfoPanelWidth, bluePlayer, playerInfoImageManager);

        // Add sub-panels from left to right to the top parent panel
        JPanel topInfoDisplayPanel = new JPanel();
        topInfoDisplayPanel.setLayout(new BoxLayout(topInfoDisplayPanel, BoxLayout.X_AXIS));
        topInfoDisplayPanel.add(redDisplayPanel);
        topInfoDisplayPanel.add(economyDisplayPanel);
        topInfoDisplayPanel.add(blueDisplayPanel);

        // Set these panels to update when the engine tells them something changed
        engine.addObserver(redDisplayPanel);
        engine.addObserver(blueDisplayPanel);
        engine.getEconomy().addObserver(economyDisplayPanel);
        return topInfoDisplayPanel;
    }
}
