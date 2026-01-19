package calculator.strategies;

import calculator.exceptions.InvalidExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Strategy3Tests {

    private Strategy3 strategy;

    @BeforeEach
    public void setUp() {
        strategy = new Strategy3();
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

}
