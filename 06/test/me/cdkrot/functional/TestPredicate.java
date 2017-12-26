package me.cdkrot.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import java.util.*;

public class TestPredicate {
    Predicate<Integer> isEven = new Predicate<Integer>() {
            public Boolean apply(Integer a) {
                return a % 2 == 0;
            }
        };

    Predicate<Integer> isPositive = new Predicate<Integer>() {
            public Boolean apply(Integer a) {
                return a >= 0;
            }
        };
    
    
    @Test
    public void testAlwaysTrue() {
        assertEquals(Predicate.ALWAYS_TRUE.apply("228"), true);
        assertEquals(Predicate.ALWAYS_TRUE.apply(666), true);
    }

    @Test
    public void testAlwaysFalse() {
        assertEquals(Predicate.ALWAYS_FALSE.apply("228"), false);
        assertEquals(Predicate.ALWAYS_FALSE.apply(666), false);
    }

    @Test
    public void testSimple() {
        assertEquals(isEven.apply(0), true);
        assertEquals(isEven.apply(1), false);
    }

    @Test
    public void testNot() {
        assertEquals(isEven.not().apply(0), false);
        assertEquals(isEven.not().apply(1), true);
    }

    @Test
    public void testOr() {
        assertEquals(isEven.or(isPositive).apply(-2), true);
        assertEquals(isEven.or(isPositive).apply(-1), false);
        assertEquals(isEven.or(isPositive).apply(0), true);
        assertEquals(isEven.or(isPositive).apply(1), true);
    }

    @Test
    public void testAnd() {
        assertEquals(isEven.and(isPositive).apply(-2), false);
        assertEquals(isEven.and(isPositive).apply(-1), false);
        assertEquals(isEven.and(isPositive).apply(0), true);
        assertEquals(isEven.and(isPositive).apply(1), false);
    }

}
