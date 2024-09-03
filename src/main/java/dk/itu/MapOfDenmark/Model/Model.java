package dk.itu.MapOfDenmark.Model;

import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

import javax.xml.stream.*;

import dk.itu.MapOfDenmark.App;
import dk.itu.MapOfDenmark.Model.Graph.Graph;
import dk.itu.MapOfDenmark.Model.Graph.Vertex;
import dk.itu.MapOfDenmark.Model.Trees.QuadTree.QuadTree;
import dk.itu.MapOfDenmark.Model.Trees.Trie.CompressedTrie;
import dk.itu.MapOfDenmark.Model.objects.*;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Element;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.scene.canvas.GraphicsContext;

/**
 * The Model class represents the data model of the application, which includes parsing OpenStreetMap (OSM) data,
 * managing the graph structure, handling serialization, and drawing elements on the map.
 */
public class Model  implements ParsingElement, Serializable {
    /** A map of class types to QuadTrees for spatial indexing. */
    public static Map<Class<? extends Element>, QuadTree> quadTreeMap = new HashMap<>();

    /** A map of key-value pairs parsed from OSM data. */
    private Map<String, String> keyValue_map = new HashMap<>();

    /** Indicates if a road allows one-way traffic. */
    private boolean oneway = false;

    /** The bounding rectangle of the map. */
    public SerializableRectangle bounds;

    /** Set of reference IDs. */
    Set<Long> refIds;

    /** Trie data structure for efficient string search. */
    static CompressedTrie trie = new CompressedTrie();

    /** The graph representing the road network. */
    private static Graph graph = new Graph();

    /** Minimum latitude of the map. */
    public double minlat;

    /** Maximum latitude of the map. */
    public  double maxlat;

    /** Minimum longitude of the map. */
    public  double minlon;

    /** Maximum longitude of the map. */
    public  double maxlon;

    /** Input stream for reading data. */
    public static InputStream inputStream;

    static {
        try {
            inputStream = new FileInputStream(App.path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads data from a file.
     *
     * @param filename The name of the file to load.
     * @return A ParsingElement object representing the loaded data.
     * @throws IOException              If an I/O error occurs.
     * @throws ClassNotFoundException   If the class of a serialized object cannot be found.
     * @throws XMLStreamException       If an error occurs during XML parsing.
     * @throws FactoryConfigurationError If an error occurs during factory configuration.
     */
    public static ParsingElement load(String filename) throws IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return new BinaryFileData(
                        (Graph) in.readObject(),
                        (Map<Class<? extends Element>, QuadTree>) in.readObject(),
                        (CompressedTrie) in.readObject(),
                        (Model) in.readObject(),
                        (SerializableRectangle) in.readObject()
                );
            }
        }
        return new Model(filename);
    }

    /**
     * Constructs a Model object and parses OSM data from the specified file.
     *
     * @param filename The name of the file containing OSM data.
     * @throws XMLStreamException       If an error occurs during XML parsing.
     * @throws FactoryConfigurationError If an error occurs during factory configuration.
     * @throws IOException              If an I/O error occurs.
     */
    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        }
        save(filename + ".obj");
    }

    /**
     * Saves the Model object to a file.
     *
     * @param filename The name of the file to save to.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(filename)))) {
            out.writeObject(graph);
            out.writeObject(quadTreeMap);
            out.writeObject(trie);
            out.writeObject(this);
            out.writeObject(this.bounds);
        }

    }

    /**
     * Parses OSM data from a ZIP file.
     *
     * @param filename The name of the ZIP file containing OSM data.
     * @throws IOException              If an I/O error occurs.
     * @throws XMLStreamException       If an error occurs during XML parsing.
     * @throws FactoryConfigurationError If an error occurs during factory configuration.
     */
    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);
    }

    /**
     * Parses OSM data from a file.
     *
     * @param filename The name of the file containing OSM data.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws XMLStreamException    If an error occurs during XML parsing.
     * @throws FactoryConfigurationError If an error occurs during factory configuration.
     */
    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseOSM(new FileInputStream(filename));
    }

    /**
     * Parses OSM data from an input stream.
     *
     * @param inputStream The input stream containing OSM data.
     * @throws XMLStreamException    If an error occurs during XML parsing.
     */
    private void parseOSM(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        SortedArray id2node;
        float lat = 0;
        float lon = 0;
        boolean street = false;
        boolean housenumber = false;
        boolean postcode = false;
        boolean city = false;

        id2node = new SortedArray();

        var way = new ArrayList<float[]>();

        Map<String, String> wayAttributes = new HashMap<>();
        boolean driveable = false;
        boolean walkable = false;

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = reader.getLocalName();

                    switch (name) {
                        case "bounds":
                            minlat = Double.parseDouble(reader.getAttributeValue(null, "minlat"));
                            maxlat = Double.parseDouble(reader.getAttributeValue(null, "maxlat"));
                            minlon = Double.parseDouble(reader.getAttributeValue(null, "minlon"));
                            maxlon = Double.parseDouble(reader.getAttributeValue(null, "maxlon"));
                            buildTrees(new SerializableRectangle(minlat, minlon, maxlat, maxlon));

                            break;
                        case "node":
                            street = false;
                            housenumber = false;
                            postcode = false;
                            city = false;

                            keyValue_map.clear();
                            long id = Long.parseLong(reader.getAttributeValue(null, "id"));
                            lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
                            lon = Float.parseFloat(reader.getAttributeValue(null, "lon"));

                            id2node.add(id, new float[]{lat, lon});


                            break;
                        case "way":
                            driveable = false;
                            walkable = false;
                            oneway = false;
                            keyValue_map.clear();
                            wayAttributes.clear();
                            way.clear();

                            break;
                        case "nd":
                            long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));

                            way.add(id2node.binarySearchGet(ref));
                            break;
                        case "tag":
                            String k = reader.getAttributeValue(null, "k");
                            String v = reader.getAttributeValue(null, "v");
                            keyValue_map.put(k, v);
                            wayAttributes.put("type", getWayType(k, v));
                            if (walkAble(k, v)) {
                                walkable = true;
                            }
                            if (driveAble(k, v)) {
                                driveable = true;
                                if (k.equals("highway")) {
                                    wayAttributes.put("type", "road");
                                }
                            }
                            if (k.equals("oneway") && v.equals("yes")) {
                                oneway = true;
                            }

                            switch (k) {
                                case "addr:street" -> street = true;
                                case "addr:housenumber" -> housenumber = true;
                                case "addr:postcode" -> postcode = true;
                                case "addr:city" -> city = true;
                            }


                            if (street && housenumber && postcode && city) {
                                trie.insert(
                                        keyValue_map.get("addr:street") + " " +
                                                keyValue_map.get("addr:housenumber") + ", " +
                                                keyValue_map.get("addr:postcode") + " " +
                                                keyValue_map.get("addr:city"), new float[]{lat, lon}

                                );
                            }
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:

                    String endName = reader.getLocalName();
                    if (endName.equals("way")) {
                        float[] wayArr = new float[way.size() * 2];
                        if (!way.isEmpty()) {
                            for (int i = 0; i < way.size(); i++) {
                                wayArr[i << 1] = way.get(i)[0];
                                wayArr[(i << 1) + 1] = way.get(i)[1];
                            }
                        }


                        String wayType = wayAttributes.get("type");

                        if (keyValue_map.containsKey("landuse")) {
                            if (keyValue_map.get("landuse").equals("forest")) {
                                wayType = "grass";
                            } else if (keyValue_map.get("landuse").equals("farmland") || keyValue_map.get("landuse").equals("farmyard") || keyValue_map.get("landuse").equals("orchard") || keyValue_map.get("landuse").equals("vineyard") || keyValue_map.get("landuse").equals("greenhouse_horticulture") || keyValue_map.get("landuse").equals("plant_nursery") || keyValue_map.get("landuse").equals("allotments") || keyValue_map.get("landuse").equals("vineyard") || keyValue_map.get("landuse").equals("farm")) {
                                wayType = "grass";
                            } else if (keyValue_map.get("landuse").equals("meadow")) {
                                wayType = "grass";
                            }
                        }


                        if (keyValue_map.containsKey("natural")) {
                            if (keyValue_map.get("natural").equals("coastline")) {
                                //layerManagement.add(new Coast(idWay, nodeIds), way);
                                wayType = "coast";
                            } else if (keyValue_map.get("natural").equals("water")) {
                                wayType = "water";
                            }
                        } else if (keyValue_map.containsKey("waterway")) {
                            wayType = "waterway";
                        } else if (keyValue_map.containsKey("leisure")) {
                            String leisureValue = keyValue_map.get("leisure");
                            if (leisureValue != null && !leisureValue.equals("nature_reserve")) {
                                wayType = "grass";
                            }
                        }


                        if (keyValue_map.containsKey("highway")) {
                            Road road = switch (keyValue_map.get("highway")) {
                                case "motorway" -> new MotorWay(wayArr);
                                case "trunk" -> new TrunkRoad(wayArr);
                                case "primary" -> new PrimaryRoad(wayArr);
                                case "secondary" -> new SecondaryRoad(wayArr);
                                case "tertiary" -> new TertiaryRoad(wayArr);
                                case "footway", "path", "steps" -> new WalkWay(wayArr);
                                default -> new OtherRoad(wayArr);
                            };

                            for (float[] p : way) {
                                graph.addVertex(p);
                            }

                            road.addEdges(way, oneway, driveable, walkable);

                            graph.addEdge(road);

                            add(road);
                        }


                        if (wayType == null) {
                            continue;
                        }

                        switch (wayType) {
                            case "coast" -> add(new Coast(wayArr));
                            case "water" -> add(new Water(wayArr));
                            case "building" -> add(new Building(wayArr));
                            case "grass" -> add(new Grass(wayArr));
                            case "ground" -> add(new Island(wayArr));
                            case "waterway" -> add(new WaterWay(wayArr));
                        }
                    }
                    break;
            }
        }

        id2node = null;
        refIds = null;
        System.gc();
    }

    /**
     * Adds an element to the QuadTree for spatial indexing.
     *
     * @param element The element to add.
     */
    public static void add (Element element){
        if (element instanceof Way way && !(element instanceof OtherWay || element instanceof Island)) {
            quadTreeMap.get(way.getClassType()).insert(way);
        }
    }

    /**
     * Only returns roads where cars can drive on
     *
     * @param k
     * @param v
     * @return true, if can drive.
     */
    private static boolean isWater (String k, String v){
        return k.equals("natural") && v.equals("water");
    }

    private static boolean isIsland (String k, String v){
        return k.equals("place") && (v.equals("island") || v.equals("islet"));
    }

    private static String getWayType (String k, String v){
        if (isRoad(k, v)) {
            return "road";
        } else if (isWater(k, v)) {
            return "water";
        } else if (k.equals("building")) {
            return "building";
        } else if (v.equals("coastline")) {
            return "coastline";
        } else if (k.equals("leisure") || k.equals("landuse")) {
            return "grass";
        } else if (isIsland(k, v) || k.equals("area") || v.equals("grass")) {
            return "ground";
        } else if (k.equals("type") && v.equals("multipolygon")) {
            return "multipolygon";
        }
        return "Unknown way type: " + v;
    }

    private static boolean driveAble (String k, String v){
        return k.equals("highway") && (v.equals("motorway") || v.equals("trunk")
                || v.equals("primary") || v.equals("secondary") || v.equals("tertiary")
                || v.equals("road") || v.equals("residential") || v.equals("motor_vehicle")
                || v.equals("unclassified") || v.equals("track") || v.equals("service"));
    }

    private static boolean walkAble (String k, String v){
        return (!v.equals("motorway"));
    }


    private static boolean isRoad (String k, String v){
        return (k.equals("highway") || k.equals("cycleway") || k.equals("footway") || k.equals("busway")) ||
                (v.equals("primary") || v.equals("secondary") || v.equals("tertiary") || v.equals("residential") ||
                        v.equals("motor_vehicle") || v.equals("unclassified") || v.equals("path") || v.equals("track")) || k.equals("route");
    }

    public static Graph getGraph() {
        if(graph == null) {
            Model.graph = new Graph();
        }
        return graph;
    }

    /**
     * Resets the model by creating a new graph, clearing the QuadTree map, and clearing the trie.
     * This method also performs garbage collection to free up memory.
     */
    public void hardReset () {
        graph = new Graph();
        quadTreeMap = new HashMap<>();
        getTrie().clear();
        System.gc();

    }

    /**
     * Retrieves the compressed trie used in the model.
     *
     * @return The compressed trie.
     */
    public CompressedTrie getTrie() {
        return trie;
    }

    /**
     * Builds various QuadTrees for spatial indexing based on the specified rectangle.
     *
     * @param rect The rectangle defining the bounds for building the QuadTrees.
     */
    public void buildTrees (SerializableRectangle rect){
        // Building QuadTree
        quadTreeMap.put(Building.class, new QuadTree(rect, 100));

        // Road QuadTree
        quadTreeMap.put(Road.class, new QuadTree(rect, 100));

        // Water QuadTree
        quadTreeMap.put(Water.class, new QuadTree(rect, 80));

        // Grass QuadTree
        quadTreeMap.put(Grass.class, new QuadTree(rect, 70));

        // Coast QuadTree
        quadTreeMap.put(Coast.class, new QuadTree(rect, 55));

        // Vertex QuadTree
        quadTreeMap.put(Vertex.class, new QuadTree(rect, 40));

        // Edge QuadTree
        quadTreeMap.put(WaterWay.class, new QuadTree(rect, 100));

        quadTreeMap.put(WalkWay.class, new QuadTree(rect, 100));
        // Path QuadTree
        quadTreeMap.put(Path.class, new QuadTree(rect, 20));

        quadTreeMap.put(PointOfInterest.class, new QuadTree(rect, 20));
    }

    /**
     * Draws elements on the map using a graphics context and a bounding rectangle.
     *
     * @param gc   The graphics context.
     * @param rect The bounding rectangle.
     */
    public static void draw (GraphicsContext gc, SerializableRectangle rect){
        List<Way> roads = quadTreeMap.get(Road.class).query(rect, new ArrayList<>());
        List<Way> buildings = quadTreeMap.get(Building.class).query(rect, new ArrayList<>());
        List<Way> waters = quadTreeMap.get(Water.class).query(rect, new ArrayList<>());
        List<Way> waterWays = quadTreeMap.get(WaterWay.class).query(rect, new ArrayList<>());
        List<Way> grasses = quadTreeMap.get(Grass.class).query(rect, new ArrayList<>());
        List<Way> coasts = quadTreeMap.get(Coast.class).query(rect, new ArrayList<>());
        List<Way> paths = quadTreeMap.get(Path.class).query(rect, new ArrayList<>());
        List<Way> pointsOfInterest = quadTreeMap.get(PointOfInterest.class).query(rect, new ArrayList<>());

        for (Way way : grasses) {
            way.draw(gc);
        }
        for (Way way : waters) {
            way.draw(gc);
        }
        for (Way way : coasts) {
            way.draw(gc);
        }
        for (Way way : buildings) {
            if (toDraw(way)) {
            way.draw(gc);
            }
        }
        for (Way way : waterWays) {
            if (toDraw(way)){
            way.draw(gc);
            }
        }
        for (Way way : roads) {
            if (toDraw(way)) {
            way.draw(gc);
            }
        }
        if (pointsOfInterest != null) {
            for (Way way : pointsOfInterest) {
                way.draw(gc);
            }
        }
        if (paths != null) {
            for (Way way : paths) {
                way.draw(gc);
            }
        }
    }
    /**
     * Draws paths on the map using a graphics context and a bounding rectangle.
     *
     * @param gc   The graphics context.
     * @param rect The bounding rectangle.
     */
    public static void drawPaths (GraphicsContext gc, SerializableRectangle rect){

        List<Way> paths = quadTreeMap.get(Path.class).query(rect, new ArrayList<>());
        List<Way> edges = quadTreeMap.get(Road.class).query(rect, new ArrayList<>());
        List<Way> vertices = quadTreeMap.get(Vertex.class).query(rect, new ArrayList<>());
        List<Way> pointsOfInterest = quadTreeMap.get(PointOfInterest.class).query(rect, new ArrayList<>());

        for (Way way : edges) {
            way.draw(gc);
        }
        for (Way way : vertices) {
            way.draw(gc);
        }
        for (Way way : paths) {
            way.draw(gc);
        }
        for (Way way : pointsOfInterest) {
            way.draw(gc);
        }
    }

    /**
     * Determines whether an element should be drawn based on zoom level.
     *
     * @param element The element to check.
     * @return True if the element should be drawn, false otherwise.
     */
    private static boolean toDraw(Element element){
        if (element instanceof Drawn) {
            return ((Drawn) element).getZoom();
        }
        return false;
    }

    public void setGraph(Graph graph) {
        Model.graph = graph;
    }

    public void setTrie(CompressedTrie trie) {
        Model.trie = trie;
    }

    public void setQuadTreeMap(Map<Class<? extends Element>, QuadTree> quadTreeMap) {
        Model.quadTreeMap = quadTreeMap;
    }

    public void setBounds(SerializableRectangle bounds) {
        this.bounds = bounds;
    }
}

