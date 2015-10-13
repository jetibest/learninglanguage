package languagelearning.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import languagelearning.LearningLanguage;

public class LLWindow extends JFrame {
	/*
	 * Main window for LLPanel and parameter-controls/settings etc.
	 */

	private LLGridPanel llPanel;
	private LLControlPanel llControlPanel;
	private LLConfigPanel llConfigPanel;

	public LLWindow() {
		init();
	}

	public LLGridPanel getGridPanel() {
		return llPanel;
	}

	public LLControlPanel getControlPanel() {
		return llControlPanel;
	}

	public LLConfigPanel getConfigPanel() {
		return llConfigPanel;
	}

	public void stop() {
		llPanel.stop();
		llControlPanel.stop();
		llConfigPanel.stop();
	}

	public void start() {
		llPanel.start();
		llControlPanel.start();
		llConfigPanel.start();
	}

	@Override
	public void dispose() {
		super.dispose();

		LearningLanguage.MAIN.stop();
	}

	private void init() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		setTitle("Learning Language");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		getContentPane().add(leftPanel);
		
		llPanel = new LLGridPanel();
		leftPanel.add(llPanel);

		llControlPanel = new LLControlPanel();
		leftPanel.add(llControlPanel);

		llConfigPanel = new LLConfigPanel();
		getContentPane().add(llConfigPanel);
		
		pack();
		setVisible(true);
	}
}
