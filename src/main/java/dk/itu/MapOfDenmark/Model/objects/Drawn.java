package dk.itu.MapOfDenmark.Model.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Interface for objects that can be drawn on a canvas.
 */
public interface Drawn {

    /**
     * Retrieves the color of the object.
     *
     * @return The color of the object.
     */
    Color getColor();

    /**
     * Draws the object on the specified graphics context.
     *
     * @param gc The graphics context on which to draw the object.
     */
    void draw(GraphicsContext gc);

    /**
     * Checks if the object should be drawn based on the zoom level.
     *
     * @return True if the object should be drawn, false otherwise.
     */
    boolean getZoom();
}
