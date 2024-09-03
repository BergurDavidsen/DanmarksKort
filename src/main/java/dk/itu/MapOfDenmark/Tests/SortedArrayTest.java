package dk.itu.MapOfDenmark.Tests;

import dk.itu.MapOfDenmark.Model.SortedArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SortedArrayTest {

    @Test
    public void testBinarySearchGet() {
        SortedArray sortedArray = new SortedArray();
        sortedArray.add(1L, new float[]{1.0f, 2.0f, 3.0f});
        sortedArray.add(2L, new float[]{4.0f, 5.0f, 6.0f});
        sortedArray.add(3L, new float[]{7.0f, 8.0f, 9.0f});

        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f}, sortedArray.binarySearchGet(1L));
        assertArrayEquals(new float[]{4.0f, 5.0f, 6.0f}, sortedArray.binarySearchGet(2L));
        assertArrayEquals(new float[]{7.0f, 8.0f, 9.0f}, sortedArray.binarySearchGet(3L));
        assertNull(sortedArray.binarySearchGet(4L));
    }
}