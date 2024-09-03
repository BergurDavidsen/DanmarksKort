package dk.itu.MapOfDenmark.Model.objects.abstracts;

import dk.itu.MapOfDenmark.Model.ColorManager;
import dk.itu.MapOfDenmark.Model.Graph.Vertex;
import dk.itu.MapOfDenmark.Model.Model;
import dk.itu.MapOfDenmark.View.MapUI;
import dk.itu.MapOfDenmark.utils.HelperMethods;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.itu.MapOfDenmark.Model.Graph.Edge;

/**
 * An abstract class representing a road, inherited from the Way class.
 */
public abstract class Road extends Way {
    /** Enumeration representing the type of road. */
    public enum RoadType{ PRIMARY, SECONDARY, TERTIARY, WALKWAY}

    /** The type of the road. */
    protected RoadType roadType;

    /** The maximum speed allowed on the road. */
    protected short maxSpeed;

    /** The list of edges associated with the road. */
    private List<Edge> edges = new ArrayList<>();

    /**
     * Constructs a road with the given coordinates.
     *
     * @param coords The coordinates of the road.
     */
    public Road(float[] coords){
        super(coords);
        this.roadType = RoadType.PRIMARY;
    }

    /**
     * Draws the road on the specified graphics context.
     *
     * @param gc The graphics context on which to draw the road.
     */
    @Override
    public void draw(GraphicsContext gc) {
        drawLines(gc, 0.00006);
    }

    /**
     * Sets the type of the road.
     *
     * @param roadType The type of the road.
     */
    public void setRoadType(RoadType roadType){
        this.roadType = roadType;
    }

    /**
     * Retrieves the type of the road.
     *
     * @return The type of the road.
     */
    public RoadType getRoadType(){
        if(roadType != null)
            return this.roadType;
        return RoadType.PRIMARY;
    }

    /**
     * Adds edges to the road based on the given nodes.
     *
     * @param nodes     The list of nodes defining the road.
     * @param oneway    Indicates if the road is one-way.
     * @param driveable Indicates if the road is drivable.
     * @param walkable  Indicates if the road is walkable.
     */
    public void addEdges(ArrayList<float[]> nodes, boolean oneway, boolean driveable, boolean walkable){
        Map<Vertex, Integer> node2Pos = Model.getGraph().getNode2Pos();
        for(int i = 0; i < nodes.size() - 1; i++){
            Vertex v1 = new Vertex(nodes.get(i));
            Vertex v2 = new Vertex(nodes.get(i+1));
            int u = node2Pos.get(v1);
            int v = node2Pos.get(v2);

            float distance = (float) HelperMethods.distance(
                    nodes.get(i)[0], nodes.get(i)[1],
                    nodes.get(i+1)[0], nodes.get(i+1)[1]
            );
            int speed = this.maxSpeed;
            edges.add(new Edge(u , v, distance, speed,this, oneway, driveable, walkable));
        }
    }

    /**
     * Retrieves the edges associated with the road.
     *
     * @return The list of edges associated with the road.
     */
    public List<Edge> getEdges(){
        return edges;
    }

    /**
     * Retrieves the color of the road.
     *
     * @return The color of the road.
     */
    @Override
    public Color getColor(){
        return ColorManager.getRoadColors(getRoadType());
    }

    /**
     * Determines if the road should be zoomed in based on its type.
     *
     * @return True if the road should be zoomed in, false otherwise.
     */
    @Override
    public boolean getZoom() {
        if(this.getRoadType()==RoadType.PRIMARY)
            return (MapUI.zoomFactor) > 5000;
        if(this.getRoadType()==RoadType.SECONDARY)
            return (MapUI.zoomFactor) > 10000;
        if(this.getRoadType()==RoadType.TERTIARY)
            return (MapUI.zoomFactor) > 2000;
        if (this.getRoadType() == RoadType.WALKWAY)
            return (MapUI.zoomFactor) > 4000;
        return false;
    }

    /**
     * Retrieves the class type of the road.
     *
     * @return The class type of the road.
     */
    @Override
    public Class<? extends Element> getClassType(){
        return Road.class;
    }
}
