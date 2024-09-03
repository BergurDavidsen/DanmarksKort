package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

/**
 * Represents a tertiary road in the map.
 */
public class TertiaryRoad extends Road {

    /**
     * Constructs a tertiary road with the specified coordinates.
     *
     * @param coords The coordinates of the tertiary road.
     */
    public TertiaryRoad(float[] coords) {
        super(coords);
        this.maxSpeed = 70;
        this.roadType = RoadType.TERTIARY;
    }
}
