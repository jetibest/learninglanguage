package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class LookAheadState extends State {
	private boolean obstacleAhead;
	private boolean dustAhead;
	
	public LookAheadState(boolean obstacleAhead,boolean dustAhead) {
	}

	public boolean isObstacleAhead() {
		return obstacleAhead;
	}

	public boolean isDustAhead() {
		return dustAhead;
	}
	
	@Override
	public String toString() {
		return "obstacleAhead="+obstacleAhead + " dustAhead="+dustAhead;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LookAheadState) {
			LookAheadState other = (LookAheadState)o;
			return other.isDustAhead() == dustAhead && other.isObstacleAhead() == obstacleAhead;
		}
		return false;
	}
}
