package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.states.DustBelowState;
import languagelearning.states.DustAheadBelowAndObstacleAheadState;
import languagelearning.states.DustAroundBelowAndObstacleAroundState;
import languagelearning.states.DustTwoAheadBelowAndObstacleAheadState;

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
	
	public DustBelowState getDustBelowState() {
		DustBelowState state = new DustBelowState();
		state.setDustBelow(getEnvironment().getDustValue(getX(), getY()) > 0);
		
		return state;
	}

	public DustAheadBelowAndObstacleAheadState getDustAheadBelowAndObstacleAheadState() {
		// Next grid square in direction of vacuum cleaner
		int xAhead = getNewXInDirection(getDirection());
		int yAhead = getNewYInDirection(getDirection());

		DustAheadBelowAndObstacleAheadState state = new DustAheadBelowAndObstacleAheadState();
		state.setObstacleAhead(!getEnvironment().canMove(xAhead, yAhead));
		state.setDustAhead(getEnvironment().getDustValue(xAhead, yAhead) > 0);
		state.setDustBelow(getEnvironment().getDustValue(getX(), getY()) > 0);
		
		return state;
	}

	public DustTwoAheadBelowAndObstacleAheadState getDustTwoAheadBelowAndObstacleAheadState() {
		// Next grid square in direction of vacuum cleaner
		int x1Ahead = getNewXInDirection(getDirection(),1);
		int y1Ahead = getNewYInDirection(getDirection(),1);
		int x2Ahead = getNewXInDirection(getDirection(),2);
		int y2Ahead = getNewYInDirection(getDirection(),2);

		DustTwoAheadBelowAndObstacleAheadState state = new DustTwoAheadBelowAndObstacleAheadState();
		state.setObstacleAhead(!getEnvironment().canMove(x1Ahead, y1Ahead));
		state.setDustAhead(getEnvironment().getDustValue(x1Ahead, y1Ahead) > 0);
		state.setDustTwoAhead(getEnvironment().getDustValue(x2Ahead, y2Ahead) > 0);
		state.setDustBelow(getEnvironment().getDustValue(getX(), getY()) > 0);
		
		return state;
	}

	public DustAroundBelowAndObstacleAroundState getDustAroundBelowAndObstacleAroundState() {
		int xNorth = getNewXInDirection(Direction.NORTH);
		int yNorth = getNewYInDirection(Direction.NORTH);

		int xEast = getNewXInDirection(Direction.EAST);
		int yEast = getNewYInDirection(Direction.EAST);

		int xSouth = getNewXInDirection(Direction.SOUTH);
		int ySouth = getNewYInDirection(Direction.SOUTH);
		
		int xWest = getNewXInDirection(Direction.WEST);
		int yWest = getNewYInDirection(Direction.WEST);
		
		DustAroundBelowAndObstacleAroundState state = new DustAroundBelowAndObstacleAroundState();
		state.setObstacleNorth(!getEnvironment().canMove(xNorth, yNorth));
		state.setObstacleEast(!getEnvironment().canMove(xEast, yEast));
		state.setObstacleSouth(!getEnvironment().canMove(xSouth, ySouth));
		state.setObstacleWest(!getEnvironment().canMove(xWest, yWest));

		state.setDustNorth(getEnvironment().getDustValue(xNorth, yNorth) > 0);
		state.setDustEast(getEnvironment().getDustValue(xEast, yEast) > 0);
		state.setDustSouth(getEnvironment().getDustValue(xSouth, ySouth) > 0);
		state.setDustWest(getEnvironment().getDustValue(xWest, yWest) > 0);

		state.setDustBelow(getEnvironment().getDustValue(getX(), getY()) > 0);
		
		return state;
	}
}
