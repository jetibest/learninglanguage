package languagelearning.env;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import languagelearning.LearningLanguage;
import languagelearning.agents.Agent;
import languagelearning.agents.GridObject;
import languagelearning.agents.SmartVacuumCleaner;
import languagelearning.gui.LLControlPanel;

public class Environment {
	public static final int DUST_MAX = 1000;
	public static final int DUST_MIN = 0;
	public static final int DUST_INCREMENT_VALUE = 5;

	private List<GridObject> objects = new ArrayList<GridObject>();
	private int[][] dustgrid;
	private boolean bounded = true; // with walls
	private int gridHeight;
	private int gridWidth;

	public Environment(int gridHeight, int gridWidth) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		
		this.dustgrid = new int[gridHeight][gridWidth];
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

	public int[][] getDustGrid() {
		return dustgrid;
	}

	public boolean isBoundled() {
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
}
