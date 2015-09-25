package languagelearning.policies;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import languagelearning.actions.Action;
import languagelearning.states.State;

public class StateActionPolicy {
	private HashMap<State,HashMap<Action,Double>> values;
	private static final double NULL_VALUE = 0;
	private static final double INF_SMALL_VALUE = -999999999;
	
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
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		Set<State> states = values.keySet();
		Iterator<State> statesIt = states.iterator();
		while (statesIt.hasNext()) {
			State state = statesIt.next();
			buffer.append("STATE = " + state + "\n");
			
			Set<Action> actions = values.get(state).keySet();
			Iterator<Action> actionsIt = actions.iterator();
			while (actionsIt.hasNext()) {
				Action action = actionsIt.next();
				
				double value = getValue(state,action);
				buffer.append("   ACTION = " + action + " VALUE = " + value + "\n");
			}
		}
		
		return buffer.toString();
	}
}
