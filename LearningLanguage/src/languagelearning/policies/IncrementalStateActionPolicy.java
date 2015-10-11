package languagelearning.policies;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import languagelearning.actions.Action;
import languagelearning.states.State;

public class IncrementalStateActionPolicy extends Policy {
	private HashMap<State,HashMap<Action,int[]>> values;
	
	public IncrementalStateActionPolicy() {
		this.values = new HashMap<State,HashMap<Action,int[]>>();
	}
	
	public double getValue(State state,Action action) {
		HashMap<Action,int[]> actionValues = values.get(state);
		if (actionValues != null) {
			int[] rewardCountAndAmount = actionValues.get(action);
			if (rewardCountAndAmount != null) {
				int count = rewardCountAndAmount[0];
				int amount = rewardCountAndAmount[1];
				if (count > 0) {
					return (double)amount / (double)count; // Return average reward
				}
			}
		}
		
		return 0;
	}
	
	public void addReward(State state,Action action,int reward) {
		HashMap<Action,int[]> actionValues = values.get(state);
		if (actionValues == null) {
			actionValues = new HashMap<Action,int[]>();
			values.put(state, actionValues);
		}
		int[] rewardCountAndAmount = actionValues.get(action);
		if (rewardCountAndAmount == null) {
			rewardCountAndAmount = new int[]{0,0};
			actionValues.put(action, rewardCountAndAmount);
		}
		rewardCountAndAmount[0]++;
		rewardCountAndAmount[1] = rewardCountAndAmount[1] + reward;
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
