package dk.itu.MapOfDenmark.Model.Graph;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

import java.io.Serializable;

/**
 * Represents an edge in a graph, connecting two vertices.
 */
public class Edge implements Serializable {
    /** The identifier of the starting vertex of the edge. */
    int u;

    /** The identifier of the ending vertex of the edge. */
    int v;

    /** The road associated with the edge. */
    Road road;

    /** The distance between the vertices connected by the edge. */
    float distance;

    /** The time taken to traverse the edge. */
    float time;

    /** The speed limit on the edge. */
    float speed;

    /** Indicates whether the edge is drivable. */
    boolean driveable;

    /** Indicates whether the edge is walkable. */
    boolean walkable;

    /** Indicates whether the edge is one-way. */
    public boolean oneway;

    /**
     * Constructs an edge between two vertices with given properties.
     *
     * @param u         The identifier of the starting vertex.
     * @param v         The identifier of the ending vertex.
     * @param distance  The distance between the vertices.
     * @param speed     The speed limit on the edge.
     * @param road      The road associated with the edge.
     * @param oneway    Indicates whether the edge is one-way.
     * @param driveable Indicates whether the edge is drivable.
     * @param walkable  Indicates whether the edge is walkable.
     */
    public Edge(int u, int v, float distance, float speed, Road road, boolean oneway, boolean driveable, boolean walkable) {
        this.u = u;
        this.v = v;
        this.distance = distance;
        this.road = road;
        this.oneway = oneway;
        this.speed = speed;
        this.time = 3.6f * (distance / speed);
        this.driveable = driveable;
        this.walkable = walkable;
    }

    /**
     * Retrieves the identifier of the starting vertex of the edge.
     *
     * @return The identifier of the starting vertex.
     */
    public int getU() {
        return u;
    }

    /**
     * Retrieves the road associated with the edge.
     *
     * @return The road associated with the edge.
     */
    public Road getRoad() {
        return road;
    }

    /**
     * Determines the next vertex along the edge given a current vertex.
     *
     * @param u The current vertex.
     * @return The identifier of the next vertex.
     */
    public int next(int u) {
        if (u == this.u) {
            return this.v;
        } else {
            return this.u;
        }
    }
}
