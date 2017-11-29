package me.cdkrot.javahw.calc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.*;

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

    public void testReversePolishEvaluation() {
        // Evaling "1 + 5 * 7
        
        List<Object> RPN = new ArrayList<Object>();
        RPN.add(new BigInteger("1"));
        RPN.add(new BigInteger("5"));
        RPN.add(new BigInteger("7"));

        RPN.add(Calculator.BinaryOperator.fromChar('*'));
        RPN.add(Calculator.BinaryOperator.fromChar('+'));
        
        Calculator calc = new Calculator(new SimpleStack<Character>(), new SimpleStack<BigInteger>());
        
        assertEquals(calc.compute(RPN), new BigInteger("36"));
    }
    
    public void testEvaluationProcess() {
        // Evaling "1 + 5 * 7"

        List<Object> RPN = new ArrayList<Object>();
        RPN.add(new BigInteger("1"));
        RPN.add(new BigInteger("5"));
        RPN.add(new BigInteger("7"));

        RPN.add(Calculator.BinaryOperator.fromChar('*'));
        RPN.add(Calculator.BinaryOperator.fromChar('+'));
        
        Stack<BigInteger> operands = mock(Stack.class);

        Calculator calc = new Calculator(null, operands);
        
        when(operands.top()).thenReturn(new BigInteger("7"), new BigInteger("5"), new BigInteger("35"), new BigInteger("1"), new BigInteger("36"));

        when(operands.size()).thenReturn(2, 2, 1);
        
        assertEquals(calc.compute(RPN), new BigInteger("36"));

        InOrder inorder = inOrder(operands);
        inorder.verify(operands).push(new BigInteger("7"));
        inorder.verify(operands).push(new BigInteger("5"));
        inorder.verify(operands).top();
        inorder.verify(operands).pop();
        inorder.verify(operands).top();
        inorder.verify(operands).pop();
        inorder.verify(operands).push(new BigInteger("35"));
        inorder.verify(operands).push(new BigInteger("1"));
        inorder.verify(operands).top();
        inorder.verify(operands).pop();
        inorder.verify(operands).top();
        inorder.verify(operands).pop();
        inorder.verify(operands).push(new BigInteger("36"));
        inorder.verify(operands).top();
    }
}
