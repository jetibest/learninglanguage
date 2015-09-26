package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class DustBelowAndObstacleAheadState extends DustBelowState {
	private boolean obstacleAhead;
	
	public DustBelowAndObstacleAheadState() {
	}

	public boolean isObstacleAhead() {
		return obstacleAhead;
	}

	public void setObstacleAhead(boolean obstacleAhead) {
		this.obstacleAhead = obstacleAhead;
	}

	@Override
	public String toString() {
		return "D-B="+isDustBelow() + " O-A="+obstacleAhead;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof DustBelowAndObstacleAheadState) {
			DustBelowAndObstacleAheadState other = (DustBelowAndObstacleAheadState)o;
			return toString().equals(other.toString());
		}
		return false;
	}
}
