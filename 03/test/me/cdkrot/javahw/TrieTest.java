package me.cdkrot.javahw;

import me.cdkrot.javahw.Trie;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.io.*;
import java.util.Base64;

public class TrieTest {
    @Test
    public void testAddSimple() {
        Trie trie = new Trie();
        assertTrue(trie.add("123"));
        assertTrue(trie.add("123456"));
        assertTrue(trie.add("aba"));
        assertFalse(trie.add("aba"));
        
        assertTrue(trie.containsKey("123"));
        assertTrue(trie.containsKey("123456"));
        assertTrue(trie.containsKey("aba"));
        
        assertFalse(trie.containsKey("12"));
        assertFalse(trie.containsKey("1234"));
        assertFalse(trie.containsKey("1234567"));
        assertFalse(trie.containsKey("ba"));
        assertFalse(trie.containsKey(""));
    }

    @Test
    public void testSize() {
        Trie trie = new Trie();

        assertEquals(trie.size(), 0);
        
        trie.add("123");
        trie.add("123456");
        trie.add("aba");

        assertEquals(trie.size(), 3);

        trie.add("aba");

        assertEquals(trie.size(), 3);

        trie.add("111");
        
        assertEquals(trie.size(), 4);
    }

    @Test
    public void testRemove() {
        Trie trie = new Trie();

        trie.add("123");
        trie.add("123456");
        trie.add("aba");

        assertTrue(trie.containsKey("123"));

        assertTrue(trie.remove("123"));
        assertFalse(trie.remove("123"));        
        
        assertEquals(trie.size(), 2);
        assertTrue(trie.containsKey("123456"));
        assertFalse(trie.containsKey("123"));

        assertTrue(trie.add("123"));
    }

    @Test
    public void testPrefixCount() {
        Trie trie = new Trie();

        trie.add("123");
        trie.add("123456");
        trie.add("aba");

        assertEquals(trie.howManyStartsWithPrefix(""), 3);
        assertEquals(trie.howManyStartsWithPrefix("1"), 2);
        assertEquals(trie.howManyStartsWithPrefix("123"), 2);
        assertEquals(trie.howManyStartsWithPrefix("123456"), 1);
        assertEquals(trie.howManyStartsWithPrefix("c"), 0);
    }

    @Test
    public void testIO() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Trie trie = new Trie();

        trie.add("123");
        trie.add("123456");
        trie.add("aba");

        trie.serialize(out);
        trie = new Trie();
        trie.deserialize(new ByteArrayInputStream(out.toByteArray()));

        assertEquals(trie.size(), 3);
        assertTrue(trie.containsKey("123"));
        assertFalse(trie.containsKey(""));
        assertEquals(trie.howManyStartsWithPrefix("12"), 2);
    }

    @Test
    public void testIOHardWay() throws ClassNotFoundException, IOException {
        String b64 =
            "rO0ABXNyABptZS5jZGtyb3QuamF2YWh3LlRyaWUkTm9kZeU14U1E8lHYAgAFQwAKY2hhclBh" +
            "cmVudFoABmlzVGVybUkAC3N1YnRyZWVTaXplTAACZ290ABNMamF2YS91dGlsL0hhc2hNYXA7" +
            "TAAGcGFyZW50dAAcTG1lL2Nka3JvdC9qYXZhaHcvVHJpZSROb2RlO3hwAAAAAAAAA3NyABFq" +
            "YXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhw" +
            "P0AAAAAAAAx3CAAAABAAAAACc3IAE2phdmEubGFuZy5DaGFyYWN0ZXI0i0fZaxomeAIAAUMA" +
            "BXZhbHVleHAAMXNxAH4AAAAxAQAAAAJzcQB+AAQ/QAAAAAAADHcIAAAAEAAAAAFzcQB+AAYA" +
            "MnNxAH4AAAAyAQAAAAFzcQB+AAQ/QAAAAAAAAHcIAAAAEAAAAAB4cQB+AAh4cQB+AANzcQB+" +
            "AAYANHNxAH4AAAA0AAAAAAFzcQB+AAQ/QAAAAAAADHcIAAAAEAAAAAFzcQB+AAYANXNxAH4A" +
            "AAA1AQAAAAFzcQB+AAQ/QAAAAAAAAHcIAAAAEAAAAAB4cQB+AA54cQB+AAN4cA==";

        // b64 contains Trie of "1", "12" and "45".
        
        Trie trie = new Trie();
        trie.deserialize(new ByteArrayInputStream(Base64.getDecoder().decode(b64)));

        assertEquals(trie.size(), 3);
        assertTrue(trie.containsKey("1"));
        assertTrue(trie.containsKey("12"));
        assertTrue(trie.containsKey("45"));
        assertFalse(trie.containsKey("4"));
        assertEquals(trie.howManyStartsWithPrefix("1"), 2);
    }
}
