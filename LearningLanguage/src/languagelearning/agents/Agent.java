package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.env.Environment;

public class Agent extends GridObject
{
	// How do we implement:
	// Direction-based vs Sensors and move in any direction
	
	private Direction direction = Direction.NORTH;
	private boolean learning = true;
	
	public Agent(int x, int y)
	{
		super(x, y);
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
		} else if (action == Action.MOVE_NORTH) {
			return moveNorth();
		} else if (action == Action.MOVE_EAST) {
			return moveEast();
		} else if (action == Action.MOVE_SOUTH) {
			return moveSouth();
		} else if (action == Action.MOVE_WEST) {
			return moveWest();
		}
		// Reward = 0
		return 0;
	}

	public boolean isLearning() {
		return learning;
	}

	public void setLearning(boolean learning) {
		this.learning = learning;
	}
	
	
}
