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
		double explorationRate = 0.1;
		
		State state = getLookAheadState();
		Action[] possibleActions = getAvailableActions();

		Action action = null;
		if (rnd.nextDouble() <= explorationRate) {
			// Explore	
			action = possibleActions[rnd.nextInt(possibleActions.length)];
		} else {
			// Exploit
			double maxValue = Double.MIN_VALUE;
			for (Action possibleAction: possibleActions) {
				double value = policy.getValue(state, possibleAction);
				if (value > maxValue) {
					maxValue = value;
					action = possibleAction;
				}
			}
		}
		
		int reward = doAction(action);
		policy.addReward(state, action, reward);
	}
	
	private Action[] getAvailableActions() {
		return new Action[]{Action.TURN_LEFT,Action.TURN_RIGHT,Action.MOVE_FORWARD,Action.COLLECT_DUST};
	}
}
