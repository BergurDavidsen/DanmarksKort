package dk.itu.MapOfDenmark.Model;

import java.io.Serializable;

/**
 * A serializable rectangle class representing a rectangle in 2D space.
 */
public class SerializableRectangle implements Serializable {
    private double minX;
    private double minY;
    private double width;
    private double height;

    /**
     * Constructs a SerializableRectangle with the specified parameters.
     *
     * @param minX   The x-coordinate of the top-left corner of the rectangle.
     * @param minY   The y-coordinate of the top-left corner of the rectangle.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public SerializableRectangle(double minX, double minY, double width, double height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    /**
     * Retrieves the x-coordinate of the top-left corner of the rectangle.
     *
     * @return The x-coordinate of the top-left corner.
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Retrieves the y-coordinate of the top-left corner of the rectangle.
     *
     * @return The y-coordinate of the top-left corner.
     */
    public double getMinY() {
        return minY;
    }

    /**
     * Retrieves the width of the rectangle.
     *
     * @return The width of the rectangle.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the rectangle.
     *
     * @return The height of the rectangle.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Calculates the x-coordinate of the bottom-right corner of the rectangle.
     *
     * @return The x-coordinate of the bottom-right corner.
     */
    public double getMaxX() {
        return minX + width;
    }

    /**
     * Calculates the y-coordinate of the bottom-right corner of the rectangle.
     *
     * @return The y-coordinate of the bottom-right corner.
     */
    public double getMaxY() {
        return minY + height;
    }

    /**
     * Checks if the rectangle contains the specified point.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return True if the rectangle contains the point, otherwise false.
     */
    public boolean contains(double x, double y) {
        return x >= minX && x <= getMaxX() && y >= minY && y <= getMaxY();
    }

    /**
     * Checks if the rectangle contains another rectangle.
     *
     * @param other The other rectangle to check.
     * @return True if this rectangle contains the other rectangle, otherwise false.
     */
    public boolean contains(SerializableRectangle other) {
        return minX <= other.getMinX() &&
                minY <= other.getMinY() &&
                getMaxX() >= other.getMaxX() &&
                getMaxY() >= other.getMaxY();
    }

    /**
     * Checks if this rectangle intersects with another rectangle.
     *
     * @param other The other rectangle to check for intersection.
     * @return True if this rectangle intersects with the other rectangle, otherwise false.
     */
    public boolean intersects(SerializableRectangle other) {
        return !(other.getMinX() > getMaxX() || other.getMaxX() < minX ||
                other.getMinY() > getMaxY() || other.getMaxY() < minY);
    }

    /**
     * Computes the intersection of this rectangle with another rectangle.
     *
     * @param other The other rectangle to intersect with.
     * @return A new rectangle representing the intersection, or null if there is no intersection.
     */
    public SerializableRectangle intersection(SerializableRectangle other) {
        double x1 = Math.max(minX, other.getMinX());
        double y1 = Math.max(minY, other.getMinY());
        double x2 = Math.min(getMaxX(), other.getMaxX());
        double y2 = Math.min(getMaxY(), other.getMaxY());

        if (x2 >= x1 && y2 >= y1) {
            return new SerializableRectangle(x1, y1, x2 - x1, y2 - y1);
        } else {
            return null; // No intersection
        }
    }

    /**
     * Computes the union of this rectangle with another rectangle.
     *
     * @param other The other rectangle to compute the union with.
     * @return A new rectangle representing the union of the two rectangles.
     */
    public SerializableRectangle union(SerializableRectangle other) {
        double x1 = Math.min(minX, other.getMinX());
        double y1 = Math.min(minY, other.getMinY());
        double x2 = Math.max(getMaxX(), other.getMaxX());
        double y2 = Math.max(getMaxY(), other.getMaxY());
        return new SerializableRectangle(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public String toString() {
        return "SerializableRectangle [minX=" + minX + ", minY=" + minY + ", width=" + width + ", height=" + height + "]";
    }
}
