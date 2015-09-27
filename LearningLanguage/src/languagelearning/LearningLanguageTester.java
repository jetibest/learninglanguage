package languagelearning;

import java.text.DecimalFormat;

import languagelearning.agents.Agent;
import languagelearning.agents.AgentFactory;
import languagelearning.agents.VacuumCleaner;
import languagelearning.env.Environment;
import languagelearning.env.RunnableEnvironment;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;


public class LearningLanguageTester {
	private final static DecimalFormat df = new DecimalFormat("#0.0");

	public static void main(String[] args) {
		int gridHeight = LearningLanguage.GRID_HEIGHT;
		int gridWidth = LearningLanguage.GRID_WIDTH;
		
		final double dustStartPercentage = RunnableEnvironment.DUST_START_PERCENTAGE;
		final double dustVariancePercentage = RunnableEnvironment.DUST_START_PERCENTAGE;
		
		final int agentInitCount = 1;
		final AgentFactory agentFactory = new AgentFactory() {
			@Override
			public Agent produceAgent(int x, int y) {
				return new VacuumCleaner(x,y);
			}
		};
		
		int runs = 100;
		int ticks = 1000;
		
		DescriptiveStatistics collectedDustStats = new DescriptiveStatistics();
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
		}
		
		System.out.println();
		
		double collectedDustRatioAverage = collectedDustStats.getMean();
		double collectedDustRatioSD = collectedDustStats.getStandardDeviation();
		
		System.out.println("Dust collected average = " + df.format(collectedDustRatioAverage*100.0) + "%");
		StandardDeviation sampleSD = new StandardDeviation();
		System.out.println("Dust collected standard deviation = " + df.format(collectedDustRatioSD*100.0) + "%");
	}

}
