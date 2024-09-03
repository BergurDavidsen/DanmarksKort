package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a point of interest in the map.
 */
public class PointOfInterest extends Way {
    private final float diameter = 0.0004f;

    /**
     * Constructs a point of interest with the specified coordinates.
     *
     * @param coords The coordinates of the point of interest.
     */
    public PointOfInterest(float[] coords) {
        super(coords);
    }

    /**
     * Draws the point of interest on the graphics context.
     *
     * @param gc The graphics context on which to draw the point of interest.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(this.getColor());
        drawOval(gc, coords[1], coords[0], diameter);
    }
}
