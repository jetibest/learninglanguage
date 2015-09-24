package languagelearning.agents;

import languagelearning.actions.Action;

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
	
	public int turnLeft()
	{
		direction = direction.nextCounterClockWise();
		return 0; // Reward = 0
	}
	
	public int turnRight()
	{
		direction = direction.nextClockWise();
		return 0; // Reward = 0
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public int moveForward()
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
		return 0; // Reward = 0
	}
	
	public int doAction(Action action) {
		if (action == Action.TURN_LEFT) {
			return turnLeft();
		} else if (action == Action.TURN_RIGHT) {
			return turnRight();
		} else if (action == Action.MOVE_FORWARD) {
			return moveForward();
		}
		// Reward = 0
		return 0;
	}
}
