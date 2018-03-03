package me.cdkrot.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import java.util.*;

public class TestCollections {
    private Function1<Integer, Integer> square = new Function1<Integer, Integer>() {
            public Integer apply(Integer a) {
                    return a * a;
            };
        };

    private Function1<Integer, Integer> id = new Function1<Integer, Integer>() {
            public Integer apply(Integer a) {
                return a;
            };
        };

    private Predicate<Integer> greater(int x) {
        return new Predicate<Integer>() {
            public Boolean apply(Integer y) {
                return y >= x;
            }
        };
    }

    private Predicate<Integer> less(int x) {
        return new Predicate<Integer>() {
            public Boolean apply(Integer y) {
                return y <= x;
            }
        };
    }

    private Function2<String, String, String> concat = new Function2<String, String, String>() {
            public String apply(String a, String b) {
                return "(" + a + "+" + b + ")";
            }
        };
    
    @Test
    public void testMap() {
        List<Integer> lst = Arrays.asList(1, 2, 3);
        List<Integer> exp = Arrays.asList(1, 4, 9);
        
        assertEquals(exp, Collections.map(square, lst));
        assertEquals(lst, Collections.map(id, lst));
    }

    @Test
    public void testMapEmpty() {
        ArrayList<Integer> lst = new ArrayList<Integer>();
        assertEquals(lst, Collections.map(square, lst));
    }

    @Test
    public void testFilter() {
        List<Integer> lst = Arrays.asList(1, 2, 3);
        assertEquals(new ArrayList<Integer>(), Collections.filter(greater(7), lst));

        ArrayList<Integer> exp = new ArrayList<Integer>();
        exp.add(1);
        exp.add(3);
        assertEquals(exp, Collections.filter(new Predicate<Integer>() {
                public Boolean apply(Integer v) {
                    return v % 2 == 1;
                };
            }, lst));
    }

    @Test
    public void testWhile() {
        List<Integer> lst = Arrays.asList(1, 2, 3);
        assertEquals(lst, Collections.takeWhile(greater(-1), lst));
        assertEquals(new ArrayList<Integer>(), Collections.takeWhile(greater(4), lst));

        ArrayList<Integer> exp = new ArrayList<Integer>();
        exp.add(1);
        exp.add(2);
        assertEquals(exp, Collections.takeWhile(less(2), lst));
    }

    @Test
    public void testUnless() {
        List<Integer> lst = Arrays.asList(1, 2, 3);
        assertEquals(lst, Collections.takeUnless(greater(7), lst));
        assertEquals(new ArrayList<Integer>(), Collections.takeUnless(greater(-1), lst));

        ArrayList<Integer> exp = new ArrayList<Integer>();
        exp.add(1);
        exp.add(2);
        assertEquals(exp, Collections.takeUnless(greater(3), lst));
    }
    
    @Test
    public void testFoldl() {
        List<String> lst = Arrays.asList("1", "2", "3");
        assertEquals("(((x+1)+2)+3)", Collections.foldl(concat, "x", lst));
    }

    @Test
    public void testFoldr() {
        List<String> lst = Arrays.asList("1", "2", "3");
        assertEquals("(1+(2+(3+x)))", Collections.foldr(concat, "x", lst));
    }
}
