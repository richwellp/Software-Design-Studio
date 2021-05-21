package mineopoly_two.replay;

import mineopoly_two.action.TurnAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Replay {
    private static final TurnAction[] allTurnActions = TurnAction.values();

    private int boardSize;
    private long worldSeed;
    private List<TurnAction> redPlayerActions;
    private List<TurnAction> bluePlayerActions;
    private boolean redThrewException;
    private boolean blueThrewException;

    public Replay(int boardSize, long worldSeed, List<TurnAction> redPlayerActions, List<TurnAction> bluePlayerActions,
                  boolean redThrewException, boolean blueThrewException) {
        this.boardSize = boardSize;
        this.worldSeed = worldSeed;
        this.redPlayerActions = redPlayerActions;
        this.bluePlayerActions = bluePlayerActions;
        this.redThrewException = redThrewException;
        this.blueThrewException = blueThrewException;
    }

    /**
     * Decodes the file containing replay information into a Java Replay object
     * The file format is:
     * [board size]\r\n
     * [world seed]\r\n
     * [red player actions]\r\n
     * [blue player actions]\r\n
     * (where player actions are a comma separated list of TurnAction indices)
     *
     * @param replayFilePathToRead The file path to the file containing replay information
     * @return A Replay object consisting of the information read from the file
     * @throws FileNotFoundException If Java cannot find the file at the specified path
     * @throws NumberFormatException If there is an issue parsing any numbers in the replay file
     */
    public static Replay decodeReplayFile(String replayFilePathToRead) throws FileNotFoundException {
        Scanner replayFileReader = new Scanner(new File(replayFilePathToRead));
        int boardSize = Integer.parseInt(replayFileReader.nextLine().trim());
        long worldSeed = Long.parseLong(replayFileReader.nextLine().trim());

        // Split and decode the red and blue player actions lists
        String[] redActionEncoding = replayFileReader.nextLine().trim().split(",");
        String[] blueActionEncoding = replayFileReader.nextLine().trim().split(",");
        List<TurnAction> redActionList = decodeActionList(redActionEncoding);
        List<TurnAction> blueActionList = decodeActionList(blueActionEncoding);

        int lastRedAction = Integer.parseInt(redActionEncoding[redActionEncoding.length - 1]);
        int lastBlueAction = Integer.parseInt(blueActionEncoding[blueActionEncoding.length - 1]);
        boolean redThrewException = (lastRedAction < 0);
        boolean blueThrewException = (lastBlueAction < 0);
        return new Replay(boardSize, worldSeed, redActionList, blueActionList, redThrewException, blueThrewException);
    }

    /**
     * Writes a replay file from a given Replay object
     *
     * @param replay The Replay object to encode into a replay file
     * @param replayFilePathToWrite The file path of the replay file that will be written, if a file exists at this
     *                               path, it will be overwritten
     * @throws IOException If there is an issue writing to the file at the specified path
     */
    public static void encodeReplayFile(Replay replay, String replayFilePathToWrite) throws IOException {
        FileWriter replayWriter = new FileWriter(new File(replayFilePathToWrite));
        replayWriter.write(replay.toString());
        replayWriter.close();
    }

    @Override
    public String toString() {
        String encodedRedActions = encodeActionList(redPlayerActions, redThrewException);
        String encodedBlueActions = encodeActionList(bluePlayerActions, blueThrewException);
        return String.join("\r\n", String.valueOf(boardSize), String.valueOf(worldSeed),
                           encodedRedActions, encodedBlueActions);
    }

    private static String encodeActionList(List<TurnAction> actionList, boolean exceptionThrown) {
        StringBuilder actionListEncoder = new StringBuilder();
        for (TurnAction action : actionList) {
            if (action == null) {
                // There is no turn action at the length of the list, it will be treated as no action
                actionListEncoder.append(allTurnActions.length);
            } else {
                actionListEncoder.append(action.ordinal());
            }
            actionListEncoder.append(',');
        }

        if (exceptionThrown) {
            // A negative number indicates an exception
            actionListEncoder.append("-1");
        } else {
            // Remove trailing comma
            actionListEncoder.deleteCharAt(actionListEncoder.length() - 1);
        }
        return actionListEncoder.toString();
    }

    private static List<TurnAction> decodeActionList(String[] encodedList) {
        List<TurnAction> turnActions = new LinkedList<>();

        for (String encodedAction : encodedList) {
            int actionIndex = Integer.parseInt(encodedAction);
            if (actionIndex < 0) {
                // Exception thrown on this turn
                break;
            }

            if (actionIndex >= allTurnActions.length) {
                // Player did nothing on this turn
                turnActions.add(null);
            } else {
                turnActions.add(allTurnActions[actionIndex]);
            }
        }

        return turnActions;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public long getWorldSeed() {
        return worldSeed;
    }

    public List<TurnAction> getRedPlayerActions() {
        return redPlayerActions;
    }

    public List<TurnAction> getBluePlayerActions() {
        return bluePlayerActions;
    }

    public boolean redThrewException() {
        return redThrewException;
    }

    public boolean blueThrewException() {
        return blueThrewException;
    }
}
