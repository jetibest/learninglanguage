package languagelearning.agents;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.StateVariable;
import languagelearning.util.BooleanMatrix;

public class AgentsConfig {
	private int agentInitCount;
	private AgentType agentType;
	private double explorationRate;
	private double explorationRateDecay;
	private double learningRate;
	private double futureRewardDiscountRate;
	private Action[] possibleActions;
	private StateVariable[] possibleStateVariables;
	private boolean sharedPolicy;
	private BooleanMatrix soundMatrix;
	private boolean debug;
	private int dustCleanValue;
	private int dustPerceptionThreshold;
	
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
	public Action[] getPossibleActions() {
		return possibleActions;
	}
	public void setPossibleActions(Action[] possibleActions) {
		this.possibleActions = possibleActions;
	}
	public StateVariable[] getPossibleStateVariables() {
		return possibleStateVariables;
	}
	public void setPossibleStateVariables(StateVariable[] possibleStateVariables) {
		this.possibleStateVariables = possibleStateVariables;
	}
	public int getAgentInitCount() {
		return agentInitCount;
	}
	public void setAgentInitCount(int agentInitCount) {
		this.agentInitCount = agentInitCount;
	}
	public AgentType getAgentType() {
		return agentType;
	}
	public void setAgentType(AgentType agentType) {
		this.agentType = agentType;
	}
	public boolean isSharedPolicy() {
		return sharedPolicy;
	}
	public void setSharedPolicy(boolean sharedPolicy) {
		this.sharedPolicy = sharedPolicy;
	}
	public BooleanMatrix getSoundMatrix() {
		return soundMatrix;
	}
	public void setSoundMatrix(BooleanMatrix soundMatrix) {
		this.soundMatrix = soundMatrix;
	}
	
	public double getExplorationRateDecay() {
		return explorationRateDecay;
	}
	public void setExplorationRateDecay(double explorationRateDecay) {
		this.explorationRateDecay = explorationRateDecay;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public int getDustCleanValue() {
		return dustCleanValue;
	}
	public void setDustCleanValue(int dustCleanValue) {
		this.dustCleanValue = dustCleanValue;
	}
	public int getDustPerceptionThreshold() {
		return dustPerceptionThreshold;
	}
	public void setDustPerceptionThreshold(int dustPerceptionThreshold) {
		this.dustPerceptionThreshold = dustPerceptionThreshold;
	}
	public void produceAgents(Environment env) {
		StateActionPolicy policy = null;
		if (sharedPolicy) {
			policy = new StateActionPolicy();
		}
		
		for (int i = 0; i < agentInitCount; i++) {
			int initX = (int) (Math.random() * env.getGridWidth());
			int initY = (int) (Math.random() * env.getGridHeight());
			
			Agent agent = agentType.produceAgent(policy, initX, initY);

			if (agent instanceof VacuumCleaner) {
				VacuumCleaner vacuumCleaner = (VacuumCleaner)agent;
				vacuumCleaner.setDustCleanValue(dustCleanValue);
				vacuumCleaner.setDustPerceptionThreshold(dustPerceptionThreshold);
			}
			if (agent instanceof TDVacuumCleaner) {
				TDVacuumCleaner tdVacuumCleaner = (TDVacuumCleaner)agent;
				tdVacuumCleaner.setExplorationRate(explorationRate);
				tdVacuumCleaner.setLearningRate(learningRate);
				tdVacuumCleaner.setFutureRewardDiscountRate(futureRewardDiscountRate);
				tdVacuumCleaner.setPossibleActions(possibleActions);
				tdVacuumCleaner.setPossibleStateVariables(possibleStateVariables);
				tdVacuumCleaner.setSoundMatrix(soundMatrix);
				tdVacuumCleaner.setDebug(debug);
				tdVacuumCleaner.setDustCleanValue(dustCleanValue);
				tdVacuumCleaner.setDustPerceptionThreshold(dustPerceptionThreshold);
				tdVacuumCleaner.setExplorationRateDecay(explorationRateDecay);
			}
			
			env.addObject(agent);
		}
	}
	
	
}
