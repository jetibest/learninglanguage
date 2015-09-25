package languagelearning.agents;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.State;

/*
 * Temporal difference learning agent (using SARSA algorithm)
 */
public class TDSarsaVacuumCleaner extends VacuumCleaner {
	private static final double EXPLORATION_RATE = 0.1;
	private static final double LEARNING_RATE = 0.1; // alpha
	private static final double FUTURE_REWARD_DISCOUNT_RATE = 0.9; // gamma
	
	private StateActionPolicy policy;
	private Random rnd;
	private State state0;
	private Action action0;
	private State state1;
	private Action action1;
	
	public TDSarsaVacuumCleaner(int x, int y) {
		super(x, y);
		this.policy = new StateActionPolicy();
		this.rnd = new Random();
	}
	
	@Override
	public void run() {
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
		
		// Update policy
		double value0 = policy.getValue(state0, action0);
		double value1 = policy.getValue(state1, action1);
		double valueDelta = LEARNING_RATE * (reward0 + (FUTURE_REWARD_DISCOUNT_RATE * value1) - value0);
		double newValue0 = value0 + valueDelta;
		policy.setValue(state0, action0, newValue0);
		
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
	
	private State getCurrentState() {
		//return getLookAroundState();
		return getLookAheadState();
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
