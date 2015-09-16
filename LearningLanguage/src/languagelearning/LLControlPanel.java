package languagelearning;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LLControlPanel extends JPanel
{
	private JCheckBox boundedCheckBox;
        private JSlider simSpeedSlider;
	
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
                
                simSpeedSlider = new JSlider(JSlider.HORIZONTAL);
                simSpeedSlider.setMinimum(Environment.SIM_SPEED_MIN);
                simSpeedSlider.setMaximum(Environment.SIM_SPEED_MAX);
                simSpeedSlider.addChangeListener(new ChangeListener()
                {
                    @Override
                    public void stateChanged(ChangeEvent e)
                    {
                        LearningLanguage ll = LearningLanguage.MAIN;
                        if(ll == null)
                        {
                            return;
                        }
                        Environment env = ll.getEnvironment();
                        if(env != null)
                        {
                            env.setSimulationSpeed(simSpeedSlider.getValue());
                        }
                    }
                });
                simSpeedSlider.setValue(Environment.SIM_SPEED_DEFAULT);
                simSpeedSlider.setToolTipText("Simulation speed (" + simSpeedSlider.getMinimum() + "-" + simSpeedSlider.getMaximum() + ")");
                
                add(simSpeedSlider);
                
	}
}
