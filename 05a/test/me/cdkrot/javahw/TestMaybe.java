package me.cdkrot.javahw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

public class TestMaybe {
    @Test(expected = NothingException.class) 
    public void testGetNothing() throws NothingException {
        Maybe<Integer> mb = Maybe.nothing();
        int val = mb.get();
    }

    @Test
    public void testGet() throws NothingException {
        Maybe<Integer> mb = Maybe.just(3);
        assertEquals((int)(mb.get()), 3);
    }

    @Test
    public void testIsPresent() {
        Maybe<Integer> mb = Maybe.nothing();
        assertFalse(mb.isPresent());
        
        Maybe<Integer> jst = Maybe.just(3);
        assertTrue(jst.isPresent());
    }

    @Test
    public void testGetWithDefault() {
        Maybe<Integer> mb = Maybe.nothing();
        assertEquals((int)(mb.getWithDefault(4)), 4);
        
        Maybe<Integer> jst = Maybe.just(3);
        assertEquals((int)(jst.getWithDefault(4)), 3);
    }

    @Test
    public void testMap() {
        Maybe<Integer> mb = Maybe.nothing();
        mb = mb.map((x) -> (x * x));
        
        Maybe<Integer> jst = Maybe.just(3);
        jst = jst.map((x) -> (x * x));

        assertFalse(mb.isPresent());
        assertTrue(jst.isPresent());

        assertEquals((int)(jst.getWithDefault(null)), 9);
    }
}
