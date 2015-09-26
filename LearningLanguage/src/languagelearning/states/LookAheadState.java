package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class LookAheadState extends DustBelowState {
	private boolean obstacleAhead;
	private boolean dustAhead;
	
	public LookAheadState() {
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
		if (o instanceof LookAheadState) {
			LookAheadState other = (LookAheadState)o;
			return toString().equals(other.toString());
		}
		return false;
	}
}
