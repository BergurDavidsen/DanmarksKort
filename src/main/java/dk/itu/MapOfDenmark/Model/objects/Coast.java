package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a coast way object.
 */
public class Coast extends Way {

    /**
     * Constructs a new Coast object with the specified coordinates.
     *
     * @param coords The coordinates of the coast.
     */
    public Coast(float[] coords) {
        super(coords);
    }

    /**
     * Draws the coast on the specified graphics context.
     *
     * @param gc The graphics context on which to draw the coast.
     */
    @Override
    public void draw(GraphicsContext gc) {
        drawLines(gc, 0.00010);
    }
}
