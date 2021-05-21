package mineopoly_two.game;

import mineopoly_two.action.Action;
import mineopoly_two.action.TurnAction;
import mineopoly_two.item.ResourceType;
import mineopoly_two.replay.Replay;
import mineopoly_two.strategy.MinePlayerStrategy;
import mineopoly_two.strategy.PlayerBoardView;
import mineopoly_two.tiles.Tile;

import java.awt.Point;
import java.util.Observable;
import java.util.Random;

@SuppressWarnings("unused")
public class GameEngine extends Observable {
    private static final int MAX_TURNS_PER_GAME = 1000;
    private static final double TURNS_PER_SECOND = 20;

    private long randomSeed;
    private GameBoard board;
    private MinePlayer redPlayer;
    private MinePlayer bluePlayer;
    private Economy economy;
    private boolean guiEnabled;
    private int minScoreToWin;

    // Variables to greatly simplify exception flow logic
    private MinePlayer playerWhoThrewException;
    private Exception exceptionThrown;

    public GameEngine(int boardSize, MinePlayerStrategy redPlayerStrategy, MinePlayerStrategy bluePlayerStrategy) {
        this(boardSize, redPlayerStrategy, bluePlayerStrategy, System.currentTimeMillis());
    }

    public GameEngine(int boardSize, MinePlayerStrategy redPlayerStrategy, MinePlayerStrategy bluePlayerStrategy, long randomSeed) {
        // Generate a random GameBoard and set player start tiles
        this.setupEngineForGame(boardSize, randomSeed);

        Tile redStartTile = board.getTileAtLocation(board.getRedStartTileLocation());
        Tile blueStartTile = board.getTileAtLocation(board.getBlueStartTileLocation());
        this.redPlayer = new MinePlayer(redPlayerStrategy, redStartTile, economy, true);
        this.bluePlayer = new MinePlayer(bluePlayerStrategy, blueStartTile, economy, false);
        this.guiEnabled = false;
    }

    private void setupEngineForGame(int boardSize, long randomSeed) {
        this.randomSeed = randomSeed;
        WorldGenerator worldGenerator = new WorldGenerator(randomSeed);
        this.board = worldGenerator.generateBoard(boardSize);
        this.economy = new Economy(ResourceType.values());
        this.minScoreToWin = 30 * boardSize * boardSize;

        this.playerWhoThrewException = null;
        this.exceptionThrown = null;
    }

    /**
     * Allows the same GameEngine object to be used for multiple games
     *
     * @param newBoardSize The size of the new game board to be generated
     * @param newSeed The new random seed value for world generation and the strategies to use
     * @param swapPlayers If true, the last red player will be the next blue player and vice versa
     */
    public void reset(int newBoardSize, long newSeed, boolean swapPlayers) {
        this.setupEngineForGame(newBoardSize, newSeed);

        MinePlayerStrategy redPlayerStrategy = this.redPlayer.getStrategy();
        MinePlayerStrategy bluePlayerStrategy = this.bluePlayer.getStrategy();
        Tile redStartTile = board.getTileAtLocation(board.getRedStartTileLocation());
        Tile blueStartTile = board.getTileAtLocation(board.getBlueStartTileLocation());
        if (swapPlayers) {
            this.redPlayer = new MinePlayer(bluePlayerStrategy, redStartTile, economy, true);
            this.bluePlayer = new MinePlayer(redPlayerStrategy, blueStartTile, economy, false);
        } else {
            this.redPlayer = new MinePlayer(redPlayerStrategy, redStartTile, economy, true);
            this.bluePlayer = new MinePlayer(bluePlayerStrategy, blueStartTile, economy, false);
        }
    }

    public GameBoard getBoard() {
        return board;
    }

    public MinePlayer getRedPlayer() {
        return redPlayer;
    }

    public MinePlayer getBluePlayer() {
        return bluePlayer;
    }

    public Economy getEconomy() {
        return economy;
    }

    public int getRedPlayerScore() {
        return redPlayer.getScore();
    }

    public int getBluePlayerScore() {
        return bluePlayer.getScore();
    }

    public int getMinScoreToWin() {
        // The minimum score to immediately end the game without finishing 1000 turns
        return minScoreToWin;
    }

    public Exception getExceptionThrown() {
        return exceptionThrown;
    }

    public long getRandomSeed() {
        return randomSeed;
    }

    public boolean isGuiEnabled() {
        return this.guiEnabled;
    }

    public void setGuiEnabled(boolean guiEnabled) {
        this.guiEnabled = guiEnabled;
    }

    public Replay getReplay() {
        boolean redThrewException = (playerWhoThrewException == redPlayer);
        boolean blueThrewException = (playerWhoThrewException == bluePlayer);
        return new Replay(board.getSize(), randomSeed, redPlayer.getAllTurnActions(), bluePlayer.getAllTurnActions(),
                          redThrewException, blueThrewException);
    }

    /**
     * Runs through a round of Mine-opoly until either the maximum number of turns is reached
     *  or a player achieves the score needed to win. If either player strategy throws an exception at any time,
     *  that strategy will receive a score of -1 and the game will end
     */
    public void runGame() {
        // Wait a few seconds at the start for graphical components to load
        delayBetweenGuiFrames(2000);

        try {
            runGameLoop();
        } catch (Exception e) {
            // It's generally bad practice to catch generic Exceptions, but because a strategy can throw an exception
            // of any type, it's unavoidable here
            playerWhoThrewException.setScore(-1);
            this.exceptionThrown = e;

            // Let anything watching update
            setChanged();
            notifyObservers();
            e.printStackTrace();
        }
    }

    private void runGameLoop() {
        initializePlayer(redPlayer, true);
        initializePlayer(bluePlayer, false);

        int turnNumber = 0;
        boolean isRedTurn = true;
        MinePlayer firstPlayer;
        MinePlayer secondPlayer;
        boolean roundHasWinner = false;

        while (turnNumber < MAX_TURNS_PER_GAME && !roundHasWinner) {
            delayBetweenGuiFrames((long) (1000 / TURNS_PER_SECOND));

            if (isRedTurn) {
                firstPlayer = redPlayer;
                secondPlayer = bluePlayer;
            } else {
                firstPlayer = bluePlayer;
                secondPlayer = redPlayer;
            }
            processTurn(firstPlayer, secondPlayer, isRedTurn);
            processTurn(secondPlayer, firstPlayer, isRedTurn);
            board.update();
            economy.increaseDemand();

            isRedTurn = !isRedTurn;
            turnNumber++;
            roundHasWinner = (redPlayer.getScore() >= minScoreToWin) || (bluePlayer.getScore() >= minScoreToWin);
            // The state of the engine has changed, let anything observing it (like the GUI) know
            this.setChanged();
            this.notifyObservers();
        }

        endRound();
    }

    private void initializePlayer(MinePlayer playerToInitialize, boolean isRedPlayer) {
        playerWhoThrewException = playerToInitialize; // If an exception gets thrown, we know who did it

        int boardSize = board.getSize();
        int maxInventorySize = MinePlayer.MAX_ITEMS;
        int maxCharge = MinePlayer.MAX_ENERGY;
        Point playerStartLocation = playerToInitialize.getCurrentTile().getLocation();
        Point playerStartCopy = new Point(playerStartLocation.x, playerStartLocation.y);

        // Figure out start tiles based on color
        Point startTileLocation;
        Point opponentStartTile;
        if (playerToInitialize.isRedPlayer()) {
            startTileLocation = board.getRedStartTileLocation();
            opponentStartTile = board.getBlueStartTileLocation();
        } else {
            startTileLocation = board.getBlueStartTileLocation();
            opponentStartTile = board.getRedStartTileLocation();
        }

        // Get the initial view of the board and finally initialize the strategy
        PlayerBoardView startingBoard = board.convertToView(playerToInitialize, opponentStartTile, 0);
        playerToInitialize.getStrategy().initialize(boardSize, maxInventorySize, maxCharge, minScoreToWin,
                                                    startingBoard, playerStartCopy, isRedPlayer, new Random(randomSeed));
        board.getTileAtLocation(startTileLocation).onEnter(playerToInitialize);
    }

    private void processTurn(MinePlayer currentPlayer, MinePlayer otherPlayer, boolean isRedTurn) {
        playerWhoThrewException = currentPlayer; // If an exception gets thrown, we know who did it

        // Ask the player what they want to do
        PlayerBoardView boardView = board.convertToView(currentPlayer, otherPlayer);
        MinePlayerStrategy playerStrategy = currentPlayer.getStrategy();
        int robotCharge = currentPlayer.getCurrentEnergy();
        TurnAction playerAction = playerStrategy.getTurnAction(boardView, economy, robotCharge, isRedTurn);
        currentPlayer.addTurnAction(playerAction);
        if (playerAction == null) {
            return;
        }

        // Perform the player's selected action
        Action actionToPerform = playerAction.getActionToPerform();
        actionToPerform.performAction(board, currentPlayer, playerAction);
    }

    private void endRound() {
        int redPlayerScore = redPlayer.getScore();
        int bluePlayerScore = bluePlayer.getScore();
        playerWhoThrewException = redPlayer;
        redPlayer.getStrategy().endRound(redPlayerScore, bluePlayerScore);
        playerWhoThrewException = bluePlayer;
        bluePlayer.getStrategy().endRound(bluePlayerScore, redPlayerScore);
        playerWhoThrewException = null;
    }

    private void delayBetweenGuiFrames(long millisecondsToWait) {
        if (!guiEnabled) {
            return;
        }

        try {
            Thread.sleep(millisecondsToWait);
        } catch (InterruptedException e) {
            System.err.println("Sleeping in between turns failed");
        }
    }
}
