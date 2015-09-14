package languagelearning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

public class LLPanel extends JPanel implements Runnable
{
	/*
	 * Paint the grid and show agents/objects on it
	 * 
	 * */
	
	private int gridWidth;
	private int gridHeight;
	private int gridSize;
	private int panelWidth;
	private int panelHeight;
	private Dimension panelSize;
	private Thread t;
	
	public LLPanel()
	{
		init();
	}
	
	private void init()
	{
		gridSize = LearningLanguage.GRID_SIZE;
		gridWidth = LearningLanguage.GRID_WIDTH;
		gridHeight = LearningLanguage.GRID_HEIGHT;
		panelWidth = gridWidth*LearningLanguage.GRID_SIZE;
		panelHeight = gridHeight*LearningLanguage.GRID_SIZE;
		panelSize = new Dimension(panelWidth, panelHeight);
		setPreferredSize(panelSize);
		
		t = new Thread(this);
	}
	
	public void start()
	{
		t.start();
	}
	
	public void stop()
	{
		// No interrupt required, 100 ms loop.
	}
	
	@Override
	public void paintComponent(Graphics g1D)
	{
		Graphics2D g = (Graphics2D) g1D;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panelWidth, panelHeight);
		
		if(LearningLanguage.MAIN.isRunning())
		{
			int[][] dustgrid = LearningLanguage.MAIN.getEnvironment().getDustGrid();
			for(int i=0;i<gridHeight;i++)
			{
				int[] dustrow = dustgrid[i];
				for(int j=0;j<gridWidth;j++)
				{
					int value = dustrow[j];
					Color c = new Color((int) (255.0D*value/Environment.DUST_MAX), 0, 0);
					g.setColor(c);
					g.fillRect(j*gridSize, i*gridSize, gridSize, gridSize);
				}
			}
			
			List<GridObject> gridObjects = LearningLanguage.MAIN.getEnvironment().getGridObjects();
			for(int i=0;i<gridObjects.size();i++)
			{
				GridObject go = gridObjects.get(i);
				
				g.setColor(Color.GREEN);
				g.fillOval(go.getX()*gridSize, go.getY()*gridSize, gridSize, gridSize);
			}
		}
	}
	
	@Override
	public void run()
	{
		LearningLanguage.MAIN.log(this.getClass().getName(), "Started!");
		
		while(LearningLanguage.MAIN.isRunning())
		{
			repaint();
			
			try
			{
				Thread.sleep(50);
			}
			catch(Exception e)
			{
			}
		}
		
		
		LearningLanguage.MAIN.log(this.getClass().getName(), "Stopped!");
	}
}
