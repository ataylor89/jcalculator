package calculator;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    
    private Parser parser;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/parse_expressions.csv")
    public void testExpressionParser(String input, String expected) {
        List<String> parseResult = parser.parse(input);
        List<String> expectedResult = Arrays.asList(expected.split(" "));
        assertEquals(parseResult, expectedResult);
    }
}
