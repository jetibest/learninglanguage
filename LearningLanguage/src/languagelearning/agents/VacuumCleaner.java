package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.states.PredicateState;
import languagelearning.states.StateVariable;

public class VacuumCleaner extends Agent
{
	public static final int DUST_CLEAN_VALUE = 500;
	
	public VacuumCleaner(int x, int y)
	{
		super(x, y);
	}
	
	public int collectDust()
	{
		int dustBefore = getEnvironment().getDustValue(getX(), getY());
		int dustAfter = Math.max(Environment.DUST_MIN, getEnvironment().getDustValue(getX(), getY()) - DUST_CLEAN_VALUE);
		getEnvironment().setDustValue(getX(), getY(), dustAfter);
		
		int reward = dustBefore - dustAfter;
		return reward;
	}
	
        @Override
	public void run()
	{	
                // Move around, and sometimes randomly change direction
                // And collect dust on the way
                collectDust();

                if(Math.random() < 0.1)
                {
                        if(Math.random() < 0.5)
                        {
                                turnLeft();
                        }
                        else
                        {
                                turnRight();
                        }
                }
                else
                {
                        moveForward();
                }
			
	}
        
	@Override
	public int doAction(Action action) {
		int reward = super.doAction(action);
		if (action == Action.COLLECT_DUST) {
			reward = reward + collectDust();
		}
		return reward;
	}
	
	public boolean isDustBelow() {
		return getEnvironment().getDustValue(getX(), getY()) > 0;
	}
	
	public boolean isDustInDirection(Direction direction,int step) {
		int xAhead = getNewXInDirection(direction,step);
		int yAhead = getNewYInDirection(direction,step);

		return getEnvironment().getDustValue(xAhead, yAhead) > 0;
	}
	
	public boolean isObstacleInDirection(Direction direction) {
		int xAhead = getNewXInDirection(direction);
		int yAhead = getNewYInDirection(direction);

		return !getEnvironment().canMove(xAhead, yAhead);
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
		} else {
			return false;
		}
	}
}
