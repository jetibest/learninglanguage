package languagelearning.env;

import languagelearning.LearningLanguage;
import languagelearning.actions.Action;
import languagelearning.agents.Agent;
import languagelearning.agents.AgentFactory;
import languagelearning.agents.YetiVacuumCleaner;
import languagelearning.agents.TDQLearningVacuumCleaner;
import languagelearning.agents.TDVacuumCleaner;
import languagelearning.policies.StateActionPolicy;
import languagelearning.states.StateVariable;
import languagelearning.util.BooleanMatrix;

public class RunnableEnvironment extends Environment implements Runnable {
	public static final long SIMULATION_INTERVAL = 1000; // is affected by
															// simulationSpeed,
															// see
															// `getSimulationSpeedMultiplier()`
	public static final double DUST_START_PERCENTAGE = 0.6;
	public static final double DUST_VARIANCE_PERCENTAGE = 0.1;
	public static final int AGENTS_INIT_COUNT = 10;
	public static final int SIM_SPEED_MIN = 0;
	public static final int SIM_SPEED_MAX = 1000;
	public static final int SIM_SPEED_DEFAULT = 500;
	
	public static final int DUST_MIN = 0;
	public static final int DUST_MAX = 10000;
	public static final int DUST_INCREMENT_VALUE = 10;

	private Thread t;
	private int simulationSpeed = SIM_SPEED_DEFAULT;

	public RunnableEnvironment(int getGridHeight, int getGridWidth) {
		super(getGridHeight, getGridWidth);
		setDustMin(DUST_MIN);
		setDustMax(DUST_MAX);
	}
	
	public void init() {
		super.init();

		t = new Thread(this);
	}
	
	@Override
	public void initDust() {
		initRandomDust(DUST_START_PERCENTAGE, DUST_VARIANCE_PERCENTAGE);
	}
	
	@Override
	public void initObjects() {
                final StateActionPolicy sharedPolicy = new StateActionPolicy();
                final double explorationRate = 0.1;
                final double explorationRateDecay = 1.0;
                final double learningRate = 0.1;
                final double futureRewardDiscountRate = 0.95;
                final Action[] possibleActions = new Action[]{
                		Action.TURN_RIGHT
                		,Action.TURN_LEFT
                		,Action.MOVE_FORWARD
                		,Action.COLLECT_DUST
                		//,Action.COLLECT_DUST_AND_PRODUCE_SOUND_C
                		//,Action.PRODUCE_SOUND_C
                };
                final StateVariable[] possibleStateVariables = new StateVariable[]{
                		StateVariable.DUST_BELOW
                		//,StateVariable.DUST_AHEAD
                		,StateVariable.OBSTACLE_AHEAD
                		//,StateVariable.SOUND_C_AHEAD
                	};
                
		initRandomAgents(AGENTS_INIT_COUNT,new AgentFactory() {
			private boolean debug = true;
			@Override
			public Agent produceAgent(int x, int y) {
                            boolean tdq = true;
                            if(tdq)
                            {
                                TDQLearningVacuumCleaner agent = new TDQLearningVacuumCleaner(sharedPolicy, x, y);
                                agent.setExplorationRate(explorationRate);
                                agent.setExplorationRateDecay(explorationRateDecay);
                                agent.setLearningRate(learningRate);
                                agent.setFutureRewardDiscountRate(futureRewardDiscountRate);
                                agent.setPossibleActions(possibleActions);
                                agent.setPossibleStateVariables(possibleStateVariables);
                                agent.setSoundMatrix(BooleanMatrix.SQUARE_5x5);
				agent.setDebug(debug);
				debug = false;
				/*agent.setExplorationRate(0.1);
				agent.setLearningRate(0.1);
				agent.setFutureRewardDiscountRate(0.9);
				agent.setPossibleActions(new Action[]{Action.TURN_RIGHT,Action.MOVE_FORWARD,Action.COLLECT_DUST_AND_PRODUCE_SOUND_C});
				agent.setPossibleStateVariables(new StateVariable[]{StateVariable.DUST_BELOW,StateVariable.OBSTACLE_AHEAD,StateVariable.SOUND_C_BELOW});*/
                                return agent;
                            }
                            else
                            {
                                YetiVacuumCleaner agent = new YetiVacuumCleaner(x, y);
				agent.setSoundMatrix(BooleanMatrix.TRIANGLE_7x5);
				return agent;
                            }
			}});
	}
	
	@Override
	public void start() {
		t.start();
		
		super.start();
	}

	@Override
	public void run() {
		getLogger().log(this.getClass().getName(), "Started!");

		long start;
		while (LearningLanguage.MAIN.isRunning()) {
			start = System.currentTimeMillis();
			
			tick();

			try {
				Thread.sleep(Math.max(0,
						(long) (getSimulationSpeedMultiplier()
								* SIMULATION_INTERVAL - (System
								.currentTimeMillis() - start))));
			} catch (Exception e) {
			}
		}

		getLogger().log(this.getClass().getName(), "Stopped!");
	}
	
	@Override
	public void updateDust() {
		updateDustWithConstantIncremenent(DUST_INCREMENT_VALUE);
	}
	
	@Override
	public void updateObjects() {
		updateObjectsInRandomOrder();
	}
	
	public double getSimulationSpeedMultiplier() {
		return 1 - (simulationSpeed * 1.0D - SIM_SPEED_MIN)
				/ (SIM_SPEED_MAX - SIM_SPEED_MIN);
	}

	public void setSimulationSpeed(int speed) {
		// Beware, if speed is set too high, then multiplier is zero.
		// When Thread.sleep is 0, this means that some threads may receive an
		// unfair advantage. This cannot be guaranteed then.
		// Maybe we need to use ticks instead.
		this.simulationSpeed = speed;
	}

}
