package languagelearning;

public class VacuumCleaner extends Agent
{
	public static final int DUST_CLEAN_VALUE = 500;
	public Environment env;
	
	public VacuumCleaner(int x, int y)
	{
		super(x, y);
		
		env = LearningLanguage.MAIN.getEnvironment();
	}
	
	public void collectDust()
	{
		env.setDustValue(getX(), getY(), Math.max(Environment.DUST_MIN, env.getDustValue(getX(), getY()) - DUST_CLEAN_VALUE));
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
}
