package languagelearning;

import languagelearning.agents.AgentsConfig;
import languagelearning.agents.DefaultAgentsConfig;
import languagelearning.env.DustMultiplierConfig;
import languagelearning.env.EnvironmentConfig;
import languagelearning.env.RunnableEnvironment;
import languagelearning.gui.LLWindow;

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
            
            // Conclusions:
            // Local placement of symbols (pheromones) can be better than basic symbol signalling through sound
            // 
            
            
            EnvironmentConfig environmentConfig = new EnvironmentConfig();
            environmentConfig.setGridWidth(32);
            environmentConfig.setGridHeight(20);
            environmentConfig.setDustMin(0);
            environmentConfig.setDustMax(10000);
            environmentConfig.setDustIncrement(10);
            environmentConfig.setDustStartPercentage(0.6);
            environmentConfig.setDustVariancePercentage(0.1);
            environmentConfig.setBounded(true);
            environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(0, 0, 32, 20, 0));
            environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(8, 4, 16, 10, 1));
            //environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(14, 6, 4, 4, 50));
            //environmentConfig.getDustMultipliers().add(new DustMultiplierConfig(0, 10, 32, 1, 50));


            AgentsConfig agentsConfig = new DefaultAgentsConfig();
            // Default: Only has bumper as sensor, and default actions
            // Normal: Can sense dust below, bumper as sensor, and has default actions
            

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
