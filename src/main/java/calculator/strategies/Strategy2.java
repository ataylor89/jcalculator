package calculator.strategies;

import calculator.Parser;
import calculator.exceptions.InvalidExpression;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Strategy2 implements Strategy {

    private Parser parser;

    public Strategy2() {
        parser = new Parser();
    }

    @Override
    public double eval(String expression) {
        List<String> tokens = parser.parse(expression);
        return evalTokens(tokens);
    }

    private void performOperation(Deque<String> operators, Deque<Double> operands) {
        String operator = operators.pop();
        if (operator.equals("_")) {
            if (operands.size() == 0) {
                throw new InvalidExpression("The " + operator + " operation is missing an operand");
            }
            double operand1 = operands.pop();
            operands.push(-1 * operand1);
        }
        else if (parser.isOperator(operator)) {
            if (operands.size() < 2) {
                throw new InvalidExpression("The " + operator + " operation is missing one or more operands");
            }
            double operand2 = operands.pop();
            double operand1 = operands.pop();
            if (operator.equals("+")) {
                operands.push(operand1 + operand2);
            }
            else if (operator.equals("-")) {
                operands.push(operand1 - operand2);
            }
            else if (operator.equals("*")) {
                operands.push(operand1 * operand2);
            }
            else if (operator.equals("/")) {
                if (operand2 == 0) {
                    throw new InvalidExpression("Division by zero is not allowed");
                }
                operands.push(operand1 / operand2);
            }
            else if (operator.equals("^")) {
                operands.push(Math.pow(operand1, operand2));
            }
        }
    }

    private double evalTokens(List<String> tokens) {
        Deque<Double> operands = new ArrayDeque<>();
        Deque<String> operators = new ArrayDeque<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (parser.isNumber(token)) {
                operands.push(Double.parseDouble(token));
            }
            else if (token.equals("(")) {
                operators.push(token);
            }
            else if (token.equals(")")) {
                while (operators.size() > 0 && !operators.peek().equals("(")) {
                    performOperation(operators, operands);
                }
                operators.pop();
            }
            else if (parser.isOperator(token)) {
                boolean isLeftAssoc = parser.isLeftAssociative(token);
                while (operators.size() > 0 && !operators.peek().equals("(") &&
                        (parser.precedence(operators.peek()) > parser.precedence(token) ||
                        (parser.precedence(operators.peek()) == parser.precedence(token) && isLeftAssoc))) {
                    performOperation(operators, operands);
                }
                operators.push(token);
            }
        }
        while (operators.size() > 0) {
            performOperation(operators, operands);
        }
        return operands.pop();
    }

}
