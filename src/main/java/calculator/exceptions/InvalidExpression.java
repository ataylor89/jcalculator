package calculator.exceptions;

public class InvalidExpression extends RuntimeException {

    public InvalidExpression() {
        super("The expression is invalid");
    }

    public InvalidExpression(String message) {
        super(message);
    }    

}
