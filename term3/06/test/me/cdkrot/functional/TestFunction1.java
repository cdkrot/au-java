package me.cdkrot.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import java.util.*;

public class TestFunction1 {
    private Function1<Integer, Integer> square = new Function1<Integer, Integer>() {
            public Integer apply(Integer a) {
                return a * a;
            }
        };
    
    @Test
    public void testSimple() {
        assertEquals(square.apply(3), new Integer(9));
    }

    @Test
    public void testCompose() {
        assertEquals(square.compose(square).apply(3), new Integer(81));
    }
}
