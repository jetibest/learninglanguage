package languagelearning.env;

import java.util.ArrayList;
import java.util.List;

import languagelearning.LearningLanguage;
import languagelearning.Logger;
import languagelearning.agents.Agent;
import languagelearning.agents.GridObject;
import languagelearning.agents.SmartVacuumCleaner;
import languagelearning.gui.LLControlPanel;

public class RunnableEnvironment extends Environment implements Runnable {
	public static final long SIMULATION_INTERVAL = 1000; // is affected by
															// simulationSpeed,
															// see
															// `getSimulationSpeedMultiplier()`
	public static final double DUST_START_PERCENTAGE = 0.6;
	public static final double DUST_VARIANCE_PERCENTAGE = 0.1;
	public static final int AGENTS_INIT_COUNT = 7;
	public static final int SIM_SPEED_MIN = 0;
	public static final int SIM_SPEED_MAX = 1000;
	public static final int SIM_SPEED_DEFAULT = 500;

	private Thread t;
	private int simulationSpeed = SIM_SPEED_DEFAULT;
	private long ticks = 0;
	private Logger logger;

	public RunnableEnvironment(int getGridHeight, int getGridWidth) {
		super(getGridHeight, getGridWidth);
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void init() {
		initDust();
		initAgents();

		t = new Thread(this);
	}
	
	public void initDust() {
		for (int i = 0; i < getGridHeight(); i++) {
			int[] row = new int[getGridWidth()];
			for (int j = 0; j < getGridWidth(); j++) {
				row[j] = (int) (DUST_MAX * DUST_START_PERCENTAGE + (Math
						.random() * 2 - 1)
						* DUST_MAX
						* DUST_VARIANCE_PERCENTAGE);
			}
			getDustGrid()[i] = row;
		}
	}
	
	public void initAgents() {
		for (int i = 0; i < AGENTS_INIT_COUNT; i++) {
			int initX = (int) (Math.random() * getGridWidth());
			int initY = (int) (Math.random() * getGridHeight());
			// objects.add(new VacuumCleaner(initX, initY));
			getObjects().add(new SmartVacuumCleaner(initX, initY));
		}
	}

	public void start() {
		t.start();

		for (int i = 0; i < getObjects().size(); i++) {
			GridObject go = getObjects().get(i);

			// Check if we need to start gridobject
			if (go instanceof Agent) {
				((Agent) go).start();
			}
		}
	}

	public void stop() {
		for (int i = 0; i < getObjects().size(); i++) {
			GridObject go = getObjects().get(i);

			// Check if we need to start gridobject
			if (go instanceof Agent) {
				((Agent) go).stop();
			}
		}

		// No interrupt required here
	}

	@Override
	public void run() {
		logger.log(this.getClass().getName(), "Started!");

		LLControlPanel llcp = LearningLanguage.MAIN.getWindow()
				.getControlPanel();
		int gridCellsCount = getGridHeight()
				* getGridWidth();
		long start;
		while (LearningLanguage.MAIN.isRunning()) {
			start = System.currentTimeMillis();

			long totalDust = 0;
			for (int i = 0; i < getGridHeight(); i++) {
				int[] row = getDustGrid()[i];
				for (int j = 0; j < getGridWidth(); j++) {
					int val = Math.min(row[j] + DUST_INCREMENT_VALUE, DUST_MAX);
					row[j] = val;
					totalDust += val;
				}
			}

			// Call `run` for all objects in a random order
			{
				int n = getObjects().size();
				List<Integer> visited = new ArrayList<Integer>();
				for (int i = 0; i < n; i++) {
					visited.add(i);
				}
				int i = 0;
				while (visited.size() > 0) {
					i = (i + (int) (n * Math.random())) % n;
					getObjects().get(visited.remove(i)).run();
					n--;
				}
			}

			llcp.updateTotalDustPercentage(100.0D
					* (totalDust - DUST_MIN * gridCellsCount)
					/ ((DUST_MAX - DUST_MIN) * gridCellsCount));
			llcp.updateTime(ticks);

			ticks++;

			try {
				Thread.sleep(Math.max(0,
						(long) (getSimulationSpeedMultiplier()
								* SIMULATION_INTERVAL - (System
								.currentTimeMillis() - start))));
			} catch (Exception e) {
			}
		}

		logger.log(this.getClass().getName(), "Stopped!");
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
