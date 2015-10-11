package languagelearning.agents;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.State;

/*
 * Temporal difference learning agent (using SARSA algorithm)
 */
public class TDSarsaVacuumCleaner extends TDVacuumCleaner {
	private State state0;
	private Action action0;
	private State state1;
	private Action action1;
	
	public TDSarsaVacuumCleaner(StateActionPolicy sharedPolicy, int x, int y) {
		super(sharedPolicy, x, y);
	}
	
	public TDSarsaVacuumCleaner(int x, int y) {
		super(null, x, y);
	}
	
	@Override
	public void run() {
		StateActionPolicy policy = getPolicy();
		// Temporal difference algorithm based on: https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node64.html
		
		if (state0 == null) {
			// Initialize state
			state0 = getCurrentState();
			// Choose action from state
			action0 = getEGreedyAction(state0);
		}
		
		// Take action, observe reward
		double reward0 = doAction(action0);
		// Observe new state'
		state1 = getCurrentState();
		
		// Choose action' from state' 
		action1 = getEGreedyAction(state1);
		
		if (isLearning()) {
			// Update policy
			double value0 = policy.getValue(state0, action0);
			double value1 = policy.getValue(state1, action1);
			double valueDelta = getLearningRate() * (reward0 + (getFutureRewardDiscountRate() * value1) - value0);
			double newValue0 = value0 + valueDelta;
			policy.setValue(state0, action0, newValue0);
		}
		
		state0 = state1;
		action0 = action1;
	}
	
/*	@Override
	public int moveForward() {
		// Move and collect dust
		
		int reward = super.moveForward();
		reward = reward + collectDust();
		return reward;
	}*/
}
