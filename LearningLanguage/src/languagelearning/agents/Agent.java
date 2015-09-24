package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.actions.AgentAction;

public class Agent extends GridObject
{
	// How do we implement:
	// Direction-based vs Sensors and move in any direction
	
	private Direction direction = Direction.NORTH;
	
	public Agent(int x, int y)
	{
		super(x, y);
		
		init();
	}
	
	private void init()
	{
	}
	
        @Override
	public void run()
	{
                // Move around, and sometimes randomly change direction

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
	
	public void turnLeft()
	{
		direction = direction.nextCounterClockWise();
	}
	
	public void turnRight()
	{
		direction = direction.nextClockWise();
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public void moveForward()
	{
		// Try to change x,y in direction
		if(direction == Direction.NORTH)
		{
			moveNorth();
		}
		else if(direction == Direction.EAST)
		{
			moveEast();
		}
		else if(direction == Direction.SOUTH)
		{
			moveSouth();
		}
		else if(direction == Direction.WEST)
		{
			moveWest();
		}
	}
	
	public void doAction(Action action) {
		if (action == AgentAction.TURN_LEFT) {
			turnLeft();
		} else if (action == AgentAction.TURN_RIGHT) {
			turnRight();
		} else if (action == AgentAction.MOVE_FORWARD) {
			moveForward();
		}
	}
}
