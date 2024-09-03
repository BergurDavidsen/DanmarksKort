package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

/**
 * Represents a trunk road in the map.
 */
public class TrunkRoad extends Road {

    /**
     * Constructs a trunk road with the specified coordinates.
     *
     * @param coords The coordinates of the trunk road.
     */
    public TrunkRoad(float[] coords) {
        super(coords);
        this.maxSpeed = 110;
    }
}
