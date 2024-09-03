package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a path on the map.
 */
public class Path extends Way {
    private final float diameter = 0.0005f;

    /**
     * Constructs a path with the specified coordinates.
     *
     * @param coords The coordinates of the path.
     */
    public Path(float[] coords) {
        super(coords);
    }

    /**
     * Draws the path on the graphics context.
     *
     * @param gc The graphics context on which to draw the path.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        drawOval(gc, coords[1], coords[0], this.diameter);
        drawOval(gc, coords[coords.length - 1], coords[coords.length - 2], this.diameter);
        drawLines(gc, 0.00015);
    }
}
