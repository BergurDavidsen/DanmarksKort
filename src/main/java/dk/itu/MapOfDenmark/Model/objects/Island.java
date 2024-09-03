package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;

/**
 * Represents an island on the map.
 */
public class Island extends Way {

    /**
     * Constructs an Island object with the specified coordinates.
     *
     * @param coords The coordinates of the island.
     */
    public Island(float[] coords) {
        super(coords);
    }
}
