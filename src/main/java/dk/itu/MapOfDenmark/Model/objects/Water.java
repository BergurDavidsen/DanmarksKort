package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;

/**
 * Represents a body of water in the map.
 */
public class Water extends Way {

    /**
     * Constructs a body of water with the specified coordinates.
     *
     * @param coords The coordinates of the body of water.
     */
    public Water(float[] coords) {
        super(coords);
    }

}
