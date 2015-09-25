package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.states.LookAheadState;
import languagelearning.states.LookAroundState;

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
	
	public LookAheadState getLookAheadState() {
		// Next grid square in direction of vacuum cleaner
		int xAhead = getNewXInDirection(getDirection());
		int yAhead = getNewYInDirection(getDirection());

		LookAheadState state = new LookAheadState();
		state.setObstacleAhead(!getEnvironment().canMove(xAhead, yAhead));
		state.setDustAhead(getEnvironment().getDustValue(xAhead, yAhead) > 0);
		state.setDustBelow(getEnvironment().getDustValue(getX(), getY()) > 0);
		
		return state;
	}

	public LookAroundState getLookAroundState() {
		int xNorth = getNewXInDirection(Direction.NORTH);
		int yNorth = getNewYInDirection(Direction.NORTH);

		int xEast = getNewXInDirection(Direction.EAST);
		int yEast = getNewYInDirection(Direction.EAST);

		int xSouth = getNewXInDirection(Direction.SOUTH);
		int ySouth = getNewYInDirection(Direction.SOUTH);
		
		int xWest = getNewXInDirection(Direction.WEST);
		int yWest = getNewYInDirection(Direction.WEST);
		
		LookAroundState state = new LookAroundState();
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
