package dk.itu.MapOfDenmark.Tests;


import dk.itu.MapOfDenmark.Model.SerializableRectangle;
import dk.itu.MapOfDenmark.Model.Trees.QuadTree.QuadTree;
import dk.itu.MapOfDenmark.Model.objects.WalkWay;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuadTreeTest {
    private QuadTree quadTree;

    @BeforeEach
    public void setUp() {
        SerializableRectangle boundary = new SerializableRectangle(0, 0, 100, 100);
        quadTree = new QuadTree(boundary, 4); // Assuming capacity of 4 elements per node
    }

    @Test
    public void testInsertAndQuery() {
        // Insert some elements into the quadtree
        Way way1 = new WalkWay(new float[]{0.0f,0.0f,10.0f,10.0f});
        Way way2 = new WalkWay(new float[]{20.0f, 20.0f, 30.0f, 30.0f});
        Way way3 = new WalkWay(new float[]{5.0f, 40.0f, 15.0f, 40.0f});
        quadTree.insert(way1);
        quadTree.insert(way2);
        quadTree.insert(way3);

        // Define a query range
        SerializableRectangle queryRange = new SerializableRectangle(10, 10, 20, 20);

        // Query the quadtree for elements within the range
        List<Way> result = quadTree.query(queryRange, new ArrayList<>());

        // Assert that the result contains the inserted elements
        assertTrue(result.contains(way1));
        assertTrue(result.contains(way2));
        assertFalse(result.contains(way3)); // way3 may not be in the query range
    }

}
