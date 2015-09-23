package languagelearning;

public enum Direction {
	NORTH, EAST, SOUTH, WEST;
	
	public Direction nextClockWise() {
		switch (this) {
		case NORTH: return EAST;
		case EAST: return SOUTH;
		case SOUTH: return WEST;
		case WEST: return NORTH;
		default: return null;
		}
	}
	
	public Direction nextCounterClockWise() {
		switch (this) {
		case NORTH: return WEST;
		case EAST: return NORTH;
		case SOUTH: return EAST;
		case WEST: return SOUTH;
		default: return null;
		}
	}

/*	public static final int DIRECTION_NORTH = 0;
	public static final int DIRECTION_EAST = 1;
	public static final int DIRECTION_SOUTH = 2;
	public static final int DIRECTION_WEST = 3;*/

}
