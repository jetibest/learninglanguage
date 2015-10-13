package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.states.StateVariable;
import languagelearning.util.BooleanMatrix;

/**
 *
 * @author yeti
 */
public class DefaultAgentsConfig extends AgentsConfig
{
    public DefaultAgentsConfig()
    {
        super();
        
        setAgentType(AgentType.QLEARNING);
                // SARSA & no Sound:        17.9 at 195K ticks
                // QLEANING & no Sound:     17.8 at 195K ticks
                // QLEARNING & Sound:       18.0 at 195K ticks
                // SARSA & Sound:           18.0 at 195K ticks
                // QLEARNING & Sound production & No sound detection    18.3    at 195K
                // QLEARNING & Sound detection & No Sound production    17.9    at 195K
                // QLEARNING & Sound & No normal collect dust           17.6    at 195K
        setAgentInitCount(8);
        setExplorationRate(0.1);
        setExplorationRateDecay(1);
        setDustCleanValue(5000);
        setDustPerceptionThreshold(1000);
        setLearningRate(0.1);
        setFutureRewardDiscountRate(0.95);
        setSharedPolicy(true);

        setPossibleActions(new Action[]{
                Action.TURN_RIGHT
                ,Action.TURN_LEFT
                ,Action.MOVE_FORWARD
                ,Action.COLLECT_DUST
                //,Action.PLACE_PHEROMONE_X
                //,Action.COLLECT_DUST_AND_PLACE_PHEROMONE_X
                //,Action.PRODUCE_SOUND_C
                //,Action.COLLECT_DUST_AND_PRODUCE_SOUND_C
        });
        setPossibleStateVariables(new StateVariable[]{
                //StateVariable.DUST_BELOW
                StateVariable.OBSTACLE_AHEAD
                //StateVariable.PHEROMONE_BELOW
                //StateVariable.PHEROMONE_AHEAD
                //,StateVariable.SOUND_C_BELOW
                //,StateVariable.SOUND_C_AHEAD
                //,StateVariable.SOUND_C_TWO_AHEAD
        });
        setSoundMatrix(BooleanMatrix.SQUARE_7x7);
        setPheromoneSize(100);
        setDebug(true);
    }
}
