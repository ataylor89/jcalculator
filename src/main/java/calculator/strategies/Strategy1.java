package calculator.strategies;

import calculator.strategies.AbstractStrategy;
import calculator.exceptions.InvalidExpression;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Strategy1 extends AbstractStrategy {

    @Override
    public double eval(String expression) {
        List<String> tokens = parse(expression);
        List<String> postfix = convertToPostfix(tokens);
        return evalPostfix(postfix);
    }

    public List<String> convertToPostfix(List<String> tokens) {
        Deque<String> stack = new ArrayDeque<>();
        List<String> result = new ArrayList<>();
        for (String token : tokens) {
            if (isNumber(token)) {
                result.add(token);
            }
            else if (token.equals("(")) {
                stack.push(token);
            }
            else if (token.equals(")")) {
                while (stack.size() > 0 && !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                stack.pop();
            }
            else if (isOperator(token)) {
                boolean isLeftAssoc = isLeftAssociative(token);
                while (stack.size() > 0 && !stack.peek().equals("(") && 
                        (precedence(stack.peek()) > precedence(token) || (precedence(stack.peek()) == precedence(token) && isLeftAssoc))) {
                    result.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (stack.size() > 0) {
            result.add(stack.pop());
        }
        return result;
    }

    public double evalPostfix(List<String> tokens) {
        Deque<Double> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            }
            else if (token.equals("_")) {
                if (stack.size() == 0) {
                    throw new InvalidExpression("The " + token + " operation is missing an operand");
                }
                double val = stack.pop();
                stack.push(-1 * val);
            }
            else {
                if (stack.size() < 2) {
                    throw new InvalidExpression("The " + token + " operation is missing one or more operands");
                }
                double val2 = stack.pop();
                double val1 = stack.pop();
                if (token.equals("+")) {
                    stack.push(val1 + val2);
                }
                else if (token.equals("-")) {
                    stack.push(val1 - val2);
                }
                else if (token.equals("*")) {
                    stack.push(val1 * val2);
                }
                else if (token.equals("/")) {
                    if (val2 == 0) {
                        throw new InvalidExpression("Division by zero is not allowed");
                    }
                    stack.push(val1 / val2);
                }
                else if (token.equals("^")) {
                    stack.push(Math.pow(val1, val2));
                }
            }
        }
        if (stack.size() != 1) {
            throw new InvalidExpression("The expression is invalid");
        }
        return stack.pop();
    }

}
