package languagelearning;

public class Agent extends GridObject implements Runnable
{
	public static final long AGENT_INTERVAL = 200;
	
	// How do we implement:
	// Direction-based vs Sensors and move in any direction
	
	// 0,1,2,3 -> NORTH, EAST, SOUTH, WEST
	private int direction = 0;
	private Thread t;
	
	public Agent(int x, int y)
	{
		super(x, y);
		
		init();
	}
	
	private void init()
	{
		t = new Thread(this);
	}
	
	public void start()
	{
		t.start();
	}
	
	public void stop()
	{
		// No interrupt required here
	}
	
	@Override
	public void run()
	{
		LearningLanguage.MAIN.log(this.getClass().getName(), "Started!");
		
		long start;
		while(LearningLanguage.MAIN.isRunning())
		{
			start = System.currentTimeMillis();
			
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
			
			try
			{
				Thread.sleep(Math.max(0, AGENT_INTERVAL - (System.currentTimeMillis() - start)));
			}
			catch(Exception e)
			{
			}
		}
		
		LearningLanguage.MAIN.log(this.getClass().getName(), "Stopped!");
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
