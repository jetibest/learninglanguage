package languagelearning.agents;

import java.util.ArrayList;
import java.util.List;

import languagelearning.actions.Action;
import languagelearning.env.Environment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.StateVariable;
import languagelearning.util.BooleanMatrix;
import languagelearning.util.Props;

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
	private int pheromoneSize;
	
	public AgentsConfig() {
		
	}
	
	public AgentsConfig(Props props) {
		readFromProps(props);
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
				vacuumCleaner.setPheromoneSize(pheromoneSize);
			}
			if (agent instanceof TDVacuumCleaner) {
				TDVacuumCleaner tdVacuumCleaner = (TDVacuumCleaner)agent;
				tdVacuumCleaner.setExplorationRate(explorationRate);
				tdVacuumCleaner.setLearningRate(learningRate);
				tdVacuumCleaner.setFutureRewardDiscountRate(futureRewardDiscountRate);
				tdVacuumCleaner.setPossibleActions(possibleActions);
				tdVacuumCleaner.setPossibleStateVariables(possibleStateVariables);
				tdVacuumCleaner.setSoundMatrix(soundMatrix);
				if (i == 0) {
					// Debug only for first agent
					tdVacuumCleaner.setDebug(debug);
				}
				tdVacuumCleaner.setDustCleanValue(dustCleanValue);
				tdVacuumCleaner.setDustPerceptionThreshold(dustPerceptionThreshold);
				tdVacuumCleaner.setExplorationRateDecay(explorationRateDecay);
			}
			
			env.addObject(agent);
		}
	}
	public int getPheromoneSize() {
		return pheromoneSize;
	}
	public void setPheromoneSize(int pheromoneSize) {
		this.pheromoneSize = pheromoneSize;
	}
	
	public void fillProps(Props props) {
		props.addValue("agentInitCount",agentInitCount);
		props.addValue("agentType",agentType.toString());
		props.addValue("explorationRate",explorationRate);
		props.addValue("explorationRateDecay",explorationRateDecay);
		props.addValue("learningRate",learningRate);
		props.addValue("futureRewardDiscountRate",futureRewardDiscountRate);
		props.addValue("sharedPolicy",sharedPolicy);
		for (int i = 0; i < possibleActions.length; i++) {
			props.addValue("possibleAction" + i,possibleActions[i].toString());
		}
		for (int i = 0; i < possibleStateVariables.length; i++) {
			props.addValue("possibleStateVariable" + i,possibleStateVariables[i].toString());
		}
		props.addValue("soundMatrix",soundMatrix.getName());
		props.addValue("debug",debug);
		props.addValue("dustCleanValue",dustCleanValue);
		props.addValue("dustPerceptionThreshold",dustPerceptionThreshold);
		props.addValue("pheromoneSize",pheromoneSize);
	}
	
	public void readFromProps(Props props) {
		agentInitCount = props.getIntValue("agentInitCount");
		agentType = AgentType.valueOf(props.getStringValue("agentType"));
		explorationRate = props.getDoubleValue("explorationRate");
		explorationRateDecay = props.getDoubleValue("explorationRateDecay");
		learningRate = props.getDoubleValue("learningRate");
		futureRewardDiscountRate = props.getDoubleValue("futureRewardDiscountRate");
		sharedPolicy = props.getBooleanValue("sharedPolicy");
		List<Action> actions = new ArrayList<Action>();
		for (int i = 0; i < 100; i++) {
			if (props.hasKey("possibleAction"+i)) {
				actions.add(Action.valueOf(props.getStringValue("possibleAction"+i)));
			} else {
				break;
			}
		}
		possibleActions = actions.toArray(new Action[actions.size()]);
		List<StateVariable> vars = new ArrayList<StateVariable>();
		for (int i = 0; i < 100; i++) {
			if (props.hasKey("possibleStateVariable"+i)) {
				vars.add(StateVariable.valueOf(props.getStringValue("possibleStateVariable"+i)));
			} else {
				break;
			}
		}
		possibleStateVariables = vars.toArray(new StateVariable[vars.size()]);
		soundMatrix = BooleanMatrix.fromName(props.getStringValue("soundMatrix"));
		debug = props.getBooleanValue("debug");
		dustCleanValue = props.getIntValue("dustCleanValue");
		dustPerceptionThreshold = props.getIntValue("dustPerceptionThreshold");
		pheromoneSize = props.getIntValue("pheromoneSize");
	}
}
