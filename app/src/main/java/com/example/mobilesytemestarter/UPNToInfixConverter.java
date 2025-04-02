package com.example.mobilesytemestarter;

import java.util.Stack;

public class UPNToInfixConverter {

    public static String convertToInfix(String[] tokens) {
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                if (stack.size() < 2) return "ERROR";
                String operand2 = stack.pop();
                String operand1 = stack.pop();
                String expression = "(" + operand1 + " " + token + " " + operand2 + ")";
                stack.push(expression);
            } else {
                stack.push(token);
            }
        }

        return stack.size() == 1 ? stack.pop() : "ERROR";
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/");
    }
}
