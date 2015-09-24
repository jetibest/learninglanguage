package languagelearning.env;

import java.util.ArrayList;
import java.util.List;

import languagelearning.LearningLanguage;
import languagelearning.agents.RL1VacuumCleaner;

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
	
	@Override
	public void initAgents() {
		for (int i = 0; i < AGENTS_INIT_COUNT; i++) {
			int initX = (int) (Math.random() * getGridWidth());
			int initY = (int) (Math.random() * getGridHeight());
			// objects.add(new VacuumCleaner(initX, initY));
			getObjects().add(new RL1VacuumCleaner(initX, initY));
		}
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
		for (int i = 0; i < getGridHeight(); i++) {
			int[] row = getDustGrid()[i];
			for (int j = 0; j < getGridWidth(); j++) {
				int val = Math.min(row[j] + DUST_INCREMENT_VALUE, DUST_MAX);
				row[j] = val;
			}
		}
	}

	@Override
	public void updateObjects() {
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
	}
	
	@Override
	public void updateStatus() {
		int gridCellsCount = getGridHeight()
				* getGridWidth();
		long totalDust = getTotalDust();
		getStatusUpdater().updateTotalDustPercentage(100.0D
				* (totalDust - DUST_MIN * gridCellsCount)
				/ ((DUST_MAX - DUST_MIN) * gridCellsCount));
		getStatusUpdater().updateTime(getTicks());
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
