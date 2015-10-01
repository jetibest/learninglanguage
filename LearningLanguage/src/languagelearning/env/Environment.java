package languagelearning.env;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import languagelearning.LearningLanguage;
import languagelearning.Logger;
import languagelearning.StatusUpdater;
import languagelearning.agents.Agent;
import languagelearning.agents.AgentFactory;
import languagelearning.agents.GridObject;
import languagelearning.agents.SmartVacuumCleaner;
import languagelearning.gui.LLControlPanel;

public abstract class Environment {
	public static final int DUST_MAX = 10000;
	public static final int DUST_MIN = 0;
	public static final int DUST_INCREMENT_VALUE = 1;

        public static final int SOUND_MAX = 3;
        
	private List<GridObject> objects = new ArrayList<GridObject>();
	private int[][] dustgrid;
        private int[][] soundgrid;
	private boolean bounded = true; // with walls
	private int gridHeight;
	private int gridWidth;
	private long ticks = 0;
	private Logger logger;
	private StatusUpdater statusUpdater;


	public Environment(int gridHeight, int gridWidth) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		
		this.dustgrid = new int[gridHeight][gridWidth];
                soundgrid = new int[gridHeight][gridWidth];
	}
	
	public void init() {
		initDust();
		initObjects();
	}

	public abstract void initDust();
	
	public void initRandomDust(double dustStartPercentage, double dustVariancePercentage) {
		for (int i = 0; i < getGridHeight(); i++) {
			int[] row = new int[getGridWidth()];
			for (int j = 0; j < getGridWidth(); j++) {
				row[j] = (int) (DUST_MAX * dustStartPercentage + (Math
						.random() * 2 - 1)
						* DUST_MAX
						* dustVariancePercentage);
			}
			dustgrid[i] = row;
		}
	}
	
	public abstract void initObjects();
	
	public void initRandomAgents(int agentInitCount,AgentFactory agentFactory) {
		for (int i = 0; i < agentInitCount; i++) {
			int initX = (int) (Math.random() * gridWidth);
			int initY = (int) (Math.random() * gridHeight);
			Agent agent = agentFactory.produceAgent(initX, initY);
			addObject(agent);
		}
	}
	
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
        
        private void resetSound()
        {
            for(int i=0;i<gridHeight;i++)
            {
                int[] soundrow = soundgrid[i];
                for(int j=0;j<gridWidth;j++)
                {
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
	
	public abstract void updateDust();
	
	public void updateDustWithConstantIncremenent(int dustIncrementValue) {
		for (int i = 0; i < gridHeight; i++) {
			int[] row = dustgrid[i];
			for (int j = 0; j < gridWidth; j++) {
				int val = Math.min(row[j] + dustIncrementValue, DUST_MAX);
				row[j] = val;
			}
		}
	}
	
	public abstract void updateObjects();
	
	public void updateObjectsInRandomOrder() {
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
			int gridCellsCount = gridHeight
					* gridWidth;
			long totalDust = getTotalDust();
			getStatusUpdater().updateTotalDustPercentage(100.0D
					* (totalDust - DUST_MIN * gridCellsCount)
					/ ((DUST_MAX - DUST_MIN) * gridCellsCount));
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
		if (x < 0 || x >= gridWidth || y < 0
				|| y >= gridHeight) {
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

        public void setSoundValue(int x, int y, int value)
        {
            if(x >= 0 && y >= 0 && x < gridWidth && y < gridHeight)
            {
                soundgrid[y][x] = value;
            }
        }
        
        public int getSoundValue(int x, int y)
        {
            if(x >= 0 && x < gridWidth && y >= 0 && y < gridHeight)
            {
                return soundgrid[y][x];
            }
            return 0;
        }
        
	public void setDustValue(int x, int y, int value) {
		dustgrid[y][x] = value;
	}

	public int getDustValue(int x, int y) {
		if (x >= 0 && x < gridWidth && y >= 0
				&& y < gridHeight) {
			return dustgrid[y][x];
		}
		return 0;
	}

	public List<GridObject> getGridObjects() {
		return objects;
	}

        public int[][] getSoundGrid()
        {
            return soundgrid;
        }
        
	public int[][] getDustGrid() {
		return dustgrid;
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

	public int getGridHeight() {
		return gridHeight;
	}

	public int getGridWidth() {
		return gridWidth;
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
	
	public long getTicks() {
		return ticks;
	}
}
