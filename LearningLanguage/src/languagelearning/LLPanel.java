package languagelearning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class LLPanel extends JPanel implements Runnable
{
	/*
	 * Paint the grid and show agents/objects on it
	 * 
	 * */
	
	private int gridWidth;
	private int gridHeight;
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
		gridWidth = LearningLanguage.MAIN.GRID_WIDTH;
		gridHeight = LearningLanguage.MAIN.GRID_HEIGHT;
		panelWidth = gridWidth*LearningLanguage.MAIN.GRID_SIZE;
		panelHeight = gridHeight*LearningLanguage.MAIN.GRID_SIZE;
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
				Thread.sleep(100);
			}
			catch(Exception e)
			{
			}
		}
		
		
		LearningLanguage.MAIN.log(this.getClass().getName(), "Stopped!");
	}
}
