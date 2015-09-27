package languagelearning.agents;

import java.util.Random;

import languagelearning.actions.Action;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.State;
import languagelearning.states.StateVariable;

public abstract class TDVacuumCleaner extends VacuumCleaner {
	private StateActionPolicy policy;
	private Random rnd;

	private double explorationRate = 0.1;
	private double learningRate = 0.1;  // the learning rate, set between 0 and 1. Setting it to 0 means that the Q-values are never updated, hence nothing is learned. Setting a high value such as 0.9 means that learning can occur quickly.
	private double futureRewardDiscountRate = 0.9; // discount factor, also set between 0 and 1. This models the fact that future rewards are worth less than immediate rewards. Mathematically, the discount factor needs to be set less than 0 for the algorithm to converge.
	private Action[] possibleActions = new Action[]{};
	private StateVariable[] possibleStateVariables = new StateVariable[]{};
	
	public TDVacuumCleaner(int x, int y) {
		super(x, y);
		this.policy = new StateActionPolicy();
		this.rnd = new Random();
	}
	
	protected void log(String text) {
		System.out.println(text);
	}

	public double getExplorationRate() {
		return explorationRate;
	}

	public void setExplorationRate(double explorationRate) {
		this.explorationRate = explorationRate;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getFutureRewardDiscountRate() {
		return futureRewardDiscountRate;
	}

	public void setFutureRewardDiscountRate(double futureRewardDiscountRate) {
		this.futureRewardDiscountRate = futureRewardDiscountRate;
	}
	
	protected Action[] getPossibleActions() {
		return this.possibleActions;
	}

	public StateActionPolicy getPolicy() {
		return policy;
	}
	
	protected Action getEGreedyAction(State state) {
		Action[] possibleActions = getPossibleActions();

		if (rnd.nextDouble() <= getExplorationRate()) {
			// Explore	
			return possibleActions[rnd.nextInt(possibleActions.length)];
		} else {
			return policy.getActionWithMaxValue(state, possibleActions);
		}
	}

	public StateVariable[] getPossibleStateVariables() {
		return possibleStateVariables;
	}

	public void setPossibleStateVariables(StateVariable[] possibleStateVariables) {
		this.possibleStateVariables = possibleStateVariables;
	}

	public void setPossibleActions(Action[] possibleActions) {
		this.possibleActions = possibleActions;
	}
	
	public State getCurrentState() {
		return getPredicateState(possibleStateVariables);
	}
}
