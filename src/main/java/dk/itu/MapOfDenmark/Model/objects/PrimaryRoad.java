package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

/**
 * Represents a primary road in the map.
 */
public class PrimaryRoad extends Road {

    /**
     * Constructs a primary road with the specified coordinates.
     *
     * @param coords The coordinates of the primary road.
     */
    public PrimaryRoad(float[] coords) {
        super(coords);
        this.maxSpeed = 90;
    }
}
