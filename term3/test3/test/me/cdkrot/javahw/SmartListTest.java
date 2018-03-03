package me.cdkrot.javahw;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SmartListTest {
    public void checkEquals(SmartList<Integer> sm, ArrayList<Integer> correct) {
        assertEquals(sm.size(), correct.size());
        for (int i = 0; i != sm.size(); ++i)
            assertEquals(sm.get(i), correct.get(i));
    }
    
    @Test
    public void testEmpty() {
        SmartList<Integer> sm = new SmartList<Integer>();

        assertTrue(sm.isEmpty());
        assertEquals(sm.size(), 0);
        assertFalse(sm.iterator().hasNext());
        assertFalse(sm.contains(null));
        assertFalse(sm.contains(1));
    }

    @Test
    public void testBrutal() {
        for (int cnt = 1; cnt <= 10; ++cnt) {
            SmartList<Integer> sm = new SmartList<Integer>();
            for (int i = 1; i <= cnt; ++i)
                sm.add(i);

            assertEquals(sm.size(), cnt);
            ArrayList<Integer> iterated = new ArrayList<Integer>();
            for (Iterator<Integer> iter = sm.iterator(); iter.hasNext();)
                iterated.add(iter.next());

            assertEquals(iterated.size(), cnt);
            for (int i = 0; i != cnt; ++i)
                assertEquals((int)(iterated.get(i)), i + 1);

            for (int i = 0; i != cnt; ++i)
                assertEquals((int)(sm.get(i)), i + 1);
        }
    }

    @Test
    public void testDelete() {
        for (int cnt = 1; cnt <= 10; ++cnt) {
            SmartList<Integer> sm = new SmartList<Integer>();
            ArrayList<Integer> correct = new ArrayList<Integer>();
            
            for (int i = 0; i != cnt; ++i) {
                sm.add(i);
                correct.add(i);
            }

            for (Iterator<Integer> iter = sm.iterator(); iter.hasNext();)
                if (iter.next() % 2 == 1)
                    iter.remove();

            for (Iterator<Integer> iter = correct.iterator(); iter.hasNext();)
                if (iter.next() % 2 == 1)
                    iter.remove();

            checkEquals(sm, correct);
        }
    }
    
    @Test
    public void testDirectDelete() {
        for (int cnt = 1; cnt <= 10; ++cnt) {
            for (int pdel = 0; pdel != cnt; ++pdel) {
                SmartList<Integer> sm = new SmartList<Integer>();
                ArrayList<Integer> correct = new ArrayList<Integer>();
                
                for (int i = 0; i != cnt; ++i) {
                    sm.add(i);
                    correct.add(i);
                }

                int r1 = sm.remove(pdel);
                int r2 = correct.remove(pdel);

                assertEquals(r1, r2);
                checkEquals(sm, correct);
            }
        }    
    }

    @Test
    public void testDirectAdd() {
        for (int cnt = 1; cnt <= 10; ++cnt) {
            for (int padd = 0; padd <= cnt; ++padd) {
                SmartList<Integer> sm = new SmartList<Integer>();
                ArrayList<Integer> correct = new ArrayList<Integer>();
                
                for (int i = 0; i != cnt; ++i) {
                    sm.add(i);
                    correct.add(i);
                }

                sm.add(padd, -1);
                correct.add(padd, -1);

                checkEquals(sm, correct);
            }
        }    
    }

    @Test
    public void testCreateFromCollection() {
        for (int len = 0; len <= 10; ++len) {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            for (int i = 0; i != len; ++i)
                arr.add(i);

            SmartList<Integer> sm = new SmartList<Integer>(arr);
            checkEquals(sm, arr);
        }
    }

    @Test
    public void testSets() {
        for (int len = 1; len <= 10; ++len) {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            SmartList<Integer> sm = new SmartList<Integer>();
            
            for (int i = 0; i != len; ++i) {
                arr.add(i);
                sm.add(i);
            }

            int numOperations = 1000;
            Random rand = new Random(228);

            for (int i = 0; i != numOperations; ++i) {
                int ind = rand.nextInt(len);
                int val = rand.nextInt(1000);

                arr.set(ind, val);
                sm.set(ind, val);

                checkEquals(sm, arr);
            }
        }
    }
}
