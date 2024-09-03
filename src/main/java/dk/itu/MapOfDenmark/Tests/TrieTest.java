package dk.itu.MapOfDenmark.Tests;


import dk.itu.MapOfDenmark.Model.Trees.Trie.CompressedTrie;
import dk.itu.MapOfDenmark.Model.Trees.Trie.Trie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrieTest {
    @Test
    public void testAdd() {
        Trie trie = new Trie();
        String[] address = new String[] {
                "Hovedgade 10, 2800 Kongens Lyngby",
                "Nørregade 25, 1165 København K",
                "Østergade 5, 3. tv., 5000 Odense C",
                "Skovvej 15, 8700 Horsens",
                "Kollegievej 3, 2200 København N",
                "Sommerhusvej 7, 4000 Roskilde",
                "Rækkehus Allé 12, 2300 København S",
                "Seniorcenterparken 8, 7100 Vejle",
                "Fjeldvej 20, 9500 Hobro",
                "Andelsboligstræde 14, 6000 Kolding"
        };
        for(int i = 0; i < address.length; i++) {
            trie.insert(address[i], new float[]{i,i});
        }

        for(int i = 0; i < address.length; i++) {
            assertArrayEquals(new float[]{i,i}, trie.search(address[i]));
        }
//        assertEquals(-1,trie.search("Meow"));
//        assertEquals(trie.search("Fjeldvej 20, 9500 Hobro"), 8);
    }

    @Test
    public void testPrefix() {
        Trie trie = new Trie();
        List<String> address = new ArrayList<>();
        address.add("hovedgade 10, 2800 kongens lyngby");
        address.add("hovedgade 11, 2800 kongens lyngby");
        address.add("hovedgade 12, 2800 kongens lyngby");
        address.add("hovedgade 13, 2800 kongens lyngby");
        address.add("hovedgade 14, 2800 kongens lyngby");
        for(int i = 0; i < address.size(); i++) {
            trie.insert(address.get(i), new float[]{i,i});
        }

        List<String> out = trie.searchPrefix("Hovedgade");
        assertEquals(address.size(), out.size());
        for(int i = 0; i < out.size(); i++) {
            assertEquals(address.get(i),out.get(i));
        }
    }

    @Test
    public void testInsertAndSearch() {
        Trie trie = new Trie();

        // Insert some words with coordinates
        trie.insert("apple", new float[]{1.0f, 2.0f});
        trie.insert("banana", new float[]{3.0f, 4.0f});
        trie.insert("cherry", new float[]{5.0f, 6.0f});

        // Test search for existing words
        assertArrayEquals(new float[]{1.0f, 2.0f}, trie.search("apple"));
        assertArrayEquals(new float[]{3.0f, 4.0f}, trie.search("banana"));
        assertArrayEquals(new float[]{5.0f, 6.0f}, trie.search("cherry"));

        // Test search for non-existing words
        assertNull(trie.search("pear"));
        assertNull(trie.search("grape"));
    }

    @Test
    public void testSearchPrefix() {
        Trie trie = new Trie();

        // Insert some words with coordinates
        trie.insert("Skovvej 20, 8700 Horsens", new float[]{1.0f, 2.0f});
        trie.insert("Skovvej 15, 8700 Horsens", new float[]{1.0f, 2.0f});
        trie.insert("hovedgade 10, 2800 kongens lyngby", new float[]{3.0f, 4.0f});
        trie.insert("hovedgade 14, 2800 kongens lyngby", new float[]{5.0f, 6.0f});

        // Test prefix search
        assertEquals(2, trie.searchPrefix("hovedgade").size());
        assertEquals(1, trie.searchPrefix("skovvej 15").size());
        assertEquals(2, trie.searchPrefix("skovvej").size());

    }
}
