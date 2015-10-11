package languagelearning.agents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.State;

/*
 * Temporal difference learning agent (using Q-Learning algorithm)
 */
public class TDQLearningVacuumCleaner extends TDVacuumCleaner {
	private long lastLogTimestamp;
	
	public TDQLearningVacuumCleaner(StateActionPolicy sharedPolicy,int x, int y) {
		super(sharedPolicy, x, y);	
	}
	
	public TDQLearningVacuumCleaner(int x, int y) {
		super(null, x, y);
	}
	
	@Override
	public void run() {
		boolean debug = false;
		if (System.currentTimeMillis() - lastLogTimestamp > 1000) {
			debug = isDebug();
			lastLogTimestamp = System.currentTimeMillis();
		}
		
		StateActionPolicy policy = getPolicy();
		// Temporal difference algorithm based on: http://www.cse.unsw.edu.au/~cs9417ml/RL1/algorithms.html
		
		if (debug) {
			log("============================================");
			log("Policy: \n" + policy);

			log("Exploration rate: " + getExplorationRate());
		}

		State state0 = getCurrentState();
		if (debug) {
			log("Current state: " + state0);
		}
		
		// Choose action from state
		Action action0 = getEGreedyAction(state0);
		if (debug) {
			log("Action: " + action0);
		}

		// Take action, observe reward
		double reward0 = doAction(action0);
		if (debug) {
			log("Reward: " + reward0);
		}
		// Observe new state'
		State state1 = getCurrentState();
		
		Action action1 = policy.getActionWithMaxValue(state1, getPossibleActionsInRandomOrder());
		
		if (isLearning()) {
			// Update policy
			double value0 = policy.getValue(state0, action0);
			double value1 = policy.getValue(state1, action1);
			double valueDelta = getLearningRate() * (reward0 + (getFutureRewardDiscountRate() * value1) - value0);
			if (debug) {
				log("Value difference: " + valueDelta);
			}
			double newValue0 = value0 + valueDelta;

			policy.setValue(state0, action0, newValue0);
			// Decay exploration
			setExplorationRate(getExplorationRate() * getExplorationRateDecay());
		}
		
		if (isDebug()) {
			policy.write(getPossibleActions());
		}
	}
}
