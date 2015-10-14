package languagelearning.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.State;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StatWriter {
	private int writeCount = 0;
	private Map<State, BufferedWriter> brs = new HashMap<State, BufferedWriter>();
	private Map<String, DescriptiveStatistics> stats = new HashMap<String, DescriptiveStatistics>();
	private BufferedWriter dpbr = null;
	private File dir;
	private Action[] actions = new Action[] {};

	public void setActions(Action[] actions) {
		this.actions = actions;
	}

	public StatWriter(File dir) {
		this.dir = dir;
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File file: files) {
				file.delete();
			}
		} else {
			dir.mkdir();
		}
	}

	public void write(StateActionPolicy policy,double totalDustPercentage) {
		writeCount++;

		DescriptiveStatistics dpscore = stats.get("dustpercentage");
		if (dpscore == null) {
			dpscore = new DescriptiveStatistics();
			stats.put("dustpercentage", dpscore);
		}
		dpscore.addValue(totalDustPercentage);

		State[] states = policy.getStates();
		for (State state : states) {
			for (Action action : actions) {
				DescriptiveStatistics stateActionStat = stats.get(state + "#"
						+ action);
				if (stateActionStat == null) {
					stateActionStat = new DescriptiveStatistics();
					stats.put(state + "#" + action, stateActionStat);
				}
				stateActionStat.addValue(policy.getValue(state, action));
			}
		}

		if (writeCount == 1000) {
			for (int i = 0; i < states.length; i++) {
				BufferedWriter br = brs.get(states[i]);
				try {
					State state = states[i];
					StringBuilder actionValues = new StringBuilder();
					if (br == null) {
						br = new BufferedWriter(new FileWriter(new File(dir,
								"state-" + states[i] + ".csv")));
						actionValues.append(actions[0].name());
						for (int j = 1; j < actions.length; j++) {
							actionValues.append(",").append(actions[j]);
						}
						actionValues.append('\n');
						brs.put(state, br);
					}

					for (int j = 0; j < actions.length; j++) {
						if (j > 0) {
							actionValues.append(",");
						}
						double value = stats.get(state + "#" + actions[j])
								.getMean();
						actionValues.append(value);
					}
					actionValues.append('\n');
					br.write(actionValues.toString());
					br.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Print the dust percentage score in separate file
			try {
				StringBuilder actionValues = new StringBuilder();
				if (dpbr == null) {
					dpbr = new BufferedWriter(new FileWriter(new File(dir,
							"dustpercentage.csv")));
				}
				actionValues.append(stats.get("dustpercentage").getMean())
						.append('\n');
				dpbr.write(actionValues.toString());
				dpbr.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Write policy
			try {
				FileUtils.writeStringToFile(new File(dir,
						"policy.txt"), policy.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			writeCount = 0;
			stats.clear();
		}

	}
}
