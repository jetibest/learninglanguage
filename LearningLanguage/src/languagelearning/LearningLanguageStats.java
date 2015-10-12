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
	private final static DecimalFormat df = new DecimalFormat("#0.0");

	public static void main(String[] args) {
		SimulationConfig simulationConfig = new SimulationConfig();
		simulationConfig.setRuns(5);
		simulationConfig.setTrainingTicks(100000);
		simulationConfig.setTestTicks(100000);
		
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setGridWidth(32);
		environmentConfig.setGridHeight(20);
		environmentConfig.setDustMin(0);
		environmentConfig.setDustMax(10000);
		environmentConfig.setDustIncrement(10);
		environmentConfig.setDustStartPercentage(0.6);
		environmentConfig.setDustVariancePercentage(0.1);
		environmentConfig.setBounded(true);
		//environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(0, 0, 16, 20, 100));
		//environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(16, 0, 16, 20, 0));
		
		AgentsConfig agentsConfig = new AgentsConfig();
		agentsConfig.setAgentType(AgentType.QLEARNING);
                // SARSA & no Sound:        17.9 at 195K ticks
                // QLEANING & no Sound:     17.8 at 195K ticks
                // QLEARNING & Sound:       18.0 at 195K ticks
                // SARSA & Sound:           18.0 at 195K ticks
                // QLEARNING & Sound production & No sound detection    18.3    at 195K
                // QLEARNING & Sound detection & No Sound production    17.9    at 195K
                // QLEARNING & Sound & No normal collect dust           17.6    at 195K
		agentsConfig.setAgentInitCount(10);
		agentsConfig.setExplorationRate(0.1);
		agentsConfig.setExplorationRateDecay(1);
		agentsConfig.setDustCleanValue(5000);
		agentsConfig.setDustPerceptionThreshold(1000);
		agentsConfig.setLearningRate(0.1);
		agentsConfig.setFutureRewardDiscountRate(0.95);
		agentsConfig.setSharedPolicy(false);
		agentsConfig.setPossibleActions(new Action[]{
        		Action.TURN_RIGHT
        		,Action.TURN_LEFT
        		,Action.MOVE_FORWARD
        		,Action.COLLECT_DUST
        		,Action.PLACE_PHEROMONE_X
        		//,Action.COLLECT_DUST_AND_PRODUCE_SOUND_C
        		//,Action.PRODUCE_SOUND_C
		});
		agentsConfig.setPossibleStateVariables(new StateVariable[]{
        		StateVariable.DUST_BELOW
        		,StateVariable.OBSTACLE_AHEAD
        		//,StateVariable.PHEROMONE_BELOW
        		,StateVariable.PHEROMONE_AHEAD
        		,StateVariable.PHEROMONE_TWO_AHEAD
        		//,StateVariable.SOUND_C_BELOW
        		//,StateVariable.SOUND_C_AHEAD
        		//,StateVariable.SOUND_C_TWO_AHEAD
		});
		agentsConfig.setSoundMatrix(BooleanMatrix.SQUARE_9x9);
		agentsConfig.setPheromoneSize(100);
		agentsConfig.setDebug(false);
		
		DescriptiveStatistics dustRatioStats = new DescriptiveStatistics();
		//DescriptiveStatistics cleaningRateStats = new DescriptiveStatistics();
		StateActionPolicy bestPolicy = null;
		double minDustRatio = Double.MAX_VALUE;
		
		for (int run = 0; run < simulationConfig.getRuns(); run++) {
			Environment environment = new SimulationEnvironment(environmentConfig);
			environment.init();
			
			agentsConfig.produceAgents(environment);
			
			// Train first
			for (int tick = 0; tick < simulationConfig.getTrainingTicks(); tick++) {
				environment.tick();
			}

			// Test
			//environment.initMaxDust(); // Fill everything with dust
			//environment.setDustIncrement(0); // No new dust
			//environment.initRandomDust();
			//environment.repositionObjectsInRandom();
			//environment.setLearning(false); // Switch off policy updates
			
			//double dustRatioStart = environment.getDustinessRatio();
			
			double dustRatioSum = 0;
			for (int tick = 0; tick < simulationConfig.getTestTicks(); tick++) {
				environment.tick();
				
				dustRatioSum = dustRatioSum + environment.getTotalDustPercentage();
			}
			
			double averageDustRatio = dustRatioSum / simulationConfig.getTestTicks();
			dustRatioStats.addValue(averageDustRatio);
			
			System.out.println((run+1) + ") average dust ratio = " + df.format(averageDustRatio) + "%");
			
			//double dustRatioEnd = environment.getDustinessRatio();
			//double dustRatioDiff = dustRatioStart - dustRatioEnd;
			//dustRatioStats.addValue(dustRatioDiff);
			//double cleaningRate = dustRatioEnd / dustRatioStart;
			//cleaningRateStats.addValue(cleaningRate);
			//System.out.println((run+1) + ") dustiness start (DS) = " + df.format(dustRatioStart) + " dustiness end (DE) = " + df.format(dustRatioEnd) + " collected dust (DS-DE) = " + df.format(dustRatioDiff) + " cleaning rate (DE/DS) = " + df.format(cleaningRate));
		
			if (averageDustRatio < minDustRatio) {
				minDustRatio = averageDustRatio;
				GridObject obj = environment.getObjects().get(0);
				if (obj instanceof TDVacuumCleaner) {
					TDVacuumCleaner td = (TDVacuumCleaner)obj;
					bestPolicy = td.getPolicy();
				}
			}
		}

		if (bestPolicy != null) {
			System.out.println();
			System.out.println("Best policy with " + df.format(minDustRatio) + "% dust ratio:");
			System.out.println(bestPolicy.toString());
		}
		
		System.out.println();
		
		System.out.println("Dust ratio average of average = " + df.format(dustRatioStats.getMean()) + "%");
		System.out.println("Dust ratio average standard deviation = " + df.format(dustRatioStats.getStandardDeviation()) + "%");
	}

}
