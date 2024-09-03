package dk.itu.MapOfDenmark.Model.Graph;

import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.scene.canvas.GraphicsContext;
import java.util.Objects;

/**
 * Represents a vertex in a graph, inherited from the abstract class Way.
 */
public class Vertex extends Way {
    /** The diameter of the vertex for drawing. */
    private final float diameter = 0.0002f;

    /**
     * Constructs a vertex with the given coordinates.
     *
     * @param points The coordinates of the vertex.
     */
    public Vertex(float[] points) {
        super(points);
    }

    /**
     * Draws the vertex on the specified graphics context.
     *
     * @param gc The graphics context on which to draw the vertex.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(this.getColor());
        drawOval(gc, coords[1], coords[0], diameter);
    }

    /**
     * Checks whether this vertex is equal to another object.
     *
     * @param obj The object to compare with this vertex.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex that = (Vertex) obj;
        return Float.compare(that.getCoords()[0], coords[0]) == 0 &&
                Float.compare(that.getCoords()[1], coords[1]) == 0;
    }

    /**
     * Computes the hash code of this vertex.
     *
     * @return The hash code of this vertex.
     */
    @Override
    public int hashCode() {
        return Objects.hash(coords[0], coords[1]);
    }
}
