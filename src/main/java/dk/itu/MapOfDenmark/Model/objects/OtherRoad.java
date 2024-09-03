package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

/**
 * Represents another type of road on the map.
 */
public class OtherRoad extends Road {

    /**
     * Constructs an OtherRoad object with the specified coordinates.
     *
     * @param coords The coordinates of the other road.
     */
    public OtherRoad(float[] coords) {
        super(coords);
        this.maxSpeed = 50;
    }
}
