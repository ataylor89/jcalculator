package calculator.strategies;

import calculator.Parser;
import calculator.exceptions.InvalidExpression;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Strategy1Tests {

    private Parser parser;
    private Strategy1 strategy;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
        strategy = new Strategy1();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/valid_inputs.csv")
    public void testEvalWithValidInputs(String input, String expected) {
        double result = strategy.eval(input);
        double expectedResult = Double.parseDouble(expected);
        assertEquals(result, expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalid_inputs.csv")
    public void testEvalWithInvalidInputs(String input) {
        assertThrows(InvalidExpression.class, () -> {
            strategy.eval(input);
        });
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/infix_to_postfix.csv")
    public void testInfixToPostfix(String input, String expected) {
        List<String> tokens = parser.parse(input);
        List<String> postfix = strategy.convertToPostfix(tokens);
        List<String> expectedResult = Arrays.asList(expected.split(" "));
        assertEquals(postfix, expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/eval_postfix.csv")
    public void evalPostfix(String input, String expected) {
        List<String> postfix = Arrays.asList(input.split(" "));
        double result = strategy.evalPostfix(postfix);
        double expectedResult = Double.parseDouble(expected);
        assertEquals(result, expectedResult);
    }
}
