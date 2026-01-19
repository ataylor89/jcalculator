package calculator;

import calculator.exceptions.InvalidExpression;
import java.util.List;
import java.util.ArrayList;

public class Parser {

    private String digits = "0123456789";
    private String operators = "+-*/_^";
    private String parentheses = "()";
    private String leftAssociativeOperators = "+-*/";
    private String rightAssociativeOperators = "^_";

    public List<String> parse(String expression) {
        List<String> tokens = new ArrayList<>();
        String buffer = "";
        expression = expression.strip();
        int n = expression.length();
        for (int i = 0; i < n; i++) {
            char ch = expression.charAt(i);
            if (isOperator(ch) || isParenthesis(ch)) {
                if (buffer.length() > 0) {
                    if (isNumber(buffer)) {
                        tokens.add(buffer);
                        buffer = "";
                    }
                    else {
                        throw new InvalidExpression("The string buffer cannot be parsed as a number");
                    }
                }
                tokens.add(String.valueOf(ch));
            }
            else if (ch == ' ') {
                if (buffer.length() > 0) {
                    if (isNumber(buffer)) {
                        tokens.add(buffer);
                        buffer = "";
                    }
                    else {
                        throw new InvalidExpression("The string buffer cannot be parsed as a number");
                    }
                }
            }
            else if (isDigit(ch) || ch == '.') {
                buffer += ch;
                if (buffer.length() > 0 && i == n - 1) {
                    if (isNumber(buffer)) {
                        tokens.add(buffer);
                        buffer = "";
                    }
                    else {
                        throw new InvalidExpression("The string buffer cannot be parsed as a number");
                    }
                }
            }
            else {
                throw new InvalidExpression("The expression contains one or more invalid characters");
            }
        }
        if (tokens.isEmpty()) {
            throw new InvalidExpression("The expression is empty");
        }
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("-") && (i == 0 || tokens.get(i-1).equals("(") || isOperator(tokens.get(i-1)))) {
                tokens.set(i, "_");
            }
        }
        return tokens;
    }

    public boolean isDigit(char ch) {
        return digits.indexOf(ch) >= 0;
    }

    public boolean isDigit(String s) {
        return digits.indexOf(s) >= 0;
    }

    public boolean isOperator(char ch) {
        return operators.indexOf(ch) >= 0;
    }

    public boolean isOperator(String s) {
        return operators.indexOf(s) >= 0;
    }

    public boolean isParenthesis(char ch) {
        return parentheses.indexOf(ch) >= 0;
    }

    public boolean isParenthesis(String s) {
        return parentheses.indexOf(s) >= 0;
    }

    public boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isLeftAssociative(char ch) {
        return leftAssociativeOperators.indexOf(ch) >= 0;
    }

    public boolean isLeftAssociative(String s) {
        return leftAssociativeOperators.indexOf(s) >= 0;
    }

    public boolean isRightAssociative(char ch) {
        return rightAssociativeOperators.indexOf(ch) >= 0;
    }

    public boolean isRightAssociative(String s) {
        return rightAssociativeOperators.indexOf(s) >= 0;
    }

    public int precedence(char ch) {
        return precedence(String.valueOf(ch));
    }

    public int precedence(String s) {
        switch (s) {
            case "+":
                return 0;
            case "-":
                return 0;
            case "*":
                return 1;
            case "/":
                return 1;
            case "_":
                return 1;
            case "^":
                return 2;
            default:
                return -1;
        }
    }

}
