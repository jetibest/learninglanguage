package languagelearning.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import languagelearning.LearningLanguage;

public class LLWindow extends JFrame
{
	/*
	 * Main window for LLPanel and parameter-controls/settings etc.
	 * 
	 */
	
	private LLPanel llPanel;
	private LLControlPanel llControlPanel;
	
	public LLWindow()
	{
		init();
	}
        
        public LLPanel getVisualPanel()
        {
            return llPanel;
        }
        
        public LLControlPanel getControlPanel()
        {
            return llControlPanel;
        }
	
	public void stop()
	{
		llPanel.stop();
                llControlPanel.stop();
	}
	
	public void start()
	{
		llPanel.start();
                llControlPanel.start();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		LearningLanguage.MAIN.stop();
	}
	
	private void init()
	{
		setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		setTitle("Learning Language");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		llPanel = new LLPanel();
		getContentPane().add(llPanel);
		
		llControlPanel = new LLControlPanel();
		getContentPane().add(llControlPanel);
		
		pack();
		setVisible(true);
	}
}
