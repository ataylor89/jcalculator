package calculator.strategies;

import calculator.strategies.AbstractStrategy;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractStrategyTest {
    
    private AbstractStrategy strategy;

    @BeforeEach
    public void setUp() {
        strategy = new AbstractStrategy() {
            @Override
            public double eval(String expression) {
                return 0;
            }
        };
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/parse_expressions.csv")
    public void testExpressionParser(String input, String expected) {
        List<String> parseResult = strategy.parse(input);
        List<String> expectedResult = Arrays.asList(expected.split(" "));
        assertEquals(parseResult, expectedResult);
    }
}
