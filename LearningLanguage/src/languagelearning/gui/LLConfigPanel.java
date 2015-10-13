package languagelearning.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import languagelearning.LearningLanguage;

public class LLConfigPanel extends JPanel
{
	private JList<String> list;

	public LLConfigPanel() {
		String[] configNames = getConfigNames();
		list = new JList<String>(configNames);
		String currentConfigName = LearningLanguage.MAIN.getCurrentConfigName();
		for (int i = 0; i < configNames.length; i++) {
			if (configNames[i].equals(currentConfigName)) {
				list.setSelectedIndex(i);
			}
		}
		
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
			    if (e.getValueIsAdjusting() == false) {
			        String config = list.getSelectedValue();
			        
			        LearningLanguage.MAIN.stop();
					try {
						LearningLanguage.MAIN.init(config);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					LearningLanguage.MAIN.start();

			    }
			}
		});
		
		add(list);
	}
        
    public void start()
    {
    }
    
    public void stop()
    {
        // Do nothing
    }
    
    public String[] getConfigNames() {
    	File dir = new File(".");
    	File[] files = dir.listFiles();
    	List<String> names = new ArrayList<String>();
    	for (File file: files) {
    		if (file.getName().endsWith(".txt")) {
    			String name = file.getName().replace(".txt", "");
    			names.add(name);
    		}
    	}
    	return names.toArray(new String[names.size()]);
    }
}
