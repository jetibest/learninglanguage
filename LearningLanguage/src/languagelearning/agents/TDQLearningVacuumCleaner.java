package languagelearning.agents;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.State;

/*
 * Temporal difference learning agent (using Q-Learning algorithm)
 */
public class TDQLearningVacuumCleaner extends VacuumCleaner {
	private static final double EXPLORATION_RATE = 0.1;
	private static final double LEARNING_RATE = 0.001; // the learning rate, set between 0 and 1. Setting it to 0 means that the Q-values are never updated, hence nothing is learned. Setting a high value such as 0.9 means that learning can occur quickly.
	private static final double FUTURE_REWARD_DISCOUNT_RATE = 0.999; // discount factor, also set between 0 and 1. This models the fact that future rewards are worth less than immediate rewards. Mathematically, the discount factor needs to be set less than 0 for the algorithm to converge.
	
	private StateActionPolicy policy;
	private Random rnd;
	
	public TDQLearningVacuumCleaner(int x, int y) {
		super(x, y);
		this.policy = new StateActionPolicy();
		this.rnd = new Random();
	}
	
	@Override
	public void run() {
		// Temporal difference algorithm based on: http://www.cse.unsw.edu.au/~cs9417ml/RL1/algorithms.html
		
		log("============================================");
		log("POLICY: \n" + policy);
		
		State state0 = getCurrentState();
		log("STATE: " + state0);
		
		// Choose action from state
		Action action0 = getEGreedyAction(state0);
		log("ACTION: " + action0);

		// Take action, observe reward
		double reward0 = doAction(action0);
		log("REWARD: " + reward0);
		// Observe new state'
		State state1 = getCurrentState();
		
		Action action1 = policy.getActionWithMaxValue(state1, getPossibleActions());
		
		// Update policy
		double value0 = policy.getValue(state0, action0);
		double value1 = policy.getValue(state1, action1);
		double valueDelta = LEARNING_RATE * (reward0 + (FUTURE_REWARD_DISCOUNT_RATE * value1) - value0);
		log("VALUE DELTA: " + valueDelta);
		double newValue0 = value0 + valueDelta;
		policy.setValue(state0, action0, newValue0);
	}
	
	private void log(String text) {
		System.out.println(text);
	}
	
	private State getCurrentState() {
		return getDustBelowAndObstacleAheadState();
	}
	
	private Action getEGreedyAction(State state) {
		Action[] possibleActions = getPossibleActions();

		if (rnd.nextDouble() <= EXPLORATION_RATE) {
			// Explore	
			return possibleActions[rnd.nextInt(possibleActions.length)];
		} else {
			return policy.getActionWithMaxValue(state, possibleActions);
		}
	}
	
	private Action[] getPossibleActions() {
		//Action[] actions = new Action[]{Action.MOVE_NORTH,Action.MOVE_EAST,Action.MOVE_SOUTH,Action.MOVE_WEST,Action.COLLECT_DUST};
		Action[] actions = new Action[]{Action.TURN_LEFT,Action.TURN_RIGHT,Action.MOVE_FORWARD, Action.COLLECT_DUST};
		// Shuffle
		Arrays.sort(actions,new Comparator<Action>() {

			@Override
			public int compare(Action o1, Action o2) {
				return (int)(rnd.nextInt(3) - 1);
			}});
		return actions;
	}
}
