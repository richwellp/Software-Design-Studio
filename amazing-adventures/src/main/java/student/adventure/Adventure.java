package student.adventure;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class Adventure {

    /** the Json layout where the adventure takes place. */
    private static Layout layout;

    /** The list of rooms in the Layout. */
    private static List<Layout.Room> rooms;

    /** The endingRoom index in the rooms list. */
    private static int endingRoomIndex;

    /** The currentRoom index starting at startingRoom index. */
    private static int currentRoomIndex;

    /** Works as System.out. */
    private static PrintStream logger;

    /**
     * Asks if the user for a file path and checks if file is valid or not.
     * @param inputStream works as System.in primarily used for testing
     * @param outputStream works as System.out primarily used for testing
     * @return the result as a string
     */
    public static String menu(final InputStream inputStream, final OutputStream outputStream) {
        // Ask for which JSON file to use
        Scanner scanner = new Scanner(inputStream);
        System.out.print("Provide a file path? (Y/N): ");
        String answer = scanner.nextLine();
        while (answer.isEmpty()
                || !(answer.equals("Y") || answer.equals("y") || answer.equals("N") || answer.equals("n"))) {
            System.out.print("Provide a file path? (Y/N): ");
            answer = scanner.nextLine();
        }
        if (answer.equals("Y") || answer.equals("y")) {
            System.out.print("Enter your JSON layout: ");
            String filePath = scanner.nextLine();
            while (filePath.isEmpty()) {
                System.out.print("Enter your JSON layout: ");
                filePath = scanner.nextLine();
            }
            // try to deserialize using Jackson
            try {
                File jsonFile = new File(filePath);
                layout = new ObjectMapper().readValue(jsonFile, Layout.class);
                if (!isValidSchema(layout)) {
                    return "Invalid file";
                }
            } catch (Exception e) {
                // catch if the file is invalid
                System.out.println(e);
                return "Invalid file";
            }
        } else if (answer.equals("N") || answer.equals("n")) {
            System.out.println("Entering default layout.");
            File seibel = new File("src/main/resources/siebel.json");
            try {
                layout = new ObjectMapper().readValue(seibel, Layout.class);
            } catch (Exception e) {
                return "Unexpected error: seibel.json was modified or removed";
            }
        }
        // initialize
        rooms = layout.getRooms();
        String startingRoom = layout.getStartingRoom();
        String endingRoom = layout.getEndingRoom();
        endingRoomIndex = findRoomIndex(endingRoom);
        currentRoomIndex = findRoomIndex(startingRoom);
        logger = new PrintStream(outputStream);
        return "Success";
    }

    /**
     * Checks if the layout parameter follows the desired schema.
     * @param jsonLayout of the JSON file named after class World
     * @return whether layout is valid or not
     */
    private static boolean isValidSchema(final Layout jsonLayout) {
        // Check start, finish, and rooms
        if (jsonLayout.getEndingRoom() == null || jsonLayout.getStartingRoom() == null
                || jsonLayout.getRooms() == null) {
            return false;
        }
        List<Layout.Room> currentRooms = jsonLayout.getRooms();
        // Check each room
        for (Layout.Room room: currentRooms) {
            if (room.getName() == null || room.getDescription() == null || room.getDirections() == null) {
                return false;
            }
            List<Layout.Room.Direction> roomDirections = room.getDirections();
            // Check each direction
            for (Layout.Room.Direction direction: roomDirections) {
                if (direction.getDirectionName() == null || direction.getRoom() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param inputStream works as System.in primarily used for testing
     * @return the result as a string
     */
    public static String startAdventure(final InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        // print room's description
        Layout.Room currentRoom = rooms.get(currentRoomIndex);
        printDescription(currentRoomIndex);
        logger.print("> ");
        String userInput = scanner.nextLine();
        while (userInput.isEmpty()) {
            logger.print("> ");
            userInput = scanner.nextLine();
        }
        // inputs are case-insensitive
        String interpretedInput = userInput.toLowerCase();
        // if line contains exit or quit, program ends
        if (interpretedInput.equals("exit") || interpretedInput.equals("quit")) {
            return "Game Over";
        }
        // user must write "go some_direction" to move
        if (!interpretedInput.contains("go ")) {
            return "I don't understand '" +  userInput + "'";
        } else {
            interpretedInput = interpretedInput.substring(interpretedInput.indexOf("go") + 2).trim();
            if (isValidDirection(currentRoomIndex, interpretedInput)) {
                // find the index of the given direction
                int directionIndex = 0;
                List<Layout.Room.Direction> currentDirections = currentRoom.getDirections();
                while (directionIndex < currentDirections.size()) {
                    String directionName = currentDirections.get(directionIndex).getDirectionName();
                    if (directionName.toLowerCase().equals(interpretedInput)) {
                        break;
                    }
                    directionIndex++;
                }
                // move to that room given the direction
                String nextRoom = currentRoom.getDirections().get(directionIndex).getRoom();
                currentRoomIndex = findRoomIndex(nextRoom);
                if (currentRoomIndex == endingRoomIndex) {
                    logger.println("You have reached the final room.");
                    return "Game Over.";
                } else {
                    return "going " + interpretedInput;
                }
            } else {
                return "I can't go " + interpretedInput;
            }
        }
    }

    /**
     * Prints the description of the room the user is in.
     * @param roomIndex is the index of the rooms list
     */
    private static void printDescription(final int roomIndex) {
        Layout.Room currentRoom = rooms.get(roomIndex);
        String description = currentRoom.getDescription();
        logger.println(description);
        // print the available directions
        logger.print("From here you can go: ");
        List<Layout.Room.Direction> availableDirections = currentRoom.getDirections();
        if (availableDirections.size() == 1) {
            logger.println(availableDirections.get(0).getDirectionName());
        } else {
            for (int index = 0; index < availableDirections.size(); index++) {
                String directionName = availableDirections.get(index).getDirectionName();
                if (index + 1 == availableDirections.size()) {
                    logger.println("or " + directionName);
                } else {
                    logger.print(directionName + ", ");
                }
            }
        }
    }

    /**
     * Checks if the user input or command is valid or not.
     * @param roomIndex is the index of the rooms list
     * @param input is the user input or command.
     * @return true if the direction exists or not
     */
    private static boolean isValidDirection(final int roomIndex, final String input) {
        Layout.Room currentRoom = rooms.get(roomIndex);
        List<Layout.Room.Direction> currentDirection = currentRoom.getDirections();
        for (Layout.Room.Direction availableDirection : currentDirection) {
            if (availableDirection.getDirectionName().toLowerCase().trim().equals(input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the index of the rooms list given the room name.
     * @param roomName a string name that identifies the room
     * @return the index of the rooms list that has the room name
     */
    private static int findRoomIndex(final String roomName) {
        for (int index = 0; index < rooms.size(); index++) {
            if (roomName.equals(rooms.get(index).getName())) {
                return index;
            }
        }
        return -1;
    }
}