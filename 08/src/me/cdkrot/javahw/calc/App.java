package me.cdkrot.javahw.calc;

import java.util.*;
import java.math.BigInteger;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Calculator calc = new Calculator(new SimpleStack<Character>(), new SimpleStack<BigInteger>());
        
        while (sc.hasNextLine()) {
            String expr = sc.nextLine();

            if (!calc.isValidExpression(expr)) {
                System.out.println("invalid expression");
            } else {
                BigInteger res = calc.compute(calc.toReversePolishNotation(expr));
                System.out.println(res.toString());
            }
        }
    }
}
