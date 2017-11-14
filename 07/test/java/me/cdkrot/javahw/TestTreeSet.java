package me.cdkrot.javahw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import java.util.*;

public class TestTreeSet {
    @Test
    public void testSize() {
        TreeSet<Integer> set = new TreeSet<Integer>();

        assertEquals(set.size(), 0);
        for (int i = 0; i != 100; ++i) {
            assertTrue(set.add(i));
            assertEquals(set.size(), i + 1);
        }
    }

    @Test
    public void testAddSimple() {
        TreeSet<Integer> set = new TreeSet<Integer>();

        assertTrue(set.add(0));
        assertTrue(set.add(1));
        assertTrue(set.add(2));
        assertFalse(set.add(0));
        assertFalse(set.add(1));

        assertEquals(set.size(), 3);
    }

    @Test
    public void testAddRemove() {
        TreeSet<Integer> set = new TreeSet<Integer>();

        assertTrue(set.add(0));
        assertTrue(set.add(1));
        assertTrue(set.add(2));
        assertFalse(set.add(0));
        assertFalse(set.add(1));

        assertEquals(set.size(), 3);

        assertTrue(set.remove(1));
        assertEquals(set.size(), 2);
        assertTrue(set.remove(2));
        assertFalse(set.remove(2));
        assertEquals(set.size(), 1);

        assertTrue(set.contains(0));
        assertFalse(set.contains(2));

        assertTrue(set.remove(0));
        assertEquals(set.size(), 0);
    }

    @Test
    public void testClear() {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i != 100; ++i)
            set.add(i);

        assertFalse(set.isEmpty());
        
        assertEquals(set.size(), 100);
        set.remove(55);
        assertEquals(set.size(), 99);
        set.clear();
        assertEquals(set.size(), 0);

        assertTrue(set.isEmpty());
    }
    
    @Test
    public void testContainsSimple() {
        TreeSet<Integer> set = new TreeSet<Integer>();

        assertEquals(set.size(), 0);
        for (int i = 0; i != 100; ++i) {
            assertTrue(set.add(i));
            for (int j = 0; j != 100; ++j)
                assertEquals(set.contains(j), j <= i);
        }

        assertEquals(set.size(), 100);
    }

    @Test
    public void testFirst() {
        TreeSet<Integer> set = new TreeSet<Integer>();

        for (int i = 0; i != 5; ++i)
            set.add(i);

        assertEquals((int)set.first(), 0);
        set.clear();
        
        assertEquals(set.first(), null);
    }
    
    @Test
    public void testLast() {
        TreeSet<Integer> set = new TreeSet<Integer>();

        for (int i = 0; i != 5; ++i)
            set.add(i);

        assertEquals((int)set.last(), 4);
        set.clear();
        
        assertEquals(set.first(), null);
    }

    @Test
    public void testLower() {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i != 5; ++i)
            set.add(i);

        assertEquals((int)set.lower(3), 2);
        assertEquals(set.lower(0), null);
        assertEquals(set.lower(-10), null);
    }

    @Test
    public void testFloor() {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i != 5; ++i)
            set.add(i);
                
        assertEquals((int)set.floor(3), 3);
        assertEquals(set.floor(-1), null);
        assertEquals(set.floor(-10), null);
    }

    @Test
    public void testHigher() {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i != 5; ++i)
            set.add(i);

        assertEquals((int)set.higher(3), 4);
        assertEquals(set.higher(10), null);
        assertEquals(set.higher(4), null);
    }

    @Test
    public void testCeiling() {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i != 5; ++i)
            set.add(i);

        assertEquals((int)set.ceiling(3), 3);
        assertEquals(set.ceiling(10), null);
        assertEquals(set.ceiling(5), null);
    }

    @Test
    public void testForwardIteration() {
        ArrayList<Integer> lst = new ArrayList<Integer>();

        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i != 5; ++i)
            set.add(i);

        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext())
            lst.add(iter.next());

        assertEquals(lst, Arrays.asList(0, 1, 2, 3, 4));
    }

    @Test
    public void testBackwardIteration() {
        ArrayList<Integer> lst = new ArrayList<Integer>();

        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i != 5; ++i)
            set.add(i);

        Iterator<Integer> iter = set.descendingIterator();
        while (iter.hasNext())
            lst.add(iter.next());

        assertEquals(lst, Arrays.asList(4, 3, 2, 1, 0));
    }
}
