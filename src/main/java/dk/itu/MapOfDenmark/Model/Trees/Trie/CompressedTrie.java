package dk.itu.MapOfDenmark.Model.Trees.Trie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A compressed trie data structure for storing and searching strings.
 */
public class CompressedTrie implements Serializable {

    private static final long serialVersionUID = 1L;
    private Node root;
    private int size;

    /**
     * Constructs an empty compressed trie.
     */
    public CompressedTrie() {
        root = new Node();
        size = 0;
    }

    /**
     * Inserts a word with its associated coordinates into the trie.
     *
     * @param key    The word to insert.
     * @param coords The coordinates associated with the word.
     */
    public void insert(String key, float[] coords) {
        Node current = root;
        key = key.toLowerCase();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!current.children.containsKey(c)) {
                current.children.put(c, new Node());
            }
            current = current.children.get(c);
        }
        if (!current.isEndOfWord) {
            current.isEndOfWord = true;
            current.setCoords(coords);
            size++;
        }
    }

    /**
     * Searches for a word in the trie and returns its associated coordinates.
     *
     * @param key The word to search for.
     * @return The coordinates associated with the word, or null if the word is not found.
     */
    public float[] search(String key) {
        Node current = root;
        key = key.toLowerCase();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!current.children.containsKey(c)) {
                return null;
            }
            current = current.children.get(c);
        }
        return current.coords;
    }

    /**
     * Searches for words with the given prefix in the trie.
     *
     * @param prefix The prefix to search for.
     * @return A list of words with the given prefix.
     */
    public List<String> searchPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        Node current = root;
        prefix = prefix.toLowerCase();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!current.children.containsKey(c)) {
                return result;
            }
            current = current.children.get(c);
        }
        AtomicInteger counter = new AtomicInteger();
        searchPrefix(current, prefix, result, counter);
        return result;
    }

    /**
     * Helper method for recursive prefix search.
     *
     * @param current The current trie node.
     * @param prefix  The prefix being searched.
     * @param result  The list of words found with the prefix.
     * @param counter The count of words found.
     */
    private void searchPrefix(Node current, String prefix, List<String> result, AtomicInteger counter) {
        if (current.isEndOfWord) {
            result.add(prefix);
            counter.incrementAndGet();
        }
        if (counter.get() >= 10) {
            return;
        }
        for (char c : current.children.keySet()) {
            searchPrefix(current.children.get(c), prefix + c, result, counter);

            if (counter.get() >= 10) {
                return;
            }
        }
    }

    /**
     * Clears the trie, removing all words.
     */
    public void clear() {
        root = new Node();
        size = 0;
    }

    /**
     * Deletes a word from the trie.
     *
     * @param key The word to delete.
     * @return true if the word was successfully deleted, false otherwise.
     */
    public boolean delete(String key) {
        return delete(root, key, 0);
    }

    private boolean delete(Node current, String key, int index) {
        if (index == key.length()) {
            if (!current.isEndOfWord) {
                return false;
            }
            current.isEndOfWord = false;
            size--;
            return current.children.isEmpty();
        }
        char c = key.charAt(index);
        if (!current.children.containsKey(c)) {
            return false;
        }
        Node node = current.children.get(c);
        boolean shouldDeleteCurrentNode = delete(node, key, index + 1) && !node.isEndOfWord;
        if (shouldDeleteCurrentNode) {
            current.children.remove(c);
            return current.children.isEmpty();
        }
        return false;
    }


    /**
     * Gets the size of the trie (number of words).
     *
     * @return The size of the trie.
     */
    public int size() {
        return size;
    }

    /**
     * Trie node class representing each node in the trie.
     */
    private static class Node implements Serializable {
        private static final long serialVersionUID = 1L;
        private final java.util.Map<Character, Node> children;
        private boolean isEndOfWord;

        float[] coords;

        /**
         * Constructs a trie node.
         */
        public Node() {
            children = new HashMap<>();
            isEndOfWord = false;

        }

        /**
         * Sets the coordinates associated with the word.
         *
         * @param coords The coordinates to set.
         */
        public void setCoords(float[] coords) {
            this.coords = coords;
        }
    }
}
