package me.cdkrot.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import java.util.*;

public class TestFunction2 {
    private Function2<String, String, String> concat = new Function2<String, String, String>() {
            public String apply(String a, String b) {
                return a + b;
            }
        };

    private Function1<String, Integer> toInt = new Function1<String, Integer>() {
            public Integer apply(String a) {
                return Integer.parseInt(a);
            }
        };

    
    @Test
    public void testSimple() {
        assertEquals(concat.apply("123", "456"), "123456");
    }

    @Test
    public void testBind1() {
        assertEquals(concat.bind1("123").apply("456"), "123456");
    }

    @Test
    public void testBind2() {
        assertEquals(concat.bind2("456").apply("123"), "123456");
    }

    @Test
    public void testCurry() {
        assertEquals(concat.curry("123").apply("456"), "123456");        
    }

    @Test
    public void testCompose() {
        assertEquals(concat.compose(toInt).apply("123", "456"), new Integer(123456));
        assertEquals(concat.compose(toInt).bind1("123").apply("456"), new Integer(123456));
    }
}
