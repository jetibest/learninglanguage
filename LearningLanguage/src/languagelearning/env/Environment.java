package languagelearning.env;

import java.util.ArrayList;
import java.util.List;

import languagelearning.Logger;
import languagelearning.StatusUpdater;
import languagelearning.agents.Agent;
import languagelearning.agents.GridObject;

public abstract class Environment {
	public static final int SOUND_MAX = 3;

	private List<GridObject> objects = new ArrayList<GridObject>();
	private int[][] dustgrid;
	private int[][] soundgrid;
	private long ticks = 0;
	private Logger logger;
	private StatusUpdater statusUpdater;
	private EnvironmentConfig config;

	public Environment(EnvironmentConfig environmentConfig) {
		this.config = environmentConfig;

		this.dustgrid = new int[this.config.getGridHeight()][this.config.getGridWidth()];
		this.soundgrid = new int[this.config.getGridHeight()][this.config.getGridWidth()];
	}
	
	public EnvironmentConfig getConfig() {
		return config;
	}
	
	public void init() {
		initRandomDust();
	}

	public void initRandomDust() {
		for (int i = 0; i < config.getGridHeight(); i++) {
			int[] row = new int[config.getGridWidth()];
			for (int j = 0; j < config.getGridWidth(); j++) {
				row[j] = (int) (this.config.getDustMax() * config.getDustStartPercentage() + (Math.random() * 2 - 1)
						* this.config.getDustMax() * config.getDustVariancePercentage());
			}
			dustgrid[i] = row;
		}
	}

//	public abstract void initObjects();

/*	public void initRandomAgents(int agentInitCount, AgentFactory agentFactory) {
		for (int i = 0; i < agentInitCount; i++) {
			int initX = (int) (Math.random() * this.config.getGridWidth());
			int initY = (int) (Math.random() * this.config.getGridHeight());
			Agent agent = agentFactory.produceAgent(initX, initY);
			addObject(agent);
		}
	}*/

	public void addObject(GridObject gridObject) {
		gridObject.setEnvironment(this);
		objects.add(gridObject);
	}

	public void start() {
		for (int i = 0; i < objects.size(); i++) {
			GridObject go = objects.get(i);

			// Check if we need to start gridobject
			if (go instanceof Agent) {
				((Agent) go).start();
			}
		}
	}

	public void stop() {
		for (int i = 0; i < objects.size(); i++) {
			GridObject go = objects.get(i);

			// Check if we need to start gridobject
			if (go instanceof Agent) {
				((Agent) go).stop();
			}
		}

		// No interrupt required here
	}

	private void resetSound() {
		for (int i = 0; i < this.config.getGridHeight(); i++) {
			int[] soundrow = soundgrid[i];
			for (int j = 0; j < this.config.getGridWidth(); j++) {
				soundrow[j] = 0;
			}
		}
	}

	public void tick() {

		resetSound();

		updateDust();

		updateObjects();

		updateStatus();

		ticks++;
	}

	public void tick(int numberOfTicks) {
		for (int t = 0; t < numberOfTicks; t++) {
			tick();
		}
	}

	public void updateDust() {
		for (int i = 0; i < config.getGridHeight(); i++) {
			int[] row = dustgrid[i];
			for (int j = 0; j < config.getGridWidth(); j++) {
				int val = Math.min(row[j] + config.getDustIncrement(), config.getDustMax());
				row[j] = val;
			}
		}
	}

	public void updateObjects() {
		// Call `run` for all objects in a random order
		{
			int n = objects.size();
			List<Integer> visited = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				visited.add(i);
			}
			int i = 0;
			while (visited.size() > 0) {
				i = (i + (int) (n * Math.random())) % n;
				objects.get(visited.remove(i)).run();
				n--;
			}
		}
	}

	public void updateStatus() {
		if (statusUpdater != null) {
			int gridCellsCount = config.getGridHeight() * config.getGridWidth();
			long totalDust = getTotalDust();
			getStatusUpdater().updateTotalDustPercentage(
					100.0D * (totalDust - config.getDustMin() * gridCellsCount)
							/ ((config.getDustMax() - config.getDustMin()) * gridCellsCount));
			getStatusUpdater().updateTime(getTicks());
		}
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public void setStatusUpdater(StatusUpdater statusUpdater) {
		this.statusUpdater = statusUpdater;
	}

	public StatusUpdater getStatusUpdater() {
		return this.statusUpdater;
	}

	public boolean canMove(int x, int y) {
		// Check boundaries of environment
		if (x < 0 || x >= config.getGridWidth() || y < 0 || y >= config.getGridHeight()) {
			return false;
		}

		// Physical properties of moving (thus not for sensing purposes!)
		for (int i = 0; i < objects.size(); i++) {
			GridObject go = objects.get(i);
			if (go.getX() == x && go.getY() == y) {
				return false;
			}
		}
		return true;
	}

	public void setSoundValue(int x, int y, int value) {
		if (x >= 0 && y >= 0 && x < config.getGridWidth() && y < config.getGridHeight()) {
			soundgrid[y][x] = value;
		}
	}

	public int getSoundValue(int x, int y) {
		if (x >= 0 && x < config.getGridWidth() && y >= 0 && y < config.getGridHeight()) {
			return soundgrid[y][x];
		}
		return 0;
	}

	public void setDustValue(int x, int y, int value) {
		dustgrid[y][x] = value;
	}

	public int getDustValue(int x, int y) {
		if (x >= 0 && x < config.getGridWidth() && y >= 0 && y < config.getGridHeight()) {
			return dustgrid[y][x];
		}
		return 0;
	}

	public List<GridObject> getGridObjects() {
		return objects;
	}

	public int[][] getSoundGrid() {
		return soundgrid;
	}

	public int[][] getDustGrid() {
		return dustgrid;
	}

	public List<GridObject> getObjects() {
		return this.objects;
	}

	public long getTotalDust() {
		long totalDust = 0;
		for (int i = 0; i < config.getGridHeight(); i++) {
			for (int j = 0; j < config.getGridWidth(); j++) {
				totalDust += dustgrid[i][j];
			}
		}
		return totalDust;
	}

	public long getTicks() {
		return ticks;
	}
}
