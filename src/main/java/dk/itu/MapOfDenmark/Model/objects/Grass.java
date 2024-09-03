package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;

/**
 * Represents a grass area on the map.
 */
public class Grass extends Way {

    /**
     * Constructs a Grass object with the specified coordinates.
     *
     * @param coords The coordinates of the grass area.
     */
    public Grass(float[] coords) {
        super(coords);
    }
}
