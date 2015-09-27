package languagelearning.agents;

import languagelearning.env.Environment;

public abstract class GridObject {
	private int x;
	private int y;
	private Environment env;

	public GridObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setEnvironment(Environment env) {
		this.env = env;
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

	public int moveNorth() {
		move(getNewXInDirection(Direction.NORTH), getNewYInDirection(Direction.NORTH));
		return 0;
	}

	public int moveEast() {
		move(getNewXInDirection(Direction.EAST), getNewYInDirection(Direction.EAST));
		return 0;
	}

	public int moveSouth() {
		move(getNewXInDirection(Direction.SOUTH), getNewYInDirection(Direction.SOUTH));
		return 0;
	}

	public int moveWest() {
		move(getNewXInDirection(Direction.WEST), getNewYInDirection(Direction.WEST));
		return 0;
	}

	public int getNewXInDirection(Direction direction) {
		return getNewXInDirection(direction,1);
	}
	
	public int getNewXInDirection(Direction direction,int step) {
		if (direction == Direction.EAST) {
			if (env.isBoundless()) {
				return (x + step) % env.getGridWidth();
			} else {
				return x + step;
			}
		} else if (direction == Direction.WEST) {
			if (env.isBoundless()) {
				return (x - step + env.getGridWidth())
						% env.getGridWidth();
			} else {
				return x - step;
			}
		} else {
			return x;
		}
	}

	public int getNewYInDirection(Direction direction) {
		return getNewYInDirection(direction,1);
	}
	
	public int getNewYInDirection(Direction direction,int step) {
		if (direction == Direction.NORTH) {
			if (env.isBoundless()) {
				return (y - step + env.getGridHeight())
						% env.getGridHeight();
			} else {
				return y - step;
			}
		} else if (direction == Direction.SOUTH) {
			if (env.isBoundless()) {
				return (y + step) % env.getGridHeight();
			} else {
				return y + step;
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
