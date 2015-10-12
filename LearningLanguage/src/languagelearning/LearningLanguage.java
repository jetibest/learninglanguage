package languagelearning;

import languagelearning.actions.Action;
import languagelearning.agents.AgentType;
import languagelearning.agents.AgentsConfig;
import languagelearning.env.DustMultiplierConfig;
import languagelearning.env.EnvironmentConfig;
import languagelearning.env.RunnableEnvironment;
import languagelearning.gui.LLWindow;
import languagelearning.states.StateVariable;
import languagelearning.util.BooleanMatrix;

public class LearningLanguage implements Logger {
	/*
	 * TODO: Make use of synchronized keyword TODO: Make GRID_SIZE dependent on
	 * the JFrame dimensions when resized (start out with default value) And
	 * split up in GRIDCELL_WIDTH, GRIDCELL_HEIGHT instead of a squared size.
	 */

	public static final LearningLanguage MAIN = new LearningLanguage();
	public static final int GRID_SIZE = 20;

	private boolean isRunning;
	private LLWindow win;
	private RunnableEnvironment env;

	public LearningLanguage() {
	}

	public void init(String[] args) {
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setGridWidth(32);
		environmentConfig.setGridHeight(20);
		environmentConfig.setDustMin(0);
		environmentConfig.setDustMax(10000);
		environmentConfig.setDustIncrement(10);
		environmentConfig.setDustStartPercentage(0.6);
		environmentConfig.setDustVariancePercentage(0.1);
		environmentConfig.setBounded(true);
		environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(0, 0, 16, 20, 100));
		environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(16, 0, 16, 20, 0));
		
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
		agentsConfig.setExplorationRateDecay(1.0);
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
        		//,StateVariable.SOUND_C_BELOW
        		//,StateVariable.SOUND_C_AHEAD
        		//,StateVariable.SOUND_C_TWO_AHEAD
		});
		agentsConfig.setSoundMatrix(BooleanMatrix.SQUARE_7x7);
		agentsConfig.setPheromoneSize(100);
		agentsConfig.setDebug(true);
		
		env = new RunnableEnvironment(environmentConfig);
		win = new LLWindow();
		env.setLogger(this);
		env.setStatusUpdater(getWindow().getControlPanel());
		env.init();
		
		agentsConfig.produceAgents(env);
	}

	public LLWindow getWindow() {
		return win;
	}

	public RunnableEnvironment getEnvironment() {
		return env;
	}

	public void start() {
		log(this.getClass().getName(), "Started!");

		isRunning = true;

		env.start();
		win.start();
	}

	public void stop() {
		isRunning = false;
		win.stop();
		env.stop();

		log(this.getClass().getName(), "Stopped!");
	}

	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void log(String key, String msg) {
		System.out.println(key + ": " + msg);
	}

	public static void main(String[] args) {
		MAIN.init(args);
		MAIN.start();

	}
}
