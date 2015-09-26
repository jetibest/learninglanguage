package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class DustAheadBelowAndObstacleAheadState extends DustBelowState {
	private boolean obstacleAhead;
	private boolean dustAhead;
	
	public DustAheadBelowAndObstacleAheadState() {
	}

	public boolean isObstacleAhead() {
		return obstacleAhead;
	}

	public boolean isDustAhead() {
		return dustAhead;
	}
	
	public void setObstacleAhead(boolean obstacleAhead) {
		this.obstacleAhead = obstacleAhead;
	}

	public void setDustAhead(boolean dustAhead) {
		this.dustAhead = dustAhead;
	}
	
	@Override
	public String toString() {
		return "O-A="+obstacleAhead + " D-A="+dustAhead  + " D-B="+isDustBelow();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof DustAheadBelowAndObstacleAheadState) {
			DustAheadBelowAndObstacleAheadState other = (DustAheadBelowAndObstacleAheadState)o;
			return toString().equals(other.toString());
		}
		return false;
	}
}
