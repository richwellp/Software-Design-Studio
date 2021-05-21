package mineopoly_two.util;

import java.awt.*;

public class DistanceUtil {
    /**
     * Computes the manhattan distance between two points.
     * A manhattan distance is the number of steps it would take to travel between the two points
     *  using only up, down, left, and right movements (no diagonals)
     *
     * @param first A point to compute a manhattan distance for relative to the other
     *               (order of first vs. second does not matter)
     * @param second A point to compute a manhattan distance for relative to the other
     *               (order of first vs. second does not matter)
     * @return The manhattan distance between the two points
     */
    public static int getManhattanDistance(Point first, Point second) {
        return getManhattanDistance(first.x, first.y, second.x, second.y);
    }

    /**
     * Computes the manhattan distance between two points.
     * A manhattan distance is the number of steps it would take to travel between the two points
     *  using only up, down, left, and right movements (no diagonals)
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The manhattan distance between the two points
     */
    public static int getManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
