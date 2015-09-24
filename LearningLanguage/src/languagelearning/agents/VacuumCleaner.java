package languagelearning.agents;

import languagelearning.LearningLanguage;
import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.states.LookAheadState;

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
			reward = collectDust();
		}
		return reward;
	}
	
	public LookAheadState getLookAheadState() {
		// Next grid square in direction of vacuum cleaner
		int xAhead = getNewXInDirection(getDirection());
		int yAhead = getNewYInDirection(getDirection());

		boolean dustAhead = getEnvironment().getDustValue(xAhead, yAhead) > 0;
		boolean obstacleAhead = !getEnvironment().canMove(xAhead, yAhead);
		LookAheadState state = new LookAheadState(obstacleAhead,dustAhead);
		
		return state;
	}
}
