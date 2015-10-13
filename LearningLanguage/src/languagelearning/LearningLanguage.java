package languagelearning;

import java.io.File;
import java.io.IOException;

import languagelearning.agents.AgentsConfig;
import languagelearning.env.EnvironmentConfig;
import languagelearning.env.RunnableEnvironment;
import languagelearning.gui.LLWindow;
import languagelearning.util.Props;
import languagelearning.util.StatWriter;

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

	public void init(String[] args) throws IOException {
            
            // Conclusions:
            // Local placement of symbols (pheromones) can be better than basic symbol signalling through sound
            // 
			String configFilePath = "Visual.txt";
			if (args.length > 0) {
				configFilePath = args[0];
				if (!configFilePath.endsWith(".txt")) {
					configFilePath = configFilePath + ".txt";
				}
			}
		
			Props props = new Props(new File(configFilePath));
			SimulationConfig simulationConfig = new SimulationConfig(props);
			EnvironmentConfig environmentConfig = new EnvironmentConfig(props);
			AgentsConfig agentsConfig = new AgentsConfig(props);


            env = new RunnableEnvironment(environmentConfig);

            if (simulationConfig.isWriteStats()) {
    			File statDirPath = new File(configFilePath.replace(".txt", ".output"));
    			StatWriter statWriter = new StatWriter(statDirPath);
    			statWriter.setActions(agentsConfig.getPossibleActions());
                env.setStatWriter(statWriter);
            }
            
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

	public static void main(String[] args) throws IOException {
		MAIN.init(args);
		MAIN.start();

	}
}
