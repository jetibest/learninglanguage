package languagelearning.env;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import languagelearning.Logger;
import languagelearning.StatusUpdater;
import languagelearning.agents.Agent;
import languagelearning.agents.GridObject;
import languagelearning.agents.TDVacuumCleaner;
import languagelearning.policies.StateActionPolicy;

public abstract class Environment {
	public static final int SOUND_MAX = 3;
	
	private Random rnd = new Random();

	private List<GridObject> objects = new ArrayList<GridObject>();
	private int[][] dustgrid;
	private double[][] dustMultGrid;
	private int[][] soundgridCurrent;
	private int[][] soundgridNew;
	private int[][] pheromoneGrid;
	private long ticks = 0;
	private Logger logger;
	private StatusUpdater statusUpdater;

	private int gridWidth;
	private int gridHeight;
	private int dustMin;
	private int dustMax;
	private int dustIncrement;
	private double dustStartPercentage;
	private double dustVariancePercentage;
	private boolean bounded;
	private int ticksToReplaceAllPoliciesWithMaxRewardPolicy;

	public Environment(EnvironmentConfig config) {
		this.gridWidth = config.getGridWidth();
		this.gridHeight = config.getGridHeight();
		this.dustMin = config.getDustMin();
		this.dustMax = config.getDustMax();
		this.dustIncrement = config.getDustIncrement();
		this.dustStartPercentage = config.getDustStartPercentage();
		this.dustVariancePercentage = config.getDustVariancePercentage();
		this.bounded = config.isBounded();

		this.dustgrid = new int[gridHeight][gridWidth];
		this.dustMultGrid = new double[gridHeight][gridWidth];
		initDustMultipliers();
		this.soundgridCurrent = new int[gridHeight][gridWidth];
		this.soundgridNew = new int[gridHeight][gridWidth];
		this.pheromoneGrid = new int[gridHeight][gridWidth];
		
		for (DustMultiplierConfig dmc: config.getDustMultipliers()) {
			this.setDustMultiplier(dmc.getFromX(), dmc.getFromY(), dmc.getWidth(), dmc.getHeight(), dmc.getMultiplier());
		}
	}
	
	public void init() {
		initRandomDust();
	}

	public void initRandomDust() {
		for (int i = 0; i < gridHeight; i++) {
			int[] row = new int[gridWidth];
			for (int j = 0; j < gridWidth; j++) {
				row[j] = (int) (dustMax * dustStartPercentage + (Math.random() * 2 - 1)
						* dustMax * dustVariancePercentage);
			}
			dustgrid[i] = row;
		}
	}

	public void initDustMultipliers() {
		for (int i = 0; i < gridHeight; i++) {
			double[] row = new double[gridWidth];
			for (int j = 0; j < gridWidth; j++) {
				row[j] = 1;
			}
			dustMultGrid[i] = row;
		}
	}
	
	public void setDustMultiplier(int x,int y,double multiplier) {
		dustMultGrid[y][x] = multiplier;
	}
	
	public void setDustMultiplier(int fromX,int fromY,int width,int height,double multiplier) {
		for (int y = fromY; y < fromY+height; y++) {
			for (int x = fromX; x < fromX+width; x++) {
				dustMultGrid[y][x] = multiplier;
			}
		}
	}

	public void initMaxDust() {
		for (int i = 0; i < gridHeight; i++) {
			int[] row = new int[gridWidth];
			for (int j = 0; j < gridWidth; j++) {
				row[j] = dustMax;
			}
			dustgrid[i] = row;
		}
	}

//	public abstract void initObjects();

/*	public void initRandomAgents(int agentInitCount, AgentFactory agentFactory) {
		for (int i = 0; i < agentInitCount; i++) {
			int initX = (int) (Math.random() * gridWidth);
			int initY = (int) (Math.random() * gridHeight);
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
		// Copy new sounds to current sounds
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				soundgridCurrent[i][j] = soundgridNew[i][j];
			}
		}
		
		// Clear new sounds
		for (int i = 0; i < gridHeight; i++) {
			int[] soundrow = soundgridNew[i];
			for (int j = 0; j < gridWidth; j++) {
				soundrow[j] = 0;
			}
		}
	}

	private void resetPheromones() {
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				pheromoneGrid[i][j] = Math.max(pheromoneGrid[i][j]-1,0);
			}
		}
	}

	public void tick() {

		resetSound();
		
		resetPheromones();

		updateDust();

		updateObjects();

		updateStatus();
		
		updatePolicies();

		ticks++;
	}
	
	public void addPheromone(int x,int y,int increment) {
		pheromoneGrid[y][x] = pheromoneGrid[y][x] + increment;
	}
	
	public int getPheromone(int x,int y) {
		if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
			return pheromoneGrid[y][x];
		}
		return 0;
	}

	public void updateDust() {
		for (int i = 0; i < gridHeight; i++) {
			int[] row = dustgrid[i];
			for (int j = 0; j < gridWidth; j++) {
				int val = Math.min(row[j] + (int)(dustIncrement * dustMultGrid[i][j]), dustMax);
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
	
	public void updatePolicies() {
		if (ticksToReplaceAllPoliciesWithMaxRewardPolicy > 0) {
			if (ticks % ticksToReplaceAllPoliciesWithMaxRewardPolicy == 0) {
				replaceAllPoliciesWithMaxRewardPolicy();
			}
		}
	}

	public void updateStatus() {
		if (statusUpdater != null) {
			getStatusUpdater().updateTotalDustPercentage(getTotalDustPercentage());
			getStatusUpdater().updateTime(getTicks());
		}
	}
	
	public double getTotalDustRatio() {
		int gridCellsCount = gridHeight * gridWidth;
		long totalDust = getTotalDust();
		return (double)(totalDust - dustMin * gridCellsCount)
		/ (double)((dustMax - dustMin) * gridCellsCount);
	}

	public double getTotalDustPercentage() {
		return 100.0D * getTotalDustRatio();
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
		if (x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
			return false;
		}

		// Physical properties of moving (thus not for sensing purposes!)
		for (GridObject go: objects) {
			if (go.getX() == x && go.getY() == y) {
				return false;
			}
		}
		return true;
	}

	public void setSoundValue(int x, int y, int value) {
		if (x >= 0 && y >= 0 && x < gridWidth && y < gridHeight) {
			soundgridNew[y][x] = value;
		}
	}

	public int getCurrentSoundValue(int x, int y) {
		if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
			return soundgridCurrent[y][x];
		}
		return 0;
	}

	public void setDustValue(int x, int y, int value) {
		dustgrid[y][x] = value;
	}

	public int getDustValue(int x, int y) {
		if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
			return dustgrid[y][x];
		}
		return 0;
	}

	public List<GridObject> getGridObjects() {
		return objects;
	}

	public int[][] getCurrentSoundGrid() {
		return soundgridCurrent;
	}

	public int[][] getNewSoundGrid() {
		return soundgridNew;
	}

	public int[][] getDustGrid() {
		return dustgrid;
	}

	public List<GridObject> getObjects() {
		return this.objects;
	}

	public long getTotalDust() {
		long totalDust = 0;
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				totalDust += dustgrid[i][j];
			}
		}
		return totalDust;
	}
	
/*	public double getDustinessRatio() {
		long totalDust = getTotalDust();
		long maxDust = dustMax * gridHeight * gridWidth;
		
		return (double)totalDust / (double)maxDust;
	}*/

	public long getTicks() {
		return ticks;
	}
	
	public void setLearning(boolean learning) {
		for (GridObject go: objects) {
			if (go instanceof Agent) {
				((Agent)go).setLearning(learning);
			}
		}
	}

	public int[][] getPheromoneGrid() {
		return pheromoneGrid;
	}

	public int[][] getDustgrid() {
		return dustgrid;
	}

	public void setDustgrid(int[][] dustgrid) {
		this.dustgrid = dustgrid;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getDustMin() {
		return dustMin;
	}

	public void setDustMin(int dustMin) {
		this.dustMin = dustMin;
	}

	public int getDustMax() {
		return dustMax;
	}

	public void setDustMax(int dustMax) {
		this.dustMax = dustMax;
	}

	public int getDustIncrement() {
		return dustIncrement;
	}

	public void setDustIncrement(int dustIncrement) {
		this.dustIncrement = dustIncrement;
	}

	public double getDustStartPercentage() {
		return dustStartPercentage;
	}

	public void setDustStartPercentage(double dustStartPercentage) {
		this.dustStartPercentage = dustStartPercentage;
	}

	public double getDustVariancePercentage() {
		return dustVariancePercentage;
	}

	public void setDustVariancePercentage(double dustVariancePercentage) {
		this.dustVariancePercentage = dustVariancePercentage;
	}

	public boolean isBounded() {
		return bounded;
	}
	
	public boolean isBoundless() {
		return !bounded;
	}

	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}
	
	public Random getRandom() {
		return rnd;
	}
	
	public void repositionObjectsInRandom() {
		for (GridObject go: objects) {
			go.setXY((int) (Math.random() * gridWidth), (int) (Math.random() * gridHeight));
		}
	}
	
	public StateActionPolicy getMaxRewardPolicy() {
		double maxReward = 0;
		StateActionPolicy maxPolicy = null;
		for (GridObject go: objects) {
			if (go instanceof TDVacuumCleaner) {
				TDVacuumCleaner tdvc = (TDVacuumCleaner)go;
				if (tdvc.getPolicyReward() > maxReward) {
					maxReward = tdvc.getPolicyReward();
					maxPolicy = tdvc.getPolicy();
				}
			}
		}
		//System.out.println("Max policy with reward " + maxReward);
		//System.out.println(maxPolicy);
		return maxPolicy;
	}
	
	public void replaceAllPoliciesWithMaxRewardPolicy() {
		StateActionPolicy maxPolicy = getMaxRewardPolicy();
		if (maxPolicy != null) {
			for (GridObject go: objects) {
				if (go instanceof TDVacuumCleaner) {
					TDVacuumCleaner tdvc = (TDVacuumCleaner)go;
					tdvc.setPolicy(maxPolicy.makeCopy());
				}
			}
		}
	}

	public int getTicksToReplaceAllPoliciesWithMaxRewardPolicy() {
		return ticksToReplaceAllPoliciesWithMaxRewardPolicy;
	}

	public void setTicksToReplaceAllPoliciesWithMaxRewardPolicy(
			int ticksToReplaceAllPoliciesWithMaxRewardPolicy) {
		this.ticksToReplaceAllPoliciesWithMaxRewardPolicy = ticksToReplaceAllPoliciesWithMaxRewardPolicy;
	}
	
	
}
