package dk.itu.MapOfDenmark.Model.Trees.QuadTree;

import dk.itu.MapOfDenmark.Model.SerializableRectangle;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Element;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a quadtree data structure for spatial indexing of geographical elements.
 */
public class QuadTree implements Serializable {
    private final int QT_NODE_CAPACITY;
    private final SerializableRectangle boundary;
    private QTNode root;

    /**
     * Internal class representing a node in the quadtree.
     */
    private static class QTNode implements Serializable{
        SerializableRectangle bounds;
        QTNode NW, NE, SW, SE;
        Element[] elements;
        boolean divided = false;
        private int size;
        private int capacity;

        /**
         * Constructs a quadtree node with the specified bounds and capacity.
         *
         * @param bounds    The bounding rectangle of the node.
         * @param capacity  The maximum capacity of elements in the node.
         */
        public QTNode(SerializableRectangle bounds, int capacity) {
            this.bounds = bounds;
            this.capacity = capacity;
            elements = new Element[this.capacity];
            size = 0;
        }

        /**
         * Subdivides the node into four child nodes.
         */
        private void subDivide() {

            double xmin = bounds.getMinX();
            double ymin = bounds.getMinY();
            double halfWidth = bounds.getWidth() / 2.0;
            double halfHeight = bounds.getHeight() / 2.0;

            SerializableRectangle nw = new SerializableRectangle(xmin, ymin, halfWidth, halfHeight);
            SerializableRectangle ne = new SerializableRectangle(xmin + halfWidth, ymin, halfWidth, halfHeight);
            SerializableRectangle sw = new SerializableRectangle(xmin, ymin + halfHeight, halfWidth, halfHeight);
            SerializableRectangle se = new SerializableRectangle(xmin + halfWidth, ymin + halfHeight, halfWidth, halfHeight);

            NW = new QTNode(nw, this.capacity);
            NE = new QTNode(ne, this.capacity);
            SW = new QTNode(sw, this.capacity);
            SE = new QTNode(se, this.capacity);

            this.divided = true;


        }
        /**
         * Inserts a geographical element into the quadtree.
         *
         * @param n    The current node in the quadtree.
         * @param way  The geographical element to insert.
         * @return     True if insertion was successful, false otherwise.
         */
        public boolean insert(QTNode n, Way way) {
            float x = -1, y = -1;

            for (float f : way.getCoords()) {
                if(x == -1){
                    x = f;
                    continue;
                } else {
                    y = f;
                }

                if(y == -1)
                    throw new NullPointerException("null x or y in quadTree");


                if (!n.bounds.contains(x, y)) {
                    return false;
                }

                if (n.size < capacity) {
                    n.elements[size] = way;
                    size++;
                    return true;

                } else {
                    if (!n.divided) {
                        n.subDivide();
                    }

                    if (n.NW.insert(n.NW, way)) {

                        return true;
                    } else if (n.NE.insert(n.NE, way)) {

                        return true;
                    } else if (n.SW.insert(n.SW, way)) {

                        return true;
                    } else if (n.SE.insert(n.SE, way)) {

                        return true;
                    }
                }
            }
            return false;
        }


        /**
         * Queries the quadtree to find geographical elements within a specified range.
         *
         * @param range  The rectangular range to search within.
         * @param ways   A list to store the found geographical elements.
         * @return       A list of geographical elements within the specified range.
         */
        public List<Way> query(SerializableRectangle range, List<Way> ways) {
            if (!bounds.intersects(range)) {
                return ways; // No intersection, return empty list
            }

            // Check each element in this node
            for (Element element : elements) {
                if (element != null) {
                    Way way = (Way) element;
                    boolean containsWay = false;
                    for (int i = 0; i < way.getCoords().length; i+=2) {
                        float[] point = new float[]{way.getCoords()[i], way.getCoords()[i+1]};
                        if (range.contains(point[0], point[1])) {
                            containsWay = true;
                            break;
                        }

                    }
                    if (containsWay) {
                        ways.add(way);
                    }
                }
            }

            // If divided, recursively search children
            if (divided) {
                NW.query(range, ways);
                NE.query(range, ways);
                SW.query(range, ways);
                SE.query(range, ways);
            }
            return ways;
        }


        /**
         * Returns a string representation of the quadtree node.
         *
         * @return A string representation of the quadtree node.
         */
        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("boundary").append(this.bounds).append("\n");
            s.append("Elements: ").append(size).append(" ").append(Arrays.toString(elements)).append("\n");
            if (divided) {
                s.append("NW: ").append("\n");
                s.append(NW.toString());
                s.append("NE: ").append("\n");
                s.append(NE.toString());
                s.append("SW: ").append("\n");
                s.append(SW.toString());
                s.append("SE: ").append("\n");
                s.append(SE.toString());
            }
            return s.toString();
        }

    }

    /**
     * Constructs a quadtree with the specified boundary and capacity.
     *
     * @param boundary  The boundary rectangle of the quadtree.
     * @param capacity  The maximum capacity of elements in each quadtree node.
     */
    public QuadTree(SerializableRectangle boundary, int capacity) {
        this.boundary = boundary;

        QT_NODE_CAPACITY = capacity;

        root = new QTNode(boundary, QT_NODE_CAPACITY);
    }

    /**
     * Inserts a geographical element into the quadtree.
     *
     * @param element  The geographical element to insert.
     */
    public void insert(Way element) {
        this.root.insert(root, element);
    }

    /**
     * Queries the quadtree to find geographical elements within a specified range.
     *
     * @param range  The rectangular range to search within.
     * @param ways   A list to store the found geographical elements.
     * @return       A list of geographical elements within the specified range.
     */
    public List<Way> query(SerializableRectangle range, List<Way> ways) {
        return root.query(range, ways);
    }

    /**
     * Finds the nearest point in the quadtree to a specified target point.
     *
     * @param point  The target point.
     * @return       The nearest point found in the quadtree.
     */
    public float[] nearestPoint(double[] point) {
        double[] d = nearestPoint(root, point, Double.MAX_VALUE, null);
        return new float[]{(float) d[0], (float) d[1]};
    }

    private double[] nearestPoint(QTNode node, double[] targetPoint, double currentMinDistance, double[] closestPoint) {
        if (node == null) {
            return closestPoint;
        }

        // Calculate the distance from the target point to the bounding box of the node
        double distanceToBoundingBox = calculateDistanceToBoundingBox(node.bounds, targetPoint);

        // If the distance to the bounding box is less than the current minimum distance,
        // we might find a closer point within this node or its children
        if (distanceToBoundingBox < currentMinDistance) {
            // Check if this node is a leaf node
            if (!node.divided) {
                // Search for the closest point among the points in this leaf node
                double minDistance = currentMinDistance;
                for (Element element : node.elements) {
                    if (element != null) {
                        Way w = (Way) element;
                        for (int i = 0; i < w.getCoords().length - 1; i += 2) {

                            double[] p = new double[]{w.getCoords()[i], w.getCoords()[i + 1]};

                            double dist = calculateDistance(p, targetPoint);
                            if (dist < minDistance) {
                                minDistance = dist;
                                closestPoint = p;
                            }
                        }
                    }
                }
                return closestPoint;
            } else {
                // Recursively search the child nodes for a closer point
                // Order the search by starting with the child closest to the target point
                QTNode[] childNodes = orderChildNodes(node, targetPoint);
                for (QTNode child : childNodes) {
                    closestPoint = nearestPoint(child, targetPoint, currentMinDistance, closestPoint);
                    if (closestPoint != null) {
                        currentMinDistance = calculateDistance(closestPoint, targetPoint);
                    }
                }
                return closestPoint;
            }
        } else {
            // If the distance to the bounding box is greater than the current minimum distance,
            // return the current closest point
            return closestPoint;
        }
    }

    /**
     * Calculates the distance between two points.
     *
     * @param p1  The first point.
     * @param p2  The second point.
     * @return    The distance between the two points.
     */
    private double calculateDistance(double[] p1, double[] p2) {
        return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
    }

    /**
     * Calculates the distance from a point to the bounding box of a rectangle.
     *
     * @param boundingBox  The bounding box rectangle.
     * @param point        The point.
     * @return             The distance from the point to the bounding box.
     */
    private double calculateDistanceToBoundingBox(SerializableRectangle boundingBox, double[] point) {
        double dx = Math.max(Math.max(boundingBox.getMinX() - point[0], 0), point[0] - boundingBox.getMaxX());
        double dy = Math.max(Math.max(boundingBox.getMinY() - point[1], 0), point[1] - boundingBox.getMaxY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Orders child nodes based on their proximity to a target point.
     *
     * @param node         The parent node.
     * @param targetPoint  The target point.
     * @return             An array of child nodes ordered by proximity to the target point.
     */
    private QTNode[] orderChildNodes(QTNode node, double[] targetPoint) {
        double xMid = node.bounds.getMinX() + (node.bounds.getWidth() / 2);
        double yMid = node.bounds.getMinY() + (node.bounds.getHeight() / 2);

        QTNode[] childNodes = {node.NW, node.NE, node.SW, node.SE};
        Arrays.sort(childNodes, Comparator.comparingDouble(childNode -> {
            double xMidChild = childNode.bounds.getMinX() + (childNode.bounds.getWidth() / 2);
            double yMidChild = childNode.bounds.getMinY() + (childNode.bounds.getHeight() / 2);
            return Math.sqrt(Math.pow(xMidChild - targetPoint[0], 2) + Math.pow(yMidChild - targetPoint[1], 2));
        }));
        return childNodes;
    }



    /**
     * Clears the quadtree, removing all elements.
     */
    public void clear(){
        root = new QTNode(boundary, QT_NODE_CAPACITY);
    }

    @Override
    public String toString() {
        System.out.println("root:");
        return root.toString();
    }
}
