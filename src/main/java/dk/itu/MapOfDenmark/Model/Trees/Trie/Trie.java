package dk.itu.MapOfDenmark.Model.Trees.Trie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Trie data structure for storing and searching strings.
 */
public class Trie implements Serializable {

    /**
     * Trie node class representing each node in the trie.
     */
    private class TrieNode implements Serializable {
        TrieNode[] children = new TrieNode[charCount];
        boolean isEndOfWord;
        float[] coords;

        public void setCoords(float[] coords) {
            this.coords = coords;
        }

        @Override
        public String toString() {
            return "TrieNode{" +
                    "children=" + Arrays.toString(children) +
                    ", isEndOfWord=" + isEndOfWord +
                    '}';
        }
    }

    private TrieNode root;

    /**
     * Constructs an empty trie.
     */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts a word with its coordinates into the trie.
     *
     * @param word   The word to insert.
     * @param coords The coordinates associated with the word.
     */
    public void insert(String word, float[] coords) {
        try {
            TrieNode current = root;
            String key = word.toLowerCase();
            for (int i = 0; i < word.length(); i++) {
                int index = getCharIndex(key.charAt(i));
                if (current.children[index] == null) {
                    current.children[index] = new TrieNode();
                }
                current = current.children[index];
            }
            current.isEndOfWord = true;
            current.setCoords(coords);
        } catch (IllegalArgumentException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Searches for a word in the trie and returns its coordinates.
     *
     * @param word The word to search for.
     * @return The coordinates associated with the word, or null if the word is not found.
     */
    public float[] search(String word) throws NullPointerException {
        TrieNode current = root;
        String key = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            int index = getCharIndex(key.charAt(i));
            if (current.children[index] == null) {
                return null;
            }
            current = current.children[index];
        }
        return current != null && current.isEndOfWord ? current.coords : null;
    }

    /**
     * Searches for words with the given prefix in the trie.
     *
     * @param prefix The prefix to search for.
     * @return A list of words with the given prefix.
     */
    public List<String> searchPrefix(String prefix) {
        if (prefix.isEmpty()) {
            return new ArrayList<>();
        }
        TrieNode current = root;
        List<String> words = new ArrayList<>();
        String key = prefix.toLowerCase();

        for (int i = 0; i < prefix.length(); i++) {
            int index = getCharIndex(key.charAt(i));
            if (current.children[index] == null) {
                return words;
            }
            current = current.children[index];
        }
        AtomicInteger count = new AtomicInteger();
        searchPrefixHelper(current, prefix, words, count);
        return words;
    }

    private void searchPrefixHelper(TrieNode node, String prefix, List<String> words, AtomicInteger count) {
        if (node.isEndOfWord) {
            if (search(prefix) != null) {
                words.add(prefix);
                // Increment the counter
                count.incrementAndGet();
            }
        }
        // Check if we have found 10 results
        if (count.get() >= 10) {
            return;
        }
        for (int i = 0; i < charCount; i++) {
            if (node.children[i] != null) {
                searchPrefixHelper(node.children[i], prefix + (char) getCharAtIndex(i), words, count);
                // Check if we have found 10 results after recursive call, if yes, return
                if (count.get() >= 10) {
                    return;
                }
            }
        }
    }

    private int getCharIndex(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a'; // for letters
        } else if (c >= '0' && c <= '9') {
            return 30 + (c - '0'); // for digits (index 30-39)
        } else {
            return switch (c) {
                case '.' -> 40; // index for '.'
                case ',' -> 41; // index for ','
                case '\'' -> 42; // index for '''
                case '-' -> 43; // index for '-'
                case ' ' -> 44; // index for ' '
                case 'æ' -> 26;
                case 'ø' -> 27;
                case 'å' -> 28;
                case 'é' -> 29;
                case 'ö' -> 45;
                case 'ü' -> 46;
                case 'ä' -> 47;
                case 'ß' -> 48;
                case 'ó' -> 49;
                case 'á' -> 50;
                case 'í' -> 51;
                case 'ú' -> 52;
                case 'ñ' -> 53;
                case 'ç' -> 54;
                case 'ê' -> 55;
                case 'â' -> 56;
                case 'û' -> 57;
                case 'î' -> 58;
                case 'ô' -> 59;
                case 'ë' -> 60;
                case 'à' -> 61;
                case 'è' -> 62;
                case 'ù' -> 63;
                case 'ï' -> 64;

                // Add more cases for other symbols as needed
                default -> throw new IllegalArgumentException("Unsupported character: " + c);
            };
        }
    }

    private final short charCount = 64;

    private char getCharAtIndex(int index) {
        if (index >= 0 && index < 26) {
            return (char) (index + 'a'); // for letters
        } else if (index >= 30 && index < 40) {
            return (char) (index - 30 + '0'); // for digits
        } else {
            return switch (index) {
                case 40 -> '.'; // for '.'
                case 41 -> ','; // for ','
                case 42 -> '\''; // for '''
                case 43 -> '-'; // for '-'
                case 44 -> ' '; // for ' '
                case 26 -> 'æ';
                case 27 -> 'ø';
                case 28 -> 'å';
                case 29 -> 'é';
                case 45 -> 'ö';
                case 46 -> 'ä';
                case 47 -> 'ü';
                case 48 -> 'ß';
                case 49 -> 'ó';
                case 50 -> 'á';
                case 51 -> 'í';
                case 52 -> 'ú';
                case 53 -> 'ñ';
                case 54 -> 'ç';
                case 55 -> 'ê';
                case 56 -> 'â';
                case 57 -> 'û';
                case 58 -> 'î';
                case 59 -> 'ô';
                case 60 -> 'ë';
                case 61 -> 'à';
                case 62 -> 'è';
                case 63 -> 'ù';
                case 64 -> 'ï';
                // Add more cases for other symbols as needed
                default -> throw new IllegalArgumentException("Unsupported index: " + index);
            };
        }
    }

    @Override
    public String toString() {
        return "Trie{" +
                "root=" + root +
                '}';
    }
}
