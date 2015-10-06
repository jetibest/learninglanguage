package languagelearning.policies;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import languagelearning.actions.Action;
import languagelearning.states.State;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StateActionPolicy {
	private HashMap<State,HashMap<Action,Double>> values;
	private static final double NULL_VALUE = 0;
	private static final double INF_SMALL_VALUE = -999999999;
	
	private int writeCount = 0;
	private Map<State, BufferedWriter> brs = new HashMap<State, BufferedWriter>();
	private Map<String, DescriptiveStatistics> stats = new HashMap<String, DescriptiveStatistics>();
	private File dir = new File("output" + System.currentTimeMillis());
	

	
	public StateActionPolicy() {
		this.values = new HashMap<State,HashMap<Action,Double>>();
	}
	
	public double getValue(State state,Action action) {
		HashMap<Action,Double> actionValues = values.get(state);
		if (actionValues != null) {
			Double value = actionValues.get(action);
			if (value != null) {
				return value;
			}
		}
		
		return NULL_VALUE;
	}
	
	public Action getActionWithMaxValue(State state,Action[] possibleActions) {
		double maxValue = INF_SMALL_VALUE;
		Action maxAction = null;
		for (Action action: possibleActions) {
			double value = getValue(state,action);
			if (value > maxValue) {
				maxValue = value;
				maxAction = action;
			}
		}
		return maxAction;
	}

	public void setValue(State state,Action action,double value) {
		HashMap<Action,Double> actionValues = values.get(state);
		if (actionValues == null) {
			actionValues = new HashMap<Action,Double>();
			values.put(state, actionValues);
		}
		actionValues.put(action, value);
	}
	
	public State[] getStates()
	{
		Set<State> states = values.keySet();
		return states.toArray(new State[]{});
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		Set<State> states = values.keySet();
		Iterator<State> statesIt = states.iterator();
		while (statesIt.hasNext()) {
			State state = statesIt.next();
			buffer.append("State = " + state + "\n");
			Action maxAction = getActionWithMaxValue(state,Action.values());
			
			Set<Action> actions = values.get(state).keySet();
			Iterator<Action> actionsIt = actions.iterator();
			while (actionsIt.hasNext()) {
				Action action = actionsIt.next();
				
				double value = getValue(state,action);
				buffer.append("   Action = " + action + " Value = " + value);
				if (action.equals(maxAction)) {
					buffer.append(" ***");
				}
				buffer.append("\n");
			}
		}
		
		return buffer.toString();
	}
	
	public void write(Action[] actions) {
		if (!dir.exists()) {
			dir.mkdir();
		}
		writeCount++;
		
		State[] states = getStates();
		for (State state: states) {
			for (Action action: actions) {
				DescriptiveStatistics stateActionStat = stats.get(state + "#" + action);
				if (stateActionStat == null) {
					stateActionStat = new DescriptiveStatistics();
					stats.put(state + "#" + action, stateActionStat);
				}
				stateActionStat.addValue(getValue(state,action));
			}
		}
		
		if(writeCount == 1000)
		{
			for(int i=0;i<states.length;i++)
			{
				BufferedWriter br = brs.get(states[i]);
				try
				{
					State state = states[i];
					StringBuilder actionValues = new StringBuilder();
					if(br == null)
					{
						br = new BufferedWriter(new FileWriter(new File(dir,"state-" + states[i] + ".csv")));
						actionValues.append(actions[0].name());
						for(int j=1;j<actions.length;j++)
						{
							actionValues.append(",").append(actions[j]);
						}
						actionValues.append('\n');
						brs.put(state, br);
					}
					
					for(int j=0;j<actions.length;j++)
					{
						if (j > 0) {
							actionValues.append(",");
						}
						double value = stats.get(state+"#"+actions[j]).getMean();
						actionValues.append(value);
					}
					actionValues.append('\n');
					br.write(actionValues.toString());
					br.flush();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

			writeCount = 0;
			stats.clear();
		}

	}
}
