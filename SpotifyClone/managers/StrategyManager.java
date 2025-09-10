package managers;

import enums.PlayStrategyType;
import strategies.CustomQueueStrategy;
import strategies.PlayStrategy;
import strategies.RandomPlayStrategy;
import strategies.SequentialPlayStrategy;

public class StrategyManager {
    private static StrategyManager instance;
    private SequentialPlayStrategy sequentialStrategy;
    private RandomPlayStrategy randomStrategy;
    private CustomQueueStrategy customQueueStrategy;

    private StrategyManager(){
        sequentialStrategy = new SequentialPlayStrategy();
        randomStrategy = new RandomPlayStrategy();
        customQueueStrategy = new CustomQueueStrategy();
    }

    public static StrategyManager getInstance(){
        if(instance == null){
            instance = new StrategyManager();
        }
        return instance;
    }

    public PlayStrategy getStrategy(PlayStrategyType type){
        if (type == PlayStrategyType.SEQUENTIAL) {
            return sequentialStrategy;
        } else if(type == PlayStrategyType.RANDOM){
            return randomStrategy;
        } else {
            return customQueueStrategy;
        }
    }
}
