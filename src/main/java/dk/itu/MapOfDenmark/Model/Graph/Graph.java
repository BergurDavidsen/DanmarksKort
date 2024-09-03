package dk.itu.MapOfDenmark.Model.Graph;

import dk.itu.MapOfDenmark.Model.Model;
import dk.itu.MapOfDenmark.Model.objects.Path;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a graph structure to model a map of Denmark.
 */
public class Graph implements Serializable {
    /**
     * The number of vertices in the graph.
     */
    int vertices;

    /**
     * The adjacency list representing the graph.
     */
    List<List<Edge>> adjList;

    /**
     * Mapping from vertices to their positions in the graph.
     */
    Map<Vertex, Integer> node2Pos = new HashMap<>();

    /**
     * Set to track seen vertices during operations on the graph.
     */
    Set<Vertex> seen = new HashSet<>();

    /**
     * Current position in the graph.
     */
    int currentPos;

    /**
     * Count of edges in the graph.
     */
    int edgeCount;

    /**
     * The time of the latest search performed on the graph.
     */
    static double timeOfLatestSearch;

    /**
     * The distance of the latest search performed on the graph.
     */
    static double distanceOfLatestSearch;

    /**
     * Constructs an empty graph.
     */
    public Graph() {
        adjList = new ArrayList<>();
        currentPos = 0;
        edgeCount = 0;
    }

    /**
     * Adds edges of a road to the graph.
     *
     * @param road The road whose edges are to be added to the graph.
     */
    public void addEdge(Road road) {
        for (Edge e : road.getEdges()) {
            int u = e.getU();
            int v = e.next(u);

            adjList.get(v).add(e);
            if (!e.oneway) {
                adjList.get(u).add(e);
            }
            edgeCount++;
        }
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param node The coordinates of the vertex to be added.
     */
    public void addVertex(float[] node) {
        Vertex vertex = new Vertex(node);
        if (!seen.contains(vertex)) {
            adjList.add(new LinkedList<>());
            node2Pos.put(vertex, currentPos);
            currentPos++;
            seen.add(vertex);
        }
    }

    /**
     * Retrieves the adjacency list of edges for a given vertex.
     *
     * @param u The identifier of the vertex.
     * @return The adjacency list of edges for the vertex.
     */
    public Iterable<Edge> getAdjList(int u) {
        return adjList.get(u);
    }

    /**
     * Retrieves all edges in the graph.
     *
     * @return An iterable collection of all edges in the graph.
     */
    public Iterable<Edge> edges() {
        ArrayList<Edge> list = new ArrayList<>();
        for (int v = 0; v < vertices; v++) {
            int selfLoops = 0;
            for (Edge e : getAdjList(v)) {
                if (e.next(v) > v) {
                    list.add(e);
                } else if (e.next(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

    /**
     * Performs Dijkstra's algorithm to find the shortest path between two vertices.
     *
     * @param start The starting vertex of the path.
     * @param end   The ending vertex of the path.
     * @return A list of vertex identifiers representing the shortest path.
     */
    public List<Integer> dijkstra(Vertex start, Vertex end) {
        System.out.println("Start: " + node2Pos.get(start) + " End: " + node2Pos.get(end));
        return dijkstra(node2Pos.get(start), node2Pos.get(end));
    }

    /**
     * Reconstructs the shortest path from the parent map.
     *
     * @param parent The parent map obtained during the shortest path search.
     * @param start  The starting vertex index.
     * @param end    The ending vertex index.
     * @return A list of vertex indices representing the shortest path from start to end.
     */
    private List<Integer> reconstructPath(Map<Integer, Integer> parent, int start, int end) {
        List<Integer> pathIds = new ArrayList<>();
        int current = end;
        while (!(current == start)) {
            pathIds.add(current);
            current = parent.get(current);
        }

        pathIds.add(start);
        Collections.reverse(pathIds); // Reverse the list to get the correct order
        return pathIds;
    }

    public static boolean wantToDrive = true;

    /**
     * Retrieves the vertex corresponding to a given position in the graph.
     *
     * @param pos The position of the vertex in the graph.
     * @return The vertex corresponding to the position.
     */
    public Vertex getNodeFromPos(int pos) {
        for (Map.Entry<Vertex, Integer> entry : node2Pos.entrySet()) {
            if (entry.getValue() == pos) {
                return entry.getKey();
            }
        }
        return null;
    }
    /**
     * Performs Dijkstra's algorithm to find the shortest path between two vertices.
     *
     * @param start The starting vertex index of the path.
     * @param end   The ending vertex index of the path.
     * @return A list of vertex indices representing the shortest path.
     */
    public List<Integer> dijkstra(int start, int end) {
//        // Priority queue for Dijkstra's algorithm with distances as priority
        PriorityQueue<VertexNode> queue = new PriorityQueue<>();

//        // Map to store the shortest distances from the start node to each node
        Map<Integer, Double> times = new HashMap<>();
        // Map to store parent nodes for path reconstruction
        Map<Integer, Integer> parent = new HashMap<>();

        // Initialize distances
        for (int vertex : node2Pos.values()) {
            times.put(vertex, Double.MAX_VALUE);
        }
        times.put(start, 0.0);

        queue.add(new VertexNode(start, 0.0, 0) {
        });


        // Dijkstra's algorithm loop
        while (!queue.isEmpty()) {
            VertexNode current = queue.poll();
            int currentNode = current.pos;
            double currentDistance = current.getDistance();
            double currentTime = current.getTime();

            // Stop if the destination node is reached
            if (currentNode == end) {
                distanceOfLatestSearch = current.getDistance();
                timeOfLatestSearch = current.getTime();
                return reconstructPath(parent, start, end);
            }

            // Ignore if the current distance to this node is not the shortest
            if (currentTime > times.get(currentNode)) {
                continue;
            }
            // Relax neighboring nodes
            boolean isValidEdge;
            for (Edge edge : adjList.get(currentNode)) {
                isValidEdge = wantToDrive ? edge.driveable : edge.walkable;

                if (isValidEdge) {
                    //System.out.println("is oneway: " + edge.oneway);
                    int neighbor = edge.next(currentNode);
                    double timeThroughCurrent;
                    double distanceThroughCurrent = currentDistance + edge.distance;

                    if (wantToDrive) {
                        timeThroughCurrent = currentTime + edge.time;
                    } else {
                        timeThroughCurrent = currentTime + 3.6 * edge.distance / 5;
                    }
                    distanceThroughCurrent = currentDistance + edge.distance;
                    if (timeThroughCurrent < times.get(neighbor)) {
                        times.put(neighbor, timeThroughCurrent);
                        parent.put(neighbor, currentNode);
                        queue.add(new VertexNode(neighbor, timeThroughCurrent, distanceThroughCurrent));
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the mapping of vertices to their positions in the graph.
     *
     * @return The mapping of vertices to their positions.
     */
    public Map<Vertex, Integer> getNode2Pos() {
        return node2Pos;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        return s.toString();
    }


    public void addGraphToMap() {
        for (Vertex node : node2Pos.keySet()) {
            Model.add(new Vertex(node.getCoords()));
        }
    }

    /**
     * Executes the Dijkstra algorithm based on a list of selected points on the graph.
     *
     * @param click A list of coordinates representing selected points on the graph.
     * @return A string representation of the calculated path time and distance.
     * @throws NullPointerException If fewer than two points are selected.
     */
    public static String executeDijkstraAction(List<float[]> click) {
        if (click.size() < 2)
            throw new NullPointerException("Please select 2 point(s) on the graph!");

        Vertex v1 = new Vertex(click.get(0));
        Vertex v2 = new Vertex(click.get(1));

        List<Integer> dijkstraPath = Model.getGraph().dijkstra(v1, v2);

        if (dijkstraPath == null) {
            throw new NullPointerException("No path found");
        } else {
            float[] wayPoints = new float[dijkstraPath.size() * 2];

            Graph graph = Model.getGraph();

            for (int i = 0; i < dijkstraPath.size(); i += 2) {
                wayPoints[i] = graph.getNodeFromPos(dijkstraPath.get(i)).getCoords()[0];
                wayPoints[i + 1] = graph.getNodeFromPos(dijkstraPath.get(i)).getCoords()[1];
            }

            int count = 0;
            for (int i = 0; i < wayPoints.length; i++) {
                if (wayPoints[i] != 0) {
                    count++;
                }
            }
            float[] finalCoords = new float[count];
            count = 0;

            for (float wayPoint : wayPoints) {
                if (wayPoint != 0) {
                    finalCoords[count] = wayPoint;
                    count++;
                }
            }

            Model.add(new Path(finalCoords));

            if (getTimeOfLatestSearch() / 60 > 60) {
                if (distanceOfLatestSearch >= 1000) {
                    return "Path time: " + String.format("%.0f", getTimeOfLatestSearch() / 60 / 60) + " h " + String.format("%.0f", getTimeOfLatestSearch() % 60) + " min " + String.format("%.1f", getDistanceOfLatestSearch() / 1000) + "km\n";
                } else {
                    return "Path time: " + String.format("%.0f", getTimeOfLatestSearch() / 60 / 60) + " h " + String.format("%.0f", getTimeOfLatestSearch() % 60) + " min " + String.format("%.0f", getDistanceOfLatestSearch()) + "m\n";
                }
            } else {
                if (distanceOfLatestSearch >= 1000) {
                    return "Path time: " + String.format("%.0f", getTimeOfLatestSearch() / 60) + " min " + String.format("%.1f", getDistanceOfLatestSearch() / 1000) + "km\n";

                } else {
                    return "Path time: " + String.format("%.0f", getTimeOfLatestSearch() / 60) + " min " + String.format("%.0f", getDistanceOfLatestSearch()) + "m\n";
                }

            }
        }
    }
    /**
     * Retrieves the time of the latest search performed on the graph.
     *
     * @return The time of the latest search.
     */
    private static double getTimeOfLatestSearch() {
        return timeOfLatestSearch;
    }
    /**
     * Retrieves the distance of the latest search performed on the graph.
     *
     * @return The distance of the latest search.
     */
    private static double getDistanceOfLatestSearch() {
        return distanceOfLatestSearch;
    }

}