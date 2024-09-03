package dk.itu.MapOfDenmark.Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The SortedArray class represents an array that maintains its elements sorted by their IDs.
 */
public class SortedArray {
    private final List<Pair<Long, float[]>> elements = new ArrayList<>();
    private boolean sorted = true;

    /**
     * Adds a new element to the sorted array.
     *
     * @param id     The ID of the element.
     * @param coords The coordinates associated with the element.
     */
    public void add(long id, float[] coords) {
        Pair<Long, float[]> pair = new Pair<>(id, coords);
        elements.add(pair);
        sorted = false;
    }

    /**
     * Searches for an element by its ID using binary search and returns its coordinates.
     *
     * @param id The ID of the element to search for.
     * @return The coordinates of the element if found, otherwise null.
     */
    public float[] binarySearchGet(long id) {
        if (!sorted) {
            elements.sort(Comparator.comparingLong(Pair::getKey));
            sorted = true;
        }
        int low = 0;
        int high = elements.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            long midId = elements.get(mid).getKey();
            if (midId < id) {
                low = mid + 1;
            } else if (midId > id) {
                high = mid - 1;
            } else {
                return elements.get(mid).getValue();
            }
        }
        return null;
    }
}
