package languagelearning;

import javax.swing.JFrame;

public class LLWindow extends JFrame
{
	/*
	 * Main window for LLPanel and parameter-controls/settings etc.
	 * 
	 */
	
	private LLPanel llPanel;
	
	public LLWindow()
	{
		init();
	}
	
	public void stop()
	{
		llPanel.stop();
	}
	
	public void start()
	{
		llPanel.start();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		LearningLanguage.MAIN.stop();
	}
	
	private void init()
	{
		llPanel = new LLPanel();
		
		setTitle("Learning Language");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		getContentPane().add(llPanel);
		
		setVisible(true);
		pack();
	}
}
