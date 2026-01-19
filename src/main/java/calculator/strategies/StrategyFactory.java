package calculator.strategies;

import calculator.exceptions.InvalidStrategy;

public class StrategyFactory {
    
    public static AbstractStrategy getStrategy(int strategy) {
        switch (strategy) {
            case 1:
                return new Strategy1();
            case 2:
                return new Strategy2();
            case 3:
                return new Strategy3();
            default:
                throw new InvalidStrategy();
        }
    }

}
