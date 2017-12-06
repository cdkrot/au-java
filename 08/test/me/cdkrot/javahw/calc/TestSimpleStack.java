package me.cdkrot.javahw.calc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class TestSimpleStack {
    @Test
    public void testEmptyStack() {
        Stack<String> stack = new SimpleStack<String>();

        assertEquals(stack.size(), 0);
        assertTrue(stack.empty());
        assertEquals(stack.top(), null);
    }

    @Test
    public void testPushes() {
        Stack<Integer> stack = new SimpleStack<Integer>();

        assertEquals(stack.size(), 0);
        assertTrue(stack.empty());

        for (int i = 1; i <= 3; ++i) {
            stack.push(i);
            assertEquals(stack.size(), i);
            assertEquals((int)stack.top(), i);
            assertFalse(stack.empty());
        }
    }


    @Test
    public void testPops() {
        Stack<Integer> stack = new SimpleStack<Integer>();
       
        for (int i = 1; i <= 3; ++i)
            stack.push(i);

        for (int i = 3; i >= 1; --i) {
            assertEquals(stack.size(), i);
            assertFalse(stack.empty());

            assertEquals((int)stack.top(), i);
            stack.pop();
        }

        assertTrue(stack.empty());
    }

    @Test
    public void testClear() {
        Stack<Integer> stack = new SimpleStack<Integer>();
       
        for (int i = 1; i <= 3; ++i)
            stack.push(i);

        stack.clear();

        // check for defects
        assertEquals(stack.size(), 0);
        assertTrue(stack.empty());

        stack.push(228);
        assertEquals(stack.size(), 1);
        assertFalse(stack.empty());
        assertEquals((int)stack.top(), 228);
    }
}
