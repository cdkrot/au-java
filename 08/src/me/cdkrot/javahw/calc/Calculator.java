package me.cdkrot.javahw.calc;

import java.math.BigInteger;
import java.util.*;

/**
 * Helper class for computing RPN.
 */
public class Calculator {
    public static enum BinaryOperator {
        PLUS,
        MINUS,
        MULT,
        DIV,
        MOD;

        public static BinaryOperator fromChar(char ch) {
            switch (ch) {
            case '+':
                return PLUS;
            case '-':
                return MINUS;
            case '*':
                return MULT;
            case '/':
                return DIV;
            case '%':
                return MOD;
            default:
                return null;
            }
        }
    };
    
    private Stack<BigInteger> stack;
    private Stack<Character> operators;

    /**
     * Constructs new instance of Calculator
     * @param ops stack to use for operators in toRpn
     * @param stck stack to used for evaluation.
     */
    public Calculator(Stack<Character> ops, Stack<BigInteger> stck) {
        operators = ops;
        stack = stck;
    }
    
    /**
     * Compute the RPN.
     * @param list of Either "BinaryOperator" or "BigInteger".
     * @return resulting BigInteger
     * @throws IllegalArgumentException for ill-formed lists.
     * @throws ArithmeticException on div by zero and similar.
     */
    public BigInteger compute(List<Object> lst) {
        stack.clear();
        
        for (Object obj: lst) {
            if (obj instanceof BigInteger)
                stack.push((BigInteger)obj);
            else if (obj instanceof BinaryOperator) {
                if (stack.size() < 2)
                    throw new IllegalArgumentException();

                BigInteger arg2 = stack.top();
                stack.pop();
                BigInteger arg1 = stack.top();
                stack.pop();

                BigInteger res = null;
                
                BinaryOperator op = (BinaryOperator)obj;
                switch (op) {
                case PLUS:
                    res = arg1.add(arg2);
                    break;
                case MINUS:
                    res = arg1.subtract(arg2);
                    break;
                case MULT:
                    res = arg1.multiply(arg2);
                    break;
                case DIV:
                    res = arg1.divide(arg2);
                    break;
                case MOD:
                    res = arg1.remainder(arg2);
                }

                stack.push(res);
            } else {
                throw new IllegalArgumentException("Ill-formed RPN");
            }
        }

        if (stack.size() != 1)
            throw new IllegalArgumentException("Ill-formed RPN");
        
        return stack.top();
    }

    private static int priority(char ch) {
        switch (ch) {
        case '+':
        case '-':
            return 6;
        case '*':
        case '%':
        case '/':
            return 5;
        case '(':
            return 1000;
        default:
            return -1;
        }
    }

    public boolean isValidExpression(String str) {
        /**
         * ( -> -1
         * ) -> +1
         * number -> 0
         * operator -> 2.
         */
        int prevToken = -1;

        int balance = 1;
        
        for (int i = 0; i <= str.length();) {
            if (i != str.length() && (str.charAt(i) == ' ' || str.charAt(i) == '\n')) {
                i += 1;
                continue;
            }
            
            int curToken;
            
            if (i == str.length()) {
                curToken = +1;
                i += 1;
            } else if ('0' <= str.charAt(i) && str.charAt(i) <= '9') {
                int i0 = i;
                while (i != str.length() && '0' <= str.charAt(i) && str.charAt(i) <= '9')
                    i += 1;
                
                curToken = 0;
            } else {
                char ch = str.charAt(i++);
                if (ch == '(')
                    curToken = -1;
                else if (ch == ')')
                    curToken = +1;
                else if ("+-*/%".indexOf(ch) != -1)
                    curToken = 2;
                else
                    return false;
            }

            
            /**
             * ( -> -1
             * ) -> +1
             * number -> 0
             * operator -> 2.
             */
            if (curToken == 2) {
                if (prevToken != 1 && prevToken != 0)
                    return false;
            } else if (curToken == 0) {
                if (prevToken != -1 && prevToken != 2)
                    return false;
            } else if (curToken == +1) {
                if (prevToken != 0 && prevToken != +1)
                    return false;
            } else if (curToken == -1) {
                if (prevToken != 2 && prevToken != -1)
                    return false;
            }

            if (curToken == -1)
                balance += 1;
            if (curToken == +1)
                balance -= 1;

            if (balance < 0)
                return false;
            
            prevToken = curToken;
        }

        return (balance == 0);
    }
    
    /**
     * Parse this string to RPN.
     * @param String to parse
     * @result list of Either "BinaryOperator" or "BigInteger" which applicable for eval().
     * @throws IllegalArgumentException for ill-formed expressions.
     */    
    public List<Object> toReversePolishNotation(String str) {
        if (!isValidExpression(str))
            throw new IllegalArgumentException("Parse error");
        
        List<Object> res = new ArrayList<Object>();
        operators.clear();

        for (int i = 0; i != str.length();) {
            if ('0' <= str.charAt(i) && str.charAt(i) <= '9') {
                int i0 = i;
                while (i != str.length() && '0' <= str.charAt(i) && str.charAt(i) <= '9')
                    i += 1;
                
                res.add(new BigInteger(str.substring(i0, i)));
                continue;
            }

            char ch = str.charAt(i++);
            
            if (ch == '(') {
                operators.push('(');
            } else if (ch == ')') {
                while ((char)(operators.top()) != '(') {
                    res.add(BinaryOperator.fromChar(operators.top()));
                    operators.pop();
                }

                operators.pop();
            } else if ("+-*/%".indexOf(ch) != -1) {
                while (!operators.empty() && priority(operators.top()) <= priority(ch)) {
                    res.add(BinaryOperator.fromChar(operators.top()));
                    operators.pop();
                }
                
                operators.push(ch);                
            }
        }

        while (!operators.empty()) {
            res.add(BinaryOperator.fromChar(operators.top()));
            operators.pop();
        }
        
        return res;
    }
}
