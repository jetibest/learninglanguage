package languagelearning;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class LLControlPanel extends JPanel
{
	private JCheckBox boundedCheckBox;
	
	public LLControlPanel() {
		super(new FlowLayout(FlowLayout.LEFT));
		
		boundedCheckBox = new JCheckBox(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				LearningLanguage.MAIN.getEnvironment().setBounded(checkBox.isSelected());
			}});
		boundedCheckBox.setText("Bounded (with walls)");
		boundedCheckBox.setSelected(true); // should take from environment, but MAIN not yet created...
		
		add(boundedCheckBox);
	}
}
