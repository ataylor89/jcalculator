package calculator.strategies;

import calculator.Parser;
import calculator.exceptions.InvalidExpression;
import java.util.ArrayList;
import java.util.List;

public class Strategy3 implements Strategy {
    
    private Parser parser;

    public Strategy3() {
        parser = new Parser();
    }

    @Override
    public double eval(String expression) {
        List<String> tokens = parser.parse(expression);
        return simplify(tokens);
    }

    private int next(List<String> tokens) {
        int n = tokens.size();
        int index = -1;
        int nestedness = 0;
        int highestPriority = -1;

        for (int i = 0; i < n; i++) {
            String token = tokens.get(i);
            
            if (i < n - 1 && tokens.get(i).equals("(") && tokens.get(i+1).equals(")")) {
                tokens.remove(i);
                tokens.remove(i);
                return next(tokens);
            }
            else if (i < n - 2 && tokens.get(i).equals("(") && tokens.get(i+2).equals(")")) {
                tokens.remove(i);
                tokens.remove(i+1);
                return next(tokens);
            }
            else if (parser.isNumber(token)) {
                continue;
            }
            else if (token.equals("(")) {
                nestedness += 1;
                continue;
            }
            else if (token.equals(")")) {
                nestedness -= 1;
                continue;
            }
            else if (parser.isOperator(token)) {
                int priority = parser.precedence(token) + 3 * nestedness;
                boolean isRightAssoc = parser.isRightAssociative(token);
                if (priority > highestPriority || (priority == highestPriority && isRightAssoc)) {
                    highestPriority = priority;
                    index = i;
                }
            }
        }

        return index;
    }

    private void validate(int index, List<String> tokens) {
        String operator = tokens.get(index);
        
        if (operator.equals("_")) {
            if (tokens.size() < index + 2) {
                throw new InvalidExpression("The " + operator + " operation is missing an operand");
            }

            String operand = tokens.get(index + 1);

            if (!parser.isNumber(operand)) {
                throw new InvalidExpression("The " + operator + " operation has an invalid operand");
            }
        }
        else {
            if (index == 0 || tokens.size() < index + 2) {
                throw new InvalidExpression("The " + operator + " operation is missing one or more operands");
            }

            String operand1 = tokens.get(index - 1);
            String operand2 = tokens.get(index + 1);

            if (!parser.isNumber(operand1) || !parser.isNumber(operand2)) {
                throw new InvalidExpression("The " + operator + " operation has one or more invalid operands");
            }

            if (operator.equals("/") && Double.parseDouble(operand2) == 0) {
                throw new InvalidExpression("Division by zero is not allowed");
            }
        }
    }

    private double simplify(List<String> tokens) {
        if (tokens.size() == 1 && parser.isNumber(tokens.get(0))) {
            return Double.parseDouble(tokens.get(0));
        }
        
        int index = next(tokens);

        if (index < 0) {
            throw new InvalidExpression("Unable to find an operation to perform");
        }

        validate(index, tokens);
        String operator = tokens.get(index);

        if (operator.equals("_")) {
            Double operand = Double.parseDouble(tokens.get(index+1));
            Double result = -1 * operand;
            tokens.set(index, String.valueOf(result));
            tokens.remove(index + 1);
        }
        else {
            Double operand1 = Double.parseDouble(tokens.get(index-1));
            Double operand2 = Double.parseDouble(tokens.get(index+1));
            Double result = null;

            if (operator.equals("+")) {
                result = operand1 + operand2;
            }
            else if (operator.equals("-")) {
                result = operand1 - operand2;
            }
            else if (operator.equals("*")) {
                result = operand1 * operand2;
            }
            else if (operator.equals("/")) {
                result = operand1 / operand2;
            }
            else if (operator.equals("^")) {
                result = Math.pow(operand1, operand2);
            }
            else {
                throw new InvalidExpression("The token '" + operator + "' is not a supported operator");
            }

            tokens.set(index - 1, String.valueOf(result));
            tokens.remove(index);
            tokens.remove(index);
        }

        return simplify(tokens);
    }

}
