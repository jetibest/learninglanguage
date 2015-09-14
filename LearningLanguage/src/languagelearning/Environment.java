package languagelearning;

import java.util.List;
import java.util.ArrayList;

public class Environment implements Runnable
{
	public static final int DUST_MAX = 100;
	public static final int DUST_MIN = 0;
	public static final int DUST_INCREMENT_VALUE = 1;
	public static final long DUST_INCREMENT_INTERVAL = 1000;
	public static final double DUST_START_PERCENTAGE = 0.6;
	public static final double DUST_VARIANCE_PERCENTAGE = 0.1;
	public static final int AGENTS_INIT_COUNT = 7;
	
	private List<GridObject> objects = new ArrayList<GridObject>();
	private int[][] dustgrid = new int[LearningLanguage.GRID_HEIGHT][LearningLanguage.GRID_WIDTH]; 
	private Thread t;
	
	public Environment()
	{
	}
	
	public void init()
	{
		for(int i=0;i<LearningLanguage.GRID_HEIGHT;i++)
		{
			int[] row = new int[LearningLanguage.GRID_WIDTH];
			for(int j=0;j<LearningLanguage.GRID_WIDTH;j++)
			{
				row[j] = (int) (DUST_MAX*DUST_START_PERCENTAGE + (Math.random()*2 - 1)*DUST_MAX*DUST_VARIANCE_PERCENTAGE);
			}
			dustgrid[i] = row;
		}
		
		for(int i=0;i<AGENTS_INIT_COUNT;i++)
		{
			int initX = (int) (Math.random()*LearningLanguage.GRID_WIDTH);
			int initY = (int) (Math.random()*LearningLanguage.GRID_HEIGHT);
			objects.add(new VacuumCleaner(initX, initY));
		}
		
		t = new Thread(this);
	}
	
	public void start()
	{
		t.start();
		
		for(int i=0;i<objects.size();i++)
		{
			GridObject go = objects.get(i);
			
			// Check if we need to start gridobject
			if(go instanceof Agent)
			{
				((Agent) go).start();
			}
		}
	}
	
	public void stop()
	{
		for(int i=0;i<objects.size();i++)
		{
			GridObject go = objects.get(i);
			
			// Check if we need to start gridobject
			if(go instanceof Agent)
			{
				((Agent) go).stop();
			}
		}
		
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
			
			for(int i=0;i<LearningLanguage.GRID_HEIGHT;i++)
			{
				int[] row = dustgrid[i];
				for(int j=0;j<LearningLanguage.GRID_WIDTH;j++)
				{
					row[j] = Math.min(row[j] + DUST_INCREMENT_VALUE, DUST_MAX);
				}
			}
			
			try
			{
				Thread.sleep(Math.max(0, DUST_INCREMENT_INTERVAL - (System.currentTimeMillis() - start)));
			}
			catch(Exception e)
			{
			}
		}
		
		LearningLanguage.MAIN.log(this.getClass().getName(), "Stopped!");
	}
	
	public boolean canMove(int x, int y)
	{
		// Physical properties of moving (thus not for sensing purposes!)
		for(int i=0;i<objects.size();i++)
		{
			GridObject go = objects.get(i);
			if(go.getX() == x && go.getY() == y)
			{
				return false;
			}
		}
		return true;
	}
	
	public void setDustValue(int x, int y, int value)
	{
		dustgrid[y][x] = value;
	}
	
	public int getDustValue(int x, int y)
	{
		return dustgrid[y][x];
	}
	
	public List<GridObject> getGridObjects()
	{
		return objects;
	}
	
	public int[][] getDustGrid()
	{
		return dustgrid;
	}
}
