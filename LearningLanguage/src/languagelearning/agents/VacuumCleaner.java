package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.states.PredicateState;
import languagelearning.states.StateVariable;

public class VacuumCleaner extends Agent
{
	private int dustCleanValue = 5000;
	private int dustPerceptionThreshold = 1000;
	private boolean internalStateA;
	
	public VacuumCleaner(int x, int y)
	{
		super(x, y);
	}
	
        // Listen for now only to the square it is at
        // But we can also listen in squares around us, and then we would know a distance as well, and calculate intensity of the sound as the agent hears it
        public int listenToCloseSound()
        {
            return getEnvironment().getSoundValue(getX(), getY());
        }
        
        public void produceSound(int symbol)
        {
            Direction d = getDirection();
            Environment env = getEnvironment();
            int x = getX();
            int y = getY();
            if(d == Direction.EAST || d == Direction.WEST)
            {
                // this is now hardcoded, but this should be transferred into representation in 2-D table around the agent with 1 and 0 representing if there is sound or not
                // default direction is north, so turn matrix relative to direction
                // then just loop through the matrix
                int x1 = getNewXInDirection(d, 1);
                int x2 = getNewXInDirection(d, 2);
                int x3 = getNewXInDirection(d, 3);
                env.setSoundValue(x1, y, symbol);
                env.setSoundValue(x2, y, symbol);
                env.setSoundValue(x3, y, symbol);
                env.setSoundValue(x2, y + 1, symbol);
                env.setSoundValue(x2, y - 1, symbol);
                env.setSoundValue(x3, y + 1, symbol);
                env.setSoundValue(x3, y - 1, symbol);
                env.setSoundValue(x3, y + 2, symbol);
                env.setSoundValue(x3, y - 2, symbol);
            }
            else
            {
                int y1 = getNewYInDirection(d, 1);
                int y2 = getNewYInDirection(d, 2);
                int y3 = getNewYInDirection(d, 3);
                env.setSoundValue(x, y1, symbol);
                env.setSoundValue(x, y2, symbol);
                env.setSoundValue(x, y3, symbol);
                env.setSoundValue(x + 1, y2, symbol);
                env.setSoundValue(x - 1, y2, symbol);
                env.setSoundValue(x + 1, y3, symbol);
                env.setSoundValue(x - 1, y3, symbol);
                env.setSoundValue(x + 2, y3, symbol);
                env.setSoundValue(x - 2, y3, symbol);
            }
        }
        
	public int collectDust()
	{
		int dustBefore = getEnvironment().getDustValue(getX(), getY());
		int dustAfter = Math.max(getEnvironment().getDustMin(), getEnvironment().getDustValue(getX(), getY()) - dustCleanValue);
		getEnvironment().setDustValue(getX(), getY(), dustAfter);
		
        // on collecting dust, produce sound in direction it is headed
        // produceSound(3);
                
		int reward = dustBefore - dustAfter;
		return reward;
	}
        
	@Override
	public int doAction(Action action) {
		int reward = super.doAction(action);
		if (action == Action.COLLECT_DUST) {
			reward = reward + collectDust();
		} else if (action == Action.SET_INTERNAL_STATE_A) {
			reward = reward + setInternalStateA(true);
		} else if (action == Action.CLEAR_INTERNAL_STATE_A) {
			reward = reward + setInternalStateA(false);
		}
                else if(action == Action.PRODUCE_SOUND_A)
                {
                    produceSound(1);
                }
                else if(action == Action.PRODUCE_SOUND_B)
                {
                    produceSound(2);
                }
                else if(action == Action.PRODUCE_SOUND_C)
                {
                    produceSound(3);
                }
		return reward;
	}
	
	public boolean isDustBelow() {
		return getEnvironment().getDustValue(getX(), getY()) > dustPerceptionThreshold;
	}
	
	public boolean isDustInDirection(Direction direction,int step) {
		int xAhead = getNewXInDirection(direction,step);
		int yAhead = getNewYInDirection(direction,step);

		return getEnvironment().getDustValue(xAhead, yAhead) > dustPerceptionThreshold;
	}
	
	public boolean isObstacleInDirection(Direction direction) {
		int xAhead = getNewXInDirection(direction);
		int yAhead = getNewYInDirection(direction);

		return !getEnvironment().canMove(xAhead, yAhead);
	}
	
	public boolean isSoundBelow(int symbol) {
		return listenToCloseSound() == symbol;
	}
	
	public PredicateState getPredicateState(StateVariable[] possibleVariables) {
		PredicateState state = new PredicateState();
		
		for (StateVariable var: possibleVariables) {
			if (hasStateVariable(var)) {
				state.setVariable(var);
			}
		}
		
		return state;
	}
	
	public boolean hasStateVariable(StateVariable var) {
		if (StateVariable.DUST_BELOW == var) {
			return isDustBelow();
		} else if (StateVariable.DUST_AHEAD == var) {
			return isDustInDirection(getDirection(),1);
		} else if (StateVariable.DUST_TWO_AHEAD == var) {
			return isDustInDirection(getDirection(),2);
		} else if (StateVariable.DUST_NORTH == var) {
			return isDustInDirection(Direction.NORTH, 1);
		} else if (StateVariable.DUST_EAST == var) {
			return isDustInDirection(Direction.EAST, 1);
		} else if (StateVariable.DUST_SOUTH == var) {
			return isDustInDirection(Direction.SOUTH, 1);
		} else if (StateVariable.DUST_WEST == var) {
			return isDustInDirection(Direction.WEST, 1);
		} else if (StateVariable.OBSTACLE_AHEAD == var) {
			return isObstacleInDirection(getDirection());
		} else if (StateVariable.OBSTACLE_NORTH == var) {
			return isObstacleInDirection(Direction.NORTH);
		} else if (StateVariable.OBSTACLE_EAST == var) {
			return isObstacleInDirection(Direction.EAST);
		} else if (StateVariable.OBSTACLE_SOUTH == var) {
			return isObstacleInDirection(Direction.SOUTH);
		} else if (StateVariable.OBSTACLE_WEST == var) {
			return isObstacleInDirection(Direction.WEST);
		} else if (StateVariable.INTERNAL_STATE_A == var) {
			return internalStateA;
		} else if (StateVariable.SOUND_A_BELOW == var) {
			return isSoundBelow(1);
		} else if (StateVariable.SOUND_B_BELOW == var) {
			return isSoundBelow(2);
		} else if (StateVariable.SOUND_C_BELOW == var) {
			return isSoundBelow(3);
		} else {
			return false;
		}
	}

	public boolean isInternalStateA() {
		return internalStateA;
	}

	public int setInternalStateA(boolean internalStateA) {
		this.internalStateA = internalStateA;
		return 0; // Reward = 0
	}

	public int getDustCleanValue() {
		return dustCleanValue;
	}

	public void setDustCleanValue(int dustCleanValue) {
		this.dustCleanValue = dustCleanValue;
	}

	public int getDustPerceptionThreshold() {
		return dustPerceptionThreshold;
	}

	public void setDustPerceptionThreshold(int dustPerceptionThreshold) {
		this.dustPerceptionThreshold = dustPerceptionThreshold;
	}
	
	
}
