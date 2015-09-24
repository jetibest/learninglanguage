package languagelearning;

import languagelearning.env.Environment;
import languagelearning.env.RunnableEnvironment;
import languagelearning.gui.LLWindow;

public class LearningLanguage {
	/*
	 * TODO: Make use of synchronized keyword TODO: Make GRID_SIZE dependent on
	 * the JFrame dimensions when resized (start out with default value) And
	 * split up in GRIDCELL_WIDTH, GRIDCELL_HEIGHT instead of a squared size.
	 */

	public static final LearningLanguage MAIN = new LearningLanguage();
	private static final int GRID_WIDTH = 32;
	private static final int GRID_HEIGHT = 20;
	public static final int GRID_SIZE = 20;

	private boolean isRunning;
	private LLWindow win;
	private RunnableEnvironment env;

	public LearningLanguage() {
	}

	public void init(String[] args) {
		env = new RunnableEnvironment(GRID_HEIGHT, GRID_WIDTH);
		win = new LLWindow();
		env.init();
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

	public void log(String key, String msg) {
		System.out.println(key + ": " + msg);
	}

	public static void main(String[] args) {
		MAIN.init(args);
		MAIN.start();

	}
}
