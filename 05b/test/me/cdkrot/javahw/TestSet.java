package me.cdkrot.javahw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

public class TestSet {
    @Test
    public void testSize() {
        Set<Integer> set = new Set<Integer>();

        assertEquals(set.size(), 0);
        for (int i = 0; i != 100; ++i) {
            assertTrue(set.add(i));
            assertEquals(set.size(), i + 1);
        }
    }

    @Test
    public void testAddSimple() {
        Set<Integer> set = new Set<Integer>();

        assertTrue(set.add(0));
        assertTrue(set.add(1));
        assertTrue(set.add(2));
        assertFalse(set.add(0));
        assertFalse(set.add(1));

        assertEquals(set.size(), 3);
    }

    @Test
    public void testContainsSimple() {
        Set<Integer> set = new Set<Integer>();

        assertEquals(set.size(), 0);
        for (int i = 0; i != 100; ++i) {
            assertTrue(set.add(i));
            for (int j = 0; j != 100; ++j)
                assertEquals(set.contains(j), j <= i);
        }
    }
}
