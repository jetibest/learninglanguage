package languagelearning;

import java.text.DecimalFormat;

import languagelearning.actions.Action;
import languagelearning.agents.AgentType;
import languagelearning.agents.AgentsConfig;
import languagelearning.agents.GridObject;
import languagelearning.agents.TDVacuumCleaner;
import languagelearning.env.Environment;
import languagelearning.env.EnvironmentConfig;
import languagelearning.env.SimulationEnvironment;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.StateVariable;
import languagelearning.util.BooleanMatrix;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class LearningLanguageStats {
	private final static DecimalFormat df = new DecimalFormat("#0.00");

	public static void main(String[] args) {
		SimulationConfig simulationConfig = new SimulationConfig();
		simulationConfig.setRuns(30);
		simulationConfig.setTrainingTicks(200000);
		simulationConfig.setTestTicks(1000);
		
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setGridWidth(32);
		environmentConfig.setGridHeight(20);
		environmentConfig.setDustMin(0);
		environmentConfig.setDustMax(10000);
		environmentConfig.setDustIncrement(10);
		environmentConfig.setDustStartPercentage(0.6);
		environmentConfig.setDustVariancePercentage(0.1);
		environmentConfig.setBounded(true);
		
		AgentsConfig agentsConfig = new AgentsConfig();
		agentsConfig.setAgentType(AgentType.QLEARNING);
		agentsConfig.setAgentInitCount(10);
		agentsConfig.setExplorationRate(0.1);
		agentsConfig.setExplorationRateDecay(1.0);
		agentsConfig.setDustCleanValue(5000);
		agentsConfig.setDustPerceptionThreshold(1000);
		agentsConfig.setLearningRate(0.1);
		agentsConfig.setFutureRewardDiscountRate(0.95);
		agentsConfig.setSharedPolicy(true);
		agentsConfig.setPossibleActions(new Action[]{
        		Action.TURN_RIGHT
        		,Action.TURN_LEFT
        		,Action.MOVE_FORWARD
        		,Action.COLLECT_DUST
        		//,Action.COLLECT_DUST_AND_PRODUCE_SOUND_C
		});
		agentsConfig.setPossibleStateVariables(new StateVariable[]{
        		StateVariable.DUST_BELOW
        		,StateVariable.OBSTACLE_AHEAD
        		,StateVariable.SOUND_C_AHEAD
		});
		agentsConfig.setSoundMatrix(BooleanMatrix.SQUARE_5x5);
		agentsConfig.setDebug(false);
		
		DescriptiveStatistics collectedDustStats = new DescriptiveStatistics();
		StateActionPolicy bestPolicy = null;
		double maxMetric = Double.MIN_VALUE;
		
		for (int run = 0; run < simulationConfig.getRuns(); run++) {
			Environment environment = new SimulationEnvironment(environmentConfig);
			environment.init();
			
			agentsConfig.produceAgents(environment);
			
			// Train first
			for (int tick = 0; tick < simulationConfig.getTrainingTicks(); tick++) {
				environment.tick();
			}

			// Test
			environment.initMaxDust(); // Fill everything with dust
			environment.setDustIncrement(0); // No new dust
			environment.setLearning(false); // Switch off policy updates
			
			double metricBefore = environment.getDustinessRatio();
			for (int tick = 0; tick < simulationConfig.getTestTicks(); tick++) {
				environment.tick();
			}
			double metricAfter = environment.getDustinessRatio();
			
			double metric = metricBefore - metricAfter;
			
			collectedDustStats.addValue(metric);
			System.out.println((run+1) + ") before = " + df.format(metricBefore) + " after = " + df.format(metricAfter) + " collecting rate = " + df.format(metric));
		
			if (metric > maxMetric) {
				maxMetric = metric;
				GridObject obj = environment.getObjects().get(0);
				if (obj instanceof TDVacuumCleaner) {
					TDVacuumCleaner td = (TDVacuumCleaner)obj;
					bestPolicy = td.getPolicy();
				}
			}
		}

		if (bestPolicy != null) {
			System.out.println();
			System.out.println("Best policy with " + df.format(maxMetric*100.0) + "% dust collected:");
			System.out.println(bestPolicy.toString());
		}
		
		System.out.println();
		
		double collectedDustRatioAverage = collectedDustStats.getMean();
		double collectedDustRatioSD = collectedDustStats.getStandardDeviation();
		
		System.out.println("Dust collected average = " + df.format(collectedDustRatioAverage*100.0) + "%");
		System.out.println("Dust collected standard deviation = " + df.format(collectedDustRatioSD*100.0) + "%");
	}

}
