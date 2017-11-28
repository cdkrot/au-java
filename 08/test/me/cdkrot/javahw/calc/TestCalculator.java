package me.cdkrot.javahw.calc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import java.math.BigInteger;

public class TestCalculator {
    protected String eval(String expr) {
        Calculator calc = new Calculator(new SimpleStack<Character>(), new SimpleStack<BigInteger>());
        return calc.compute(calc.toReversePolishNotation(expr)).toString();
    }

    protected boolean correct(String expr) {
        Calculator calc = new Calculator(new SimpleStack<Character>(), new SimpleStack<BigInteger>());
        return calc.isValidExpression(expr);
    }
    
    @Test
    public void testSingle() {
        assertEquals(eval("18 + 7"), "25");
        assertEquals(eval("18 - 7"), "11");
        assertEquals(eval("18 * 7"), "126");
        assertEquals(eval("18 / 7"), "2");
        assertEquals(eval("18 % 7"), "4");
    }

    @Test
    public void testBracketsSimple() {
        assertEquals(eval("(18)"), "18");
        assertEquals(eval("18 + (7)"), "25");
        assertEquals(eval("(18 + 7)"), "25");
        assertEquals(eval("(18 + (((7))))"), "25");
    }

    @Test
    public void testSamePriority() {
        assertEquals(eval("1 - 2 - 3 - 4"), "-8");
        assertEquals(eval("1 + 2 + 3 + 4"), "10");
        assertEquals(eval("1 * 2 * 3 * 4"), "24");
    }

    @Test
    public void testPrioritySimple() {
        assertEquals(eval("5 * 7 + 3"), "38");
        assertEquals(eval("5 * (7 + 3)"), "50");
        assertEquals(eval("4 + 5 * 7 + 3"), "42");
        assertEquals(eval("(4 + 5) * 7 + 3"), "66");
        assertEquals(eval("(4 + 5) * (7 + 3)"), "90");
    }

    @Test
    public void testPriorityLarge() {
        assertEquals(eval("1 - 4 * 5 + 9 * 3 * 7 + 100 / 2"), "220");
        assertEquals(eval("1 + 1 + 3 / 2 - 10 * 20 * 5 % 29"), "-11");
        assertEquals(eval("(5 + 10) / 2 * 3 + 7 % 2 - 3"), "19");

        assertEquals(eval("20 % 11 - 3 * 11 / (2 + 7 * 3) / 3 + 2"), "11");
    }

    @Test
    public void testErrorHandling() {
        assertFalse(correct("(("));
        assertFalse(correct("))"));
        assertFalse(correct("()"));
        assertFalse(correct(")("));
        assertFalse(correct(") + 0 +("));
        assertFalse(correct("1 + + 2"));
        assertFalse(correct("1 * + 2"));
        assertFalse(correct("1 * ( 2"));
        assertFalse(correct("1 ) * 2"));
        assertFalse(correct("1 + )"));
        assertFalse(correct("1 +"));
        assertFalse(correct("* 1"));
    }
}
