package languagelearning.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import languagelearning.LearningLanguage;
import languagelearning.env.Environment;
import languagelearning.env.RunnableEnvironment;

public class LLControlPanel extends JPanel
{
	private JCheckBox boundedCheckBox;
        private JSlider simSpeedSlider;
        private JLabel simTimer;
        private JLabel totalDustIndicator;
        private NumberFormat ticksNF = new DecimalFormat("#,###,###,##0");
	private NumberFormat dustNF = new DecimalFormat("0.#");
        
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
                simSpeedSlider.setMinimum(RunnableEnvironment.SIM_SPEED_MIN);
                simSpeedSlider.setMaximum(RunnableEnvironment.SIM_SPEED_MAX);
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
                        RunnableEnvironment env = ll.getEnvironment();
                        if(env != null)
                        {
                            env.setSimulationSpeed(simSpeedSlider.getValue());
                        }
                    }
                });
                simSpeedSlider.setValue(RunnableEnvironment.SIM_SPEED_DEFAULT);
                simSpeedSlider.setToolTipText("Simulation speed (" + simSpeedSlider.getMinimum() + "-" + simSpeedSlider.getMaximum() + ")");
                
                add(simSpeedSlider);
                
                simTimer = new JLabel("0 ticks");
                simTimer.setToolTipText("Simulation time in ticks");
                
                add(simTimer);
                
                totalDustIndicator = new JLabel("?%");
                totalDustIndicator.setToolTipText("Total amount of dust in the environment in percentage.");
                
                add(totalDustIndicator);
	}
        
        public void start()
        {
        }
        
        public void stop()
        {
            // Do nothing
        }
        
        public void updateTime(long ticks)
        {
            simTimer.setText(ticksNF.format(ticks) + " ticks");
        }
        
        public void updateTotalDustPercentage(double dustPercentage)
        {
            totalDustIndicator.setText(dustNF.format(dustPercentage) + "%");
        }
}
