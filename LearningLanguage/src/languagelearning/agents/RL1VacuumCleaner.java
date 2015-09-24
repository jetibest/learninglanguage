package languagelearning.agents;

import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.policies.IncrementalStateActionPolicy;
import languagelearning.states.State;

/**
 *
 * @author simon
 */
public class RL1VacuumCleaner extends VacuumCleaner {
	private IncrementalStateActionPolicy policy;
	private Random rnd;
	
	public RL1VacuumCleaner(int x, int y) {
		super(x, y);
		this.policy = new IncrementalStateActionPolicy();
		this.rnd = new Random();
	}

	@Override
	public void run() {
		System.out.println("======================================");
		System.out.println("POLICY:");
		System.out.println(policy);

		double explorationRate = 0.1;
		
		State state = getLookAheadState();
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
	}
	
	private Action[] getAvailableActions() {
		return new Action[]{Action.TURN_LEFT,Action.TURN_RIGHT,Action.MOVE_FORWARD,Action.COLLECT_DUST};
	}
}
