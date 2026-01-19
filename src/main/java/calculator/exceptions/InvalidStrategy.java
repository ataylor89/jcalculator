package calculator.exceptions;

public class InvalidStrategy extends RuntimeException {

    public InvalidStrategy() {
        super("Strategy not supported");
    }

    public InvalidStrategy(String message) {
        super(message);
    }    

}
