package languagelearning;

public class LearningLanguage
{
	public static final LearningLanguage MAIN = new LearningLanguage();
	public static final int GRID_WIDTH = 32;
	public static final int GRID_HEIGHT = 20;
	public static final int GRID_SIZE = 20;
	
	private boolean isRunning;
	private LLWindow win;
	
	public LearningLanguage()
	{
		isRunning = true;
	}
	
	public void init(String[] args)
	{
		win = new LLWindow();
		
	}
	
	public void start()
	{
		log(this.getClass().getName(), "Started!");
		
		win.start();
	}
	
	public void stop()
	{
		isRunning = false;
		win.stop();
		
		log(this.getClass().getName(), "Stopped!");
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	public void log(String key, String msg)
	{
		System.out.println(key + ": " + msg);
	}
	
	public static void main(String[] args)
	{
		MAIN.init(args);
		MAIN.start();
		
	}
}
