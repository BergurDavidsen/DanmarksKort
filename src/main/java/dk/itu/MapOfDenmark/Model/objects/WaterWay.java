package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import dk.itu.MapOfDenmark.View.MapUI;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a waterway in the map.
 */
public class WaterWay extends Way {

    /**
     * Constructs a waterway with the specified coordinates.
     *
     * @param coords The coordinates of the waterway.
     */
    public WaterWay(float[] coords) {
        super(coords);
    }

    /**
     * Draws the waterway on the graphics context.
     *
     * @param gc The graphics context to draw on.
     */
    @Override
    public void draw(GraphicsContext gc) {
        drawLines(gc, 0.0001);
    }

    /**
     * Checks if the zoom factor of the map UI is greater than 25000.
     *
     * @return True if the zoom factor is greater than 25000, false otherwise.
     */
    public boolean getZoom() {
        return (MapUI.zoomFactor) > 15000;
    }

}
