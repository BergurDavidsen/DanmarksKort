package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import dk.itu.MapOfDenmark.View.MapUI;

/**
 * Represents a building way object.
 */
public class Building extends Way {

    /**
     * Constructs a new Building object with the specified coordinates.
     *
     * @param coords The coordinates of the building.
     */
    public Building(float[] coords) {
        super(coords);
    }

    /**
     * Checks if the object should be drawn based on the zoom level.
     *
     * @return True if the object should be drawn, false otherwise.
     */
    @Override
    public boolean getZoom() {
        return (MapUI.zoomFactor) > 30000;
    }
}
