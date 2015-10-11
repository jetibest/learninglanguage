package languagelearning.agents;

import languagelearning.policies.Policy;
import languagelearning.policies.StateActionPolicy;

public enum AgentType {
	RANDOM, SMART, QLEARNING, SARSA, YETI;
	
	public Agent produceAgent(Policy policy,int x,int y) {
		if (RANDOM == this) {
			return new RandomVacuumCleaner(x,y);
		} else if (SMART == this) {
			return new SmartVacuumCleaner(x, y);
		} else if (QLEARNING == this) {
			return new TDQLearningVacuumCleaner((StateActionPolicy)policy,x, y);
		} else if (SARSA == this) {
			return new TDQLearningVacuumCleaner((StateActionPolicy)policy,x, y);
		} else if (YETI == this) {
			return new YetiVacuumCleaner(x, y);
		} else {
			return null;
		}
	}
}
