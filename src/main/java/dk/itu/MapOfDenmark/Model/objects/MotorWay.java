package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

/**
 * Represents a motorway (highway) on the map.
 */
public class MotorWay extends Road {

    /**
     * Constructs a MotorWay object with the specified coordinates.
     *
     * @param coords The coordinates of the motorway.
     */
    public MotorWay(float[] coords) {
        super(coords);
        this.maxSpeed = 130;
    }
}
