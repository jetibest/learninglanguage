package languagelearning;

public class Agent extends GridObject
{
	// How do we implement:
	// Direction-based vs Sensors and move in any direction
	
	// 0,1,2,3 -> NORTH, EAST, SOUTH, WEST
	public static final int DIRECTION_NORTH = 0;
	public static final int DIRECTION_EAST = 1;
	public static final int DIRECTION_SOUTH = 2;
	public static final int DIRECTION_WEST = 3;
	
	private int direction = DIRECTION_NORTH;
	
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
		direction = (direction + 3)%4;
	}
	
	public void turnRight()
	{
		direction = (direction + 1)%4;
	}
	
	public int getDirection()
	{
		return direction;
	}
	
	public void moveForward()
	{
		// Try to change x,y in direction
		if(direction == 0)
		{
			moveNorth();
		}
		else if(direction == 1)
		{
			moveEast();
		}
		else if(direction == 2)
		{
			moveSouth();
		}
		else if(direction == 3)
		{
			moveWest();
		}
	}
}
