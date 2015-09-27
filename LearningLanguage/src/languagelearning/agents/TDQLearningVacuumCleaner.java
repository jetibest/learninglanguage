package languagelearning.agents;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.State;

/*
 * Temporal difference learning agent (using Q-Learning algorithm)
 */
public class TDQLearningVacuumCleaner extends TDVacuumCleaner {
	public TDQLearningVacuumCleaner(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void run() {
		StateActionPolicy policy = getPolicy();
		// Temporal difference algorithm based on: http://www.cse.unsw.edu.au/~cs9417ml/RL1/algorithms.html
		
		log("============================================");
		log("Policy: \n" + policy);
		
		State state0 = getCurrentState();
		log("Current state: " + state0);
		
		// Choose action from state
		Action action0 = getEGreedyAction(state0);
		log("Action: " + action0);

		// Take action, observe reward
		double reward0 = doAction(action0);
		log("Reward: " + reward0);
		// Observe new state'
		State state1 = getCurrentState();
		
		Action action1 = policy.getActionWithMaxValue(state1, getPossibleActionsInRandomOrder());
		
		// Update policy
		double value0 = policy.getValue(state0, action0);
		double value1 = policy.getValue(state1, action1);
		double valueDelta = getLearningRate() * (reward0 + (getFutureRewardDiscountRate() * value1) - value0);
		log("Value difference: " + valueDelta);
		double newValue0 = value0 + valueDelta;
		policy.setValue(state0, action0, newValue0);
	}
}
