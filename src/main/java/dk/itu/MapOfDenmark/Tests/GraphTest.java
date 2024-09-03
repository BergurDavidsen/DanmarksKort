package dk.itu.MapOfDenmark.Tests;

import dk.itu.MapOfDenmark.Model.Graph.Graph;
import dk.itu.MapOfDenmark.Model.Model;
import dk.itu.MapOfDenmark.Model.objects.MotorWay;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTest {
    private Graph graph;
    @BeforeEach
    public void setUp() throws XMLStreamException, IOException, ClassNotFoundException {
        graph = new Graph();
    }

    @AfterEach
    public void tearDown(){
        graph = null;
    }

    @Test
    public void testGraphInitialization() {
        //assertEquals(0, graph.getVertexCount());
        //assertEquals(0, graph.getEdgeCount());
    }

    @Test
    public void testAddingVertices() {
        graph.addVertex(new float[]{0.0f, 0.0f});
        graph.addVertex(new float[]{1.0f, 1.0f});
        //assertEquals(2, graph.getVertexCount());
    }

    @Test
    public void testAddingEdges() throws XMLStreamException, IOException {

        float[] coords = {
            12.3456f, 56.7890f,  // First vertex (longitude, latitude)
            12.3457f, 56.7891f,  // Second vertex (longitude, latitude)
        };
        // Create a Road object representing a road segment
        // Define coordinates for a motorway segment
        MotorWay motorway = new MotorWay(coords);
        ArrayList<float[]> nodes = new ArrayList<>();
        nodes.add(new float[] {coords[0], coords[1]}); // Coordinates for node 1
        nodes.add(new float[] {coords[2], coords[3]}); // Coordinates for node 2
        // Add more nodes as needed
        motorway.addEdges(nodes, true, true, false);


        // Add the road to the graph
        graph.addEdge(motorway);
        //assertEquals(1, graph.getEdgeCount());
    }
}
