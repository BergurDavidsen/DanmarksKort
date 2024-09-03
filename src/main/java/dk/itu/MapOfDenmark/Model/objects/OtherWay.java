package dk.itu.MapOfDenmark.Model.objects;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;

/**
 * Represents another type of way on the map.
 */
public class OtherWay extends Way {

    /**
     * Constructs an OtherWay object with the specified coordinates.
     *
     * @param coords The coordinates of the other way.
     */
    public OtherWay(float[] coords) {
        super(coords);
    }
}
