package student.adventure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Layout {

    private String startingRoom;

    private String endingRoom;

    private List<Room> rooms;

    @JsonCreator
    Layout(final @JsonProperty("startingRoom") String setStartingRoom,
           final @JsonProperty("endingRoom") String setEndingRoom,
           final @JsonProperty("rooms") List<Room> setRooms) {
        startingRoom = setStartingRoom;
        endingRoom = setEndingRoom;
        rooms = setRooms;
    }

    public String getStartingRoom() {
        return startingRoom;
    }

    public void setStartingRoom(final String setStartingRoom) {
        this.startingRoom = setStartingRoom;
    }

    public String getEndingRoom() {
        return endingRoom;
    }

    public void setEndingRoom(final String setEndingRoom) {
        this.endingRoom = setEndingRoom;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms (final List<Room> setRooms) {
        this.rooms = setRooms;
    }

    public static class Room {

        private String name;

        private String description;

        private String[] items;

        private List<Direction> directions;

        @JsonCreator
        Room(final @JsonProperty("name") String setName,
             final @JsonProperty("description") String setDescription,
             final @JsonProperty("directions") List<Direction> setDirections) {
            this.name = setName;
            this.description = setDescription;
            this.directions = setDirections;
        }

        public String getName() {
            return name;
        }

        public void setName(final String setName) {
            this.name = setName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(final String setDescription) {
            this.description = setDescription;
        }

        public String[] getItems() {
            return items;
        }

        public void setItems(final String[] setItems) {
            this.items = setItems;
        }

        public List<Direction> getDirections() {
            return directions;
        }

        public void setDirections(final List<Direction> setDirections) {
            this.directions = setDirections;
        }

        public static class Direction {

            private String directionName;

            private String room;

            @JsonCreator
            Direction(final @JsonProperty("directionName") String setDirectionName,
                      final @JsonProperty("room") String setRoom) {
                this.directionName = setDirectionName;
                this.room = setRoom;
            }

            public String getDirectionName() {
                return directionName;
            }

            public void setDirectionName(final String setDirectionName) {
                this.directionName = setDirectionName;
            }

            public String getRoom() {
                return this.room;
            }

            public void setRoom(final String setRoom) {
                this.room = setRoom;
            }
        }
    }
}
