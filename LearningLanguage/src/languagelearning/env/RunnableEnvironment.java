package languagelearning.env;

import languagelearning.LearningLanguage;
import languagelearning.actions.Action;
import languagelearning.agents.Agent;
import languagelearning.agents.AgentFactory;
import languagelearning.agents.TDQLearningVacuumCleaner;
import languagelearning.states.StateVariable;

public class RunnableEnvironment extends Environment implements Runnable {
	public static final long SIMULATION_INTERVAL = 1000; // is affected by
															// simulationSpeed,
															// see
															// `getSimulationSpeedMultiplier()`
	public static final double DUST_START_PERCENTAGE = 0.6;
	public static final double DUST_VARIANCE_PERCENTAGE = 0.1;
	public static final int AGENTS_INIT_COUNT = 1;
	public static final int SIM_SPEED_MIN = 0;
	public static final int SIM_SPEED_MAX = 1000;
	public static final int SIM_SPEED_DEFAULT = 500;

	private Thread t;
	private int simulationSpeed = SIM_SPEED_DEFAULT;

	public RunnableEnvironment(int getGridHeight, int getGridWidth) {
		super(getGridHeight, getGridWidth);
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
		initRandomAgents(AGENTS_INIT_COUNT,new AgentFactory() {

			@Override
			public Agent produceAgent(int x, int y) {
				TDQLearningVacuumCleaner agent = new TDQLearningVacuumCleaner(x, y);
				agent.setDebug(true);
				
				agent.setPossibleActions(new Action[]{Action.TURN_RIGHT,Action.MOVE_FORWARD,Action.COLLECT_DUST});
				agent.setPossibleStateVariables(new StateVariable[]{StateVariable.DUST_AHEAD,StateVariable.DUST_BELOW,StateVariable.OBSTACLE_AHEAD});
				//agent.setPossibleStateVariables(StateVariable.values());
				
				return agent;
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
