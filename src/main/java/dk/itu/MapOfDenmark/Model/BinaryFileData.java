package dk.itu.MapOfDenmark.Model;

import dk.itu.MapOfDenmark.Model.Graph.Graph;
import dk.itu.MapOfDenmark.Model.Trees.QuadTree.QuadTree;
import dk.itu.MapOfDenmark.Model.Trees.Trie.CompressedTrie;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Element;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents binary file data containing information about the map.
 */
public class BinaryFileData implements ParsingElement, Serializable {
    private final Graph graph;
    private final CompressedTrie trie;
    private final Map<Class<? extends Element>, QuadTree> quadTreeMap;
    private final Model model;
    private final SerializableRectangle bounds;

    /**
     * Constructs binary file data with the specified parameters.
     *
     * @param graph       The graph representing the map.
     * @param quadTreeMap The quadtree map containing spatial data.
     * @param trie        The trie containing textual data.
     * @param model       The model containing additional map data.
     * @param bounds      The bounds of the map.
     */
    public BinaryFileData(Graph graph, Map<Class<? extends Element>, QuadTree> quadTreeMap, CompressedTrie trie, Model model, SerializableRectangle bounds) {
        this.graph = graph;
        this.trie = trie;
        this.quadTreeMap = quadTreeMap;
        this.model = model;
        this.bounds = bounds;
    }

    /**
     * Gets the graph representing the map.
     *
     * @return The graph.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Gets the quadtree map containing spatial data.
     *
     * @return The quadtree map.
     */
    public Map<Class<? extends Element>, QuadTree> getQuadTreeMap() {
        return quadTreeMap;
    }

    /**
     * Gets the trie containing textual data.
     *
     * @return The trie.
     */
    public CompressedTrie getTrie() {
        return trie;
    }

    /**
     * Gets the model containing additional map data.
     *
     * @return The model.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Gets the bounds of the map.
     *
     * @return The bounds.
     */
    public SerializableRectangle getBounds() {
        return bounds;
    }
}
