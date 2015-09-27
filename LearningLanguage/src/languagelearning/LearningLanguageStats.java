package languagelearning;

import java.text.DecimalFormat;

import languagelearning.actions.Action;
import languagelearning.agents.Agent;
import languagelearning.agents.AgentFactory;
import languagelearning.agents.GridObject;
import languagelearning.agents.TDQLearningVacuumCleaner;
import languagelearning.agents.TDVacuumCleaner;
import languagelearning.env.Environment;
import languagelearning.env.RunnableEnvironment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.StateVariable;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;


public class LearningLanguageStats {
	private final static DecimalFormat df = new DecimalFormat("#0.0");

	public static void main(String[] args) {
		int runs = 100;
		int ticks = 5000;
		
		int gridHeight = LearningLanguage.GRID_HEIGHT;
		int gridWidth = LearningLanguage.GRID_WIDTH;
		
		final double dustStartPercentage = RunnableEnvironment.DUST_START_PERCENTAGE;
		final double dustVariancePercentage = RunnableEnvironment.DUST_VARIANCE_PERCENTAGE;
		
		final int agentInitCount = 1;
		
		final double explorationRate = 0.1;
		final double learningRate = 0.1;
		final double futureRewardDiscountRate = 0.9;
		
		final Action[] possibleActions = new Action[]{Action.TURN_RIGHT,Action.MOVE_FORWARD,Action.COLLECT_DUST};
		//final Action[] possibleActions = new Action[]{Action.MOVE_NORTH,Action.MOVE_EAST,Action.MOVE_SOUTH,Action.MOVE_WEST,Action.TURN_RIGHT,Action.MOVE_FORWARD,Action.COLLECT_DUST};
		//final StateVariable[] possibleStateVariables = new StateVariable[]{StateVariable.DUST_BELOW,StateVariable.DUST_AHEAD,StateVariable.OBSTACLE_AHEAD};
		final StateVariable[] possibleStateVariables = new StateVariable[]{StateVariable.DUST_BELOW,StateVariable.DUST_AHEAD,StateVariable.DUST_TWO_AHEAD,StateVariable.OBSTACLE_AHEAD};
		//final StateVariable[] possibleStateVariables = new StateVariable[]{StateVariable.DUST_NORTH,StateVariable.DUST_EAST,StateVariable.DUST_SOUTH,StateVariable.DUST_WEST,StateVariable.OBSTACLE_NORTH,StateVariable.OBSTACLE_EAST,StateVariable.OBSTACLE_SOUTH,StateVariable.OBSTACLE_WEST};
		//final StateVariable[] possibleStateVariables = new StateVariable[]{StateVariable.DUST_NORTH,StateVariable.DUST_EAST,StateVariable.DUST_SOUTH,StateVariable.DUST_WEST};
		//final StateVariable[] possibleStateVariables = StateVariable.values();
		
		final AgentFactory agentFactory = new AgentFactory() {
			@Override
			public Agent produceAgent(int x, int y) {
				TDVacuumCleaner agent = new TDQLearningVacuumCleaner(x,y);
				agent.setExplorationRate(explorationRate);
				agent.setLearningRate(learningRate);
				agent.setFutureRewardDiscountRate(futureRewardDiscountRate);
				agent.setPossibleActions(possibleActions);
				agent.setPossibleStateVariables(possibleStateVariables);
				return agent;
				
				//return new SmartVacuumCleaner(x,y);
				
				//return new RandomVacuumCleaner(x,y);
			}
		};
		
		DescriptiveStatistics collectedDustStats = new DescriptiveStatistics();
		StateActionPolicy bestPolicy = null;
		double maxCollectedDustRatio = 0;
		
		for (int run = 0; run < runs; run++) {
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
					// Do not add dust
				}

				@Override
				public void updateObjects() {
					updateObjectsInRandomOrder();
				}
			};
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
		StandardDeviation sampleSD = new StandardDeviation();
		System.out.println("Dust collected standard deviation = " + df.format(collectedDustRatioSD*100.0) + "%");
	}

}
