package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

/**
 * Represents a secondary road in the map.
 */
public class SecondaryRoad extends Road {

    /**
     * Constructs a secondary road with the specified coordinates.
     *
     * @param coords The coordinates of the secondary road.
     */
    public SecondaryRoad(float[] coords) {
        super(coords);
        this.maxSpeed = 80;
        this.roadType = RoadType.SECONDARY;
    }
}
