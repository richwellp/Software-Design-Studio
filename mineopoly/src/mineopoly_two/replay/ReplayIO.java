package mineopoly_two.replay;

import mineopoly_two.game.GameEngine;
import mineopoly_two.strategy.MinePlayerStrategy;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ReplayIO {
    /**
     * Sets up a GameEngine to play a replay of a past match as specified in the replay file
     *
     * @param replayFilePath The path to the replay file of a past match
     * @return A GameEngine ready for runGame() to be called to replay the match
     */
    public static GameEngine setupEngineForReplay(String replayFilePath) {
        Replay gameReplay;
        try {
            gameReplay = Replay.decodeReplayFile(replayFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Could not locate the replay file at the given path");
            e.printStackTrace();
            return null;
        } catch (NumberFormatException e) {
            System.err.println("There was an issue decoding the replay file");
            e.printStackTrace();
            return null;
        }

        int boardSize = gameReplay.getBoardSize();
        MinePlayerStrategy redPlayerReplay = new ReplayStrategy(gameReplay);
        MinePlayerStrategy bluePlayerReplay = new ReplayStrategy(gameReplay);
        long worldSeed = gameReplay.getWorldSeed();

        GameEngine replayEngine = new GameEngine(boardSize, redPlayerReplay, bluePlayerReplay, worldSeed);
        replayEngine.setGuiEnabled(true); // I'm going to assume you're doing this to watch
        return replayEngine;
    }

    /**
     * Writes a Replay object, probably retrieved from the GameEngine, to a file so it can be watched again in
     *  the future. This will overwrite a file if a file already exists at the specified path
     *
     * @param replayToWrite The Replay object to write to a replay file
     * @param replayFilePath The file path at which to write the replay file
     */
    public static void writeReplayToFile(Replay replayToWrite, String replayFilePath) {
        try {
            Replay.encodeReplayFile(replayToWrite, replayFilePath);
        } catch (IOException e) {
            System.err.println("There was an error encoding the game replay");
            e.printStackTrace();
        }
    }
}
