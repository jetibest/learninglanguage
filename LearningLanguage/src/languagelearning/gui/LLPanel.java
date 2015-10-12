package languagelearning.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import languagelearning.LearningLanguage;
import languagelearning.agents.Agent;
import languagelearning.agents.GridObject;
import languagelearning.env.Environment;

public class LLPanel extends JPanel implements Runnable {
	/*
	 * Paint the grid and show agents/objects on it
	 */

	private int gridWidth;
	private int gridHeight;
	private int gridSize;
	private int panelWidth;
	private int panelHeight;
	private Dimension panelSize;
	private Thread t;

	public LLPanel() {
		init();
	}

	private void init() {
		gridSize = LearningLanguage.GRID_SIZE;
		gridWidth = LearningLanguage.MAIN.getEnvironment().getGridWidth();
		gridHeight = LearningLanguage.MAIN.getEnvironment().getGridHeight();
		panelWidth = gridWidth * LearningLanguage.GRID_SIZE;
		panelHeight = gridHeight * LearningLanguage.GRID_SIZE;
		panelSize = new Dimension(panelWidth, panelHeight);
		setPreferredSize(panelSize);

		t = new Thread(this);
	}

	public void start() {
		t.start();
	}

	public void stop() {
		// No interrupt required, 100 ms loop.
	}

	@Override
	public void paintComponent(Graphics g1D) {
		Graphics2D g = (Graphics2D) g1D;

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panelWidth, panelHeight);

		if (LearningLanguage.MAIN.isRunning()) {
			
			paintDustGrid(g);
			
			paintSoundGrid(g);
			
			paintPheromoneGrid(g);
			
			paintObjects(g);
		}
	}
	
	private void paintDustGrid(Graphics2D g) {
		int[][] dustgrid = LearningLanguage.MAIN.getEnvironment()
				.getDustGrid();
		for (int i = 0; i < gridHeight; i++) {
			int[] dustrow = dustgrid[i];
			for (int j = 0; j < gridWidth; j++) {
				int value = dustrow[j];
				Color c = new Color(
						(int) (255.0D * value / LearningLanguage.MAIN
								.getEnvironment().getDustMax()), 0, 0);
				g.setColor(c);
				g.fillRect(j * gridSize, i * gridSize, gridSize, gridSize);
			}
		}
	}
	
	private void paintSoundGrid(Graphics2D g) {
		int[][] soundgridNew = LearningLanguage.MAIN.getEnvironment()
				.getNewSoundGrid();
		int[][] soundgridCurrent = LearningLanguage.MAIN.getEnvironment()
				.getCurrentSoundGrid();
		for (int i = 0; i < gridHeight; i++) {
			int[] soundrowNew = soundgridNew[i];
			int[] soundrowCurrent = soundgridCurrent[i];
			for (int j = 0; j < gridWidth; j++) {
				int valueNew = soundrowNew[j];
				if (valueNew > 0) {
					Color c = new Color(
							(int) (255.0D * valueNew / Environment.SOUND_MAX),
							(int) (255.0D * valueNew / Environment.SOUND_MAX),
							(int) (255.0D * valueNew / Environment.SOUND_MAX));
					g.setColor(c);
					g.drawOval(j * gridSize, i * gridSize, gridSize, gridSize);
				}
				
				int valueCurrent = soundrowCurrent[j];
				if (valueCurrent > 0) {
					Color c = new Color(
							(int) (255.0D * valueCurrent / Environment.SOUND_MAX),
							(int) (255.0D * valueCurrent / Environment.SOUND_MAX),
							(int) (255.0D * valueCurrent / Environment.SOUND_MAX));
					g.setColor(c);
					g.drawOval((int)((j * gridSize)+(gridSize*0.25)), (int)((i * gridSize)+(gridSize*0.25)), (int)(gridSize/2), (int)(gridSize/2));
				}
			}
		}
	}
	
	private void paintPheromoneGrid(Graphics2D g) {
		int[][] pheromoneGrid = LearningLanguage.MAIN.getEnvironment().getPheromoneGrid();
		for (int i = 0; i < gridHeight; i++) {
			int[] pheromoneRow = pheromoneGrid[i];
			for (int j = 0; j < gridWidth; j++) {
				int pheromone = pheromoneRow[j];
				for (int p = 0; p < pheromone; p++) {
					Color c = Color.WHITE;
					g.setColor(c);
					int dx = (int)(Math.random() * gridSize);
					int dy = (int)(Math.random() * gridSize);
					g.drawOval((j * gridSize)+dx, (i * gridSize)+dy, 3, 3);
					if (p > 50) {
						break;
					}
				}
			}
		}			
	}
	
	private void paintObjects(Graphics2D g) {
		List<GridObject> gridObjects = LearningLanguage.MAIN
				.getEnvironment().getGridObjects();
		for (int i = 0; i < gridObjects.size(); i++) {
			GridObject go = gridObjects.get(i);

			g.setColor(Color.GREEN);
			g.fillOval(go.getX() * gridSize, go.getY() * gridSize,
					gridSize, gridSize);

			if (go instanceof Agent) {
				Agent agent = (Agent) go;

				int halfGridSize = (int) (gridSize * 0.5);
				int centerX = go.getX() * gridSize + halfGridSize;
				int centerY = go.getY() * gridSize + halfGridSize;
				int directionX = centerX;
				int directionY = centerY;
				switch (agent.getDirection()) {
				case NORTH:
					directionY = directionY - halfGridSize;
					break;
				case EAST:
					directionX = directionX + halfGridSize;
					break;
				case SOUTH:
					directionY = directionY + halfGridSize;
					break;
				case WEST:
					directionX = directionX - halfGridSize;
					break;
				}

				g.setColor(Color.BLUE);
				g.drawLine(centerX, centerY, directionX, directionY);
			}
		}
	}

	@Override
	public void run() {
		LearningLanguage.MAIN.log(this.getClass().getName(), "Started!");

		while (LearningLanguage.MAIN.isRunning()) {
			repaint();

			try {
				Thread.sleep(50);
			} catch (Exception e) {
			}
		}

		LearningLanguage.MAIN.log(this.getClass().getName(), "Stopped!");
	}
}
