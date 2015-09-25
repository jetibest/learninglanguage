package languagelearning.agents;

import java.util.List;
import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.policies.IncrementalStateActionPolicy;
import languagelearning.states.State;

/**
 *
 * @author simon
 */
public class RL2VacuumCleaner extends VacuumCleaner {
	private IncrementalStateActionPolicy policy;
	private Random rnd;
	private State prevState;
	private Action prevAction;
	
	public RL2VacuumCleaner(int x, int y) {
		super(x, y);
		this.policy = new IncrementalStateActionPolicy();
		this.rnd = new Random();
	}
	
	@Override
	public void run() {
		System.out.println("======================================");
		System.out.println("POLICY:");
		System.out.println(policy);

		double explorationRate = 0.3;
		
		State state = getLookAroundState();
		System.out.println("CURRENT STATE = " + state);

		Action[] possibleActions = getAvailableActions();

		Action action = null;
		if (rnd.nextDouble() <= explorationRate) {
			// Explore	
			action = possibleActions[rnd.nextInt(possibleActions.length)];
		} else {
			// Exploit
			double maxValue = -999;
			for (Action possibleAction: possibleActions) {
				double value = policy.getValue(state, possibleAction);
				if (value > maxValue) {
					maxValue = value;
					action = possibleAction;
				}
			}
		}
		
		System.out.println("CHOOSEN ACTION = " + action);
		int reward = doAction(action);
		System.out.println("REWARD = " + reward);
		policy.addReward(state, action, reward);
		if (prevState != null && prevAction != null) {
			policy.addReward(prevState, prevAction, (int)(reward * 0.2));
		}
 		
		prevState = state;
		prevAction = action;
	}
	
	private Action[] getAvailableActions() {
		return new Action[]{Action.MOVE_NORTH,Action.MOVE_EAST,Action.MOVE_SOUTH,Action.MOVE_WEST,Action.COLLECT_DUST};
	}
}
