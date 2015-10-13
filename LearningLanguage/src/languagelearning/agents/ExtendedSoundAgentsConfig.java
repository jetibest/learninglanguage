package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.states.StateVariable;

/**
 *
 * @author yeti
 */
public class ExtendedSoundAgentsConfig extends DefaultAgentsConfig
{
    public ExtendedSoundAgentsConfig()
    {
        setPossibleActions(new Action[]{
                Action.TURN_RIGHT
                ,Action.TURN_LEFT
                ,Action.MOVE_FORWARD
                ,Action.COLLECT_DUST
                //,Action.PLACE_PHEROMONE_X
                //,Action.COLLECT_DUST_AND_PLACE_PHEROMONE_X
                ,Action.PRODUCE_SOUND_C
                ,Action.COLLECT_DUST_AND_PRODUCE_SOUND_C
        });
        setPossibleStateVariables(new StateVariable[]{
                StateVariable.DUST_BELOW
                ,StateVariable.OBSTACLE_AHEAD
                //StateVariable.PHEROMONE_BELOW
                //StateVariable.PHEROMONE_AHEAD
                ,StateVariable.SOUND_C_BELOW
                ,StateVariable.SOUND_C_AHEAD
                ,StateVariable.SOUND_C_TWO_AHEAD
        });
    }
}
