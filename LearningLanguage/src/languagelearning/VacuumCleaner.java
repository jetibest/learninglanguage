package languagelearning;

public class VacuumCleaner extends Agent
{
	public static final int DUST_CLEAN_VALUE = 30;
	private Environment env;
	
	public VacuumCleaner(int x, int y)
	{
		super(x, y);
		
		env = LearningLanguage.MAIN.getEnvironment();
	}
	
	private void collectDust()
	{
		env.setDustValue(getX(), getY(), Math.max(Environment.DUST_MIN, env.getDustValue(getX(), getY()) - DUST_CLEAN_VALUE));
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
}