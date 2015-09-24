package languagelearning.agents;

import languagelearning.LearningLanguage;
import languagelearning.env.Environment;

public abstract class GridObject {
	private int x;
	private int y;
	private Environment env;

	public GridObject(int x, int y) {
		this.x = x;
		this.y = y;
		env = LearningLanguage.MAIN.getEnvironment();
	}

	public void start() {
		// Maybe some pre-run initialization here
		// As opposed to the init function, here is it guaranteed that all
		// objects etc. are initialized.
		// So this function may utilize certain functions that cannot yet be
		// done in init()
	}

	public void stop() {

	}

	public void run() {
		// Do an update on the object
	}

	public void move(int x, int y) {
		if (!env.canMove(x, y)) {
			return;
		}
		this.x = x;
		this.y = y;
	}

	public void moveNorth() {
		move(getNewXInDirection(Direction.NORTH), getNewYInDirection(Direction.NORTH));
	}

	public void moveEast() {
		move(getNewXInDirection(Direction.EAST), getNewYInDirection(Direction.EAST));
	}

	public void moveSouth() {
		move(getNewXInDirection(Direction.SOUTH), getNewYInDirection(Direction.SOUTH));
	}

	public void moveWest() {
		move(getNewXInDirection(Direction.WEST), getNewYInDirection(Direction.WEST));
	}
	
	public int getNewXInDirection(Direction direction) {
		if (direction == Direction.EAST) {
			if (env.isBoundless()) {
				return (x + 1) % env.getGridWidth();
			} else {
				return x + 1;
			}
		} else if (direction == Direction.WEST) {
			if (env.isBoundless()) {
				return (x - 1 + env.getGridWidth())
						% env.getGridWidth();
			} else {
				return x - 1;
			}
		} else {
			return x;
		}
	}

	public int getNewYInDirection(Direction direction) {
		if (direction == Direction.NORTH) {
			if (env.isBoundless()) {
				return (y - 1 + env.getGridHeight())
						% env.getGridHeight();
			} else {
				return y - 1;
			}
		} else if (direction == Direction.SOUTH) {
			if (env.isBoundless()) {
				return (y + 1) % env.getGridHeight();
			} else {
				return y + 1;
			}
		} else {
			return y;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Environment getEnvironment() {
		return env;
	}
}
