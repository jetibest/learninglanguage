package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.states.PredicateState;
import languagelearning.states.StateVariable;
import languagelearning.util.BooleanMatrix;

public class VacuumCleaner extends Agent {
	private int dustCleanValue = 5000;
	private int dustPerceptionThreshold = 1000;
	private int pheromoneSize = 10;
	private boolean internalStateA;
	private BooleanMatrix soundMatrix = BooleanMatrix.TRIANGLE_7x5; // Pattern of sounds around agent (in north direction)

	public VacuumCleaner(int x, int y) {
		super(x, y);
	}

	public void setSoundMatrix(BooleanMatrix soundMatrix) {
		this.soundMatrix = soundMatrix;
	}
	
	// Listen for now only to the square it is at
	// But we can also listen in squares around us, and then we would know a
	// distance as well, and calculate intensity of the sound as the agent hears
	// it
	public int getSoundSymbolBelow() {
		return getEnvironment().getCurrentSoundValue(getX(), getY());
	}

	public int getSoundSymbolInDirection(Direction direction, int step) {
		int xAhead = getNewXInDirection(direction, step);
		int yAhead = getNewYInDirection(direction, step);

		return getEnvironment().getCurrentSoundValue(xAhead, yAhead);
	}

	public int getPheromoneBelow() {
		return getEnvironment().getPheromone(getX(), getY());
	}

	public int getPheromoneInDirection(Direction direction, int step) {
		int xAhead = getNewXInDirection(direction, step);
		int yAhead = getNewYInDirection(direction, step);

		return getEnvironment().getPheromone(xAhead, yAhead);
	}

	public boolean isPheromoneBelow() {
		return getPheromoneBelow() > 0;
	}

	public boolean isPheromoneInDirection(Direction direction, int step) {
		return getPheromoneInDirection(direction,step) > 0;
	}

	public int placePheromone() {
		getEnvironment().addPheromone(getX(), getY(), pheromoneSize);
		return 0; // Reward 0
	}
	
	public int produceSoundWithSoundMatrix(int symbol) {
		if (this.soundMatrix != null) {
			Environment env = getEnvironment();
			BooleanMatrix localMatrix = this.soundMatrix.rotateInDirection(getDirection());
			for (int deltaX = localMatrix.getMinRelativeX(); deltaX <= localMatrix.getMaxRelativeX(); deltaX++) {
				for (int deltaY = localMatrix.getMinRelativeY(); deltaY <= localMatrix.getMaxRelativeY(); deltaY++) {
					boolean value = localMatrix.getValueRelativeToMiddlePoint(deltaX, deltaY);
					if (value) {
						int xx = getNewX(deltaX);
						int yy = getNewY(deltaY);
						env.setSoundValue(xx, yy, symbol);
					}
				}				
			}
		}
		return 0; // No reward
	}

	public int collectDustWithoutSound() {
		return collectDustAndProduceSignal(0,Integer.MAX_VALUE);
	}

	public int collectDustAndProduceSignal(int symbol,int rewardThreshold) {
		int dustBefore = getEnvironment().getDustValue(getX(), getY());
		int dustAfter = Math.max(getEnvironment().getDustMin(),
				getEnvironment().getDustValue(getX(), getY()) - dustCleanValue);
		getEnvironment().setDustValue(getX(), getY(), dustAfter);

		int reward = dustBefore - dustAfter;

		if (reward >= rewardThreshold && symbol > 0) {
			produceSoundWithSoundMatrix(symbol);
		}

		return reward;
	}
	
	public int collectDustAndPlacePheromene(int rewardThreshold) {
		int dustBefore = getEnvironment().getDustValue(getX(), getY());
		int dustAfter = Math.max(getEnvironment().getDustMin(),
				getEnvironment().getDustValue(getX(), getY()) - dustCleanValue);
		getEnvironment().setDustValue(getX(), getY(), dustAfter);

		int reward = dustBefore - dustAfter;

		if (reward >= rewardThreshold) {
			placePheromone();
		}

		return reward;
	}
	
	@Override
	public int doAction(Action action) {
		int reward = super.doAction(action);
		if (action == Action.COLLECT_DUST) {
			reward = reward + collectDustWithoutSound();
		} else if (action == Action.SET_INTERNAL_STATE_A) {
			reward = reward + setInternalStateA(true);
		} else if (action == Action.CLEAR_INTERNAL_STATE_A) {
			reward = reward + setInternalStateA(false);
		} else if (action == Action.PRODUCE_SOUND_A) {
			reward = reward + produceSoundWithSoundMatrix(1);
		} else if (action == Action.PRODUCE_SOUND_B) {
			reward = reward + produceSoundWithSoundMatrix(2);
		} else if (action == Action.PRODUCE_SOUND_C) {
			reward = reward + produceSoundWithSoundMatrix(3);
		} else if (action == Action.COLLECT_DUST_AND_PRODUCE_SOUND_A) {
			reward = reward + collectDustAndProduceSignal(1,1);
		} else if (action == Action.COLLECT_DUST_AND_PRODUCE_SOUND_B) {
			reward = reward + collectDustAndProduceSignal(2,1);
		} else if (action == Action.COLLECT_DUST_AND_PRODUCE_SOUND_C) {
			reward = reward + collectDustAndProduceSignal(3,1);
		} else if (action == Action.PLACE_PHEROMONE_X) {
			reward = reward + placePheromone();
		} else if (action == Action.COLLECT_DUST_AND_PLACE_PHEROMONE_X) {
			reward = reward + collectDustAndPlacePheromene(1);
		}
		return reward;
	}

	public boolean isDustBelow() {
		return getEnvironment().getDustValue(getX(), getY()) > dustPerceptionThreshold;
	}

	public boolean isDustRight() {
		Direction d = getDirection().nextClockWise();
		return isDustInDirection(d,1);
	}

	public boolean isDustLeft() {
		Direction d = getDirection().nextCounterClockWise();
		return isDustInDirection(d,1);
	}

	public boolean isDustInDirection(Direction direction, int step) {
		int xAhead = getNewXInDirection(direction, step);
		int yAhead = getNewYInDirection(direction, step);

		return getEnvironment().getDustValue(xAhead, yAhead) > dustPerceptionThreshold;
	}

	public boolean isObstacleInDirection(Direction direction) {
		int xAhead = getNewXInDirection(direction);
		int yAhead = getNewYInDirection(direction);

		return !getEnvironment().canMove(xAhead, yAhead);
	}

	public boolean isSoundBelow(int symbol) {
		return getSoundSymbolBelow() == symbol;
	}

	public boolean isSoundLeft(int symbol) {
		return getSoundSymbolInDirection(getDirection().nextCounterClockWise(), 1) == symbol;
	}
	
	public boolean isSoundRight(int symbol) {
		return getSoundSymbolInDirection(getDirection().nextClockWise(), 1) == symbol;
	}
	
	public boolean isSoundInDirection(int symbol, int step) {
		return getSoundSymbolInDirection(getDirection(), step) == symbol;
	}

	public PredicateState getPredicateState(StateVariable[] possibleVariables) {
		PredicateState state = new PredicateState();

		for (StateVariable var : possibleVariables) {
			if (hasStateVariable(var)) {
				state.setVariable(var);
			}
		}

		return state;
	}

	public boolean hasStateVariable(StateVariable var) {
		if (StateVariable.DUST_BELOW == var) {
			return isDustBelow();
		} else if (StateVariable.DUST_RIGHT == var) {
			return isDustRight();
		} else if (StateVariable.DUST_LEFT == var) {
			return isDustLeft();
		} else if (StateVariable.DUST_AHEAD == var) {
			return isDustInDirection(getDirection(), 1);
		} else if (StateVariable.DUST_TWO_AHEAD == var) {
			return isDustInDirection(getDirection(), 2);
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
		} else if (StateVariable.SOUND_A_AHEAD == var) {
			return isSoundInDirection(1, 1);
		} else if (StateVariable.SOUND_B_AHEAD == var) {
			return isSoundInDirection(2, 1);
		} else if (StateVariable.SOUND_C_AHEAD == var) {
			return isSoundInDirection(3, 1);
		} else if (StateVariable.SOUND_A_TWO_AHEAD == var) {
			return isSoundInDirection(1, 2);
		} else if (StateVariable.SOUND_B_TWO_AHEAD == var) {
			return isSoundInDirection(2, 2);
		} else if (StateVariable.SOUND_C_TWO_AHEAD == var) {
			return isSoundInDirection(3, 2);
		} else if (StateVariable.SOUND_C_LEFT == var) {
			return isSoundLeft(3);
		} else if (StateVariable.SOUND_C_RIGHT == var) {
			return isSoundRight(3);
		} else if (StateVariable.PHEROMONE_BELOW == var) {
			return isPheromoneBelow();
		} else if (StateVariable.PHEROMONE_AHEAD == var) {
			return isPheromoneInDirection(getDirection(), 1);
		} else if (StateVariable.PHEROMONE_TWO_AHEAD == var) {
			return isPheromoneInDirection(getDirection(), 2);
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

	public int getPheromoneSize() {
		return pheromoneSize;
	}

	public void setPheromoneSize(int pheromoneSize) {
		this.pheromoneSize = pheromoneSize;
	}

}
