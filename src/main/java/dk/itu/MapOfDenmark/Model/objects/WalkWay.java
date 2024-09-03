package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a walkway in the map.
 */
public class WalkWay extends Road {

    /**
     * Constructs a walkway with the specified coordinates.
     *
     * @param coords The coordinates of the walkway.
     */
    public WalkWay(float[] coords) {
        super(coords);
        this.roadType = RoadType.WALKWAY;
    }

    /**
     * Draws the walkway on the graphics context.
     *
     * @param gc The graphics context on which to draw the walkway.
     */
    @Override
    public void draw(GraphicsContext gc) {
        drawLines(gc, 0.00002);
    }
}
