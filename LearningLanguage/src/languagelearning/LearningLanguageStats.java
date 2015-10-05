package languagelearning;

import java.text.DecimalFormat;

import languagelearning.actions.Action;
import languagelearning.agents.Agent;
import languagelearning.agents.AgentFactory;
import languagelearning.agents.GridObject;
import languagelearning.agents.TDQLearningVacuumCleaner;
import languagelearning.agents.TDVacuumCleaner;
import languagelearning.env.Environment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.StateVariable;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;


public class LearningLanguageStats {
	private final static DecimalFormat df = new DecimalFormat("#0.0");

	public static void main(String[] args) {
		int runs = 20;
		int ticks = 5000;
		
		int gridHeight = LearningLanguage.GRID_HEIGHT;
		int gridWidth = LearningLanguage.GRID_WIDTH;
		
		final int dustMin = 0;
		final int dustMax = 10000;
		final int dustIncrement = 10;
		
		final double dustStartPercentage = 0.6;
		final double dustVariancePercentage = 0.1;
		
		final int agentInitCount = 10;
		
		final double explorationRate = 0.1;
		final double learningRate = 0.1;
		final double futureRewardDiscountRate = 0.99;
		
		final Action[] possibleActions = new Action[]{Action.TURN_RIGHT,Action.MOVE_FORWARD,Action.COLLECT_DUST,Action.PRODUCE_SOUND_C};
		final StateVariable[] possibleStateVariables = new StateVariable[]{StateVariable.DUST_BELOW,StateVariable.DUST_AHEAD,StateVariable.OBSTACLE_AHEAD,StateVariable.SOUND_C_BELOW};

		DescriptiveStatistics collectedDustStats = new DescriptiveStatistics();
		StateActionPolicy bestPolicy = null;
		double maxCollectedDustRatio = 0;
		
		for (int run = 0; run < runs; run++) {
			final StateActionPolicy sharedPolicy = new StateActionPolicy();
			
			final AgentFactory agentFactory = new AgentFactory() {
				@Override
				public Agent produceAgent(int x, int y) {
					TDVacuumCleaner agent = new TDQLearningVacuumCleaner(sharedPolicy,x,y);
					agent.setExplorationRate(explorationRate);
					agent.setLearningRate(learningRate);
					agent.setFutureRewardDiscountRate(futureRewardDiscountRate);
					agent.setPossibleActions(possibleActions);
					agent.setPossibleStateVariables(possibleStateVariables);
					//agent.setSoundSymbolOnEveryDustCollected(3);
					return agent;
				}
			};
			
			final Environment environment = new Environment(gridHeight,gridWidth) {

				@Override
				public void initDust() {
					initRandomDust(dustStartPercentage, dustVariancePercentage);
				}

				@Override
				public void initObjects() {
					initRandomAgents(agentInitCount, agentFactory);
				}

				@Override
				public void updateDust() {
					updateDustWithConstantIncremenent(dustIncrement);
				}

				@Override
				public void updateObjects() {
					updateObjectsInRandomOrder();
				}
			};
			environment.setDustMin(dustMin);
			environment.setDustMax(dustMax);
			environment.init();
			
			long totalDustBefore = environment.getTotalDust();
			environment.tick(ticks);
			long totalDustAfter = environment.getTotalDust();
			long totalDustCollected = totalDustBefore - totalDustAfter;
			double collectedDustRatio = (double)totalDustCollected / (double)totalDustBefore;
			collectedDustStats.addValue(collectedDustRatio);
			System.out.println("before = " + totalDustBefore + " after = " + totalDustAfter + " collected = " + totalDustCollected + " " + df.format(collectedDustRatio*100.0) + "%");
		
			if (collectedDustRatio > maxCollectedDustRatio) {
				maxCollectedDustRatio = collectedDustRatio;
				GridObject obj = environment.getObjects().get(0);
				if (obj instanceof TDVacuumCleaner) {
					TDVacuumCleaner td = (TDVacuumCleaner)obj;
					bestPolicy = td.getPolicy();
				}
			}
		}

		if (bestPolicy != null) {
			System.out.println();
			System.out.println("Best policy with " + df.format(maxCollectedDustRatio*100.0) + "% dust collected:");
			System.out.println(bestPolicy.toString());
		}
		
		System.out.println();
		
		double collectedDustRatioAverage = collectedDustStats.getMean();
		double collectedDustRatioSD = collectedDustStats.getStandardDeviation();
		
		System.out.println("Dust collected average = " + df.format(collectedDustRatioAverage*100.0) + "%");
		System.out.println("Dust collected standard deviation = " + df.format(collectedDustRatioSD*100.0) + "%");
	}

}
