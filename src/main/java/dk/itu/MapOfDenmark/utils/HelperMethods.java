package dk.itu.MapOfDenmark.utils;

/**
 * The HelperMethods class provides utility methods for common calculations and operations.
 */
public class HelperMethods {
    /** Whether debug mode is enabled. */
    public static boolean debug = true;

    /**
     * Calculates the distance between two points using their coordinates.
     *
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return The distance between the two points.
     */
    public static double distance(float x1, float y1, float x2, float y2){
        int degreeFactor = 111139;
        return Math.sqrt(Math.pow((x2-x1),2)+ Math.pow((y2-y1),2)) * degreeFactor;
    }
}
