package dk.itu.MapOfDenmark.Model.Graph;

/**
 * Represents a node in Dijkstra's algorithm for graph traversal.
 * This class is package-private, used internally by the Graph class.
 */
class VertexNode implements Comparable<VertexNode> {
    /** The position of the node in the graph. */
    int pos;

    /** The time taken to reach this node from the start node. */
    double time;

    /** The distance covered to reach this node from the start node. */
    double distance;

    /**
     * Constructs a vertex node with the specified position, time, and distance.
     *
     * @param pos      The position of the node in the graph.
     * @param time     The time taken to reach this node from the start node.
     * @param distance The distance covered to reach this node from the start node.
     */
    VertexNode(int pos, double time, double distance) {
        this.pos = pos;
        this.time = time;
        this.distance = distance;
    }

    /**
     * Compares this vertex node with another based on the time taken.
     *
     * @param other The other vertex node to compare with.
     * @return A negative integer, zero, or a positive integer if this node is less than, equal to,
     *         or greater than the other node in terms of time taken, respectively.
     */
    @Override
    public int compareTo(VertexNode other) {
        // Compare based on time
        return Double.compare(this.time, other.time);
    }

    /**
     * Retrieves the time taken to reach this node from the start node.
     *
     * @return The time taken to reach this node.
     */
    double getTime() {
        return time;
    }

    /**
     * Retrieves the distance covered to reach this node from the start node.
     *
     * @return The distance covered to reach this node.
     */
    double getDistance() {
        return distance;
    }
}
