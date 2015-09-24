package languagelearning.policies;

import java.util.HashMap;

import languagelearning.actions.Action;
import languagelearning.states.State;

public class IncrementalStateActionPolicy {
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
}
