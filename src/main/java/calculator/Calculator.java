package calculator;

import calculator.strategies.Strategy;
import calculator.strategies.StrategyFactory;
import calculator.exceptions.InvalidExpression;
import calculator.exceptions.InvalidStrategy;
import java.io.Console;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Calculator {

    public void evalAndPrint(String expression, int strategy) {
        try {
            Strategy s = StrategyFactory.getStrategy(strategy);
            double result = s.eval(expression);
            if (result % 1 == 0) {
                System.out.println((int) result);
            }
            else {
                System.out.println(result);
            }
        } catch (InvalidExpression | InvalidStrategy e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println("There was an error while evaluating the expression");
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Options options = new Options();
        options.addOption(Option.builder("s")
            .longOpt("strategy")
            .desc("The strategy for evaluating arithmetic expressions")
            .hasArg()
            .type(Integer.class)
            .required(false)
            .build());
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmdLine = parser.parse(options, args);
            String[] positionalArgs = cmdLine.getArgs();
            Integer strategy = (Integer) cmdLine.getParsedOptionValue("s", 1);
            if (positionalArgs.length == 1 && positionalArgs[0] != null) {
                String expression = positionalArgs[0].strip();
                calculator.evalAndPrint(expression, strategy);
            }
            else {
                Console console = System.console();
                while (true) {
                    String userInput = console.readLine();
                    if (userInput == null) {
                        break;
                    }
                    userInput = userInput.strip();
                    if (userInput.equals("exit")) {
                        break;
                    }
                    calculator.evalAndPrint(userInput, strategy);
                }
            }
        } catch (ParseException e) {
            System.out.println(e);
        }
    }
}
