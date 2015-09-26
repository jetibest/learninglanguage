package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class DustAheadBelowAndObstacleAheadState extends DustBelowAndObstacleAheadState {
	private boolean dustAhead;
	
	public DustAheadBelowAndObstacleAheadState() {
	}

	public boolean isDustAhead() {
		return dustAhead;
	}
	
	public void setDustAhead(boolean dustAhead) {
		this.dustAhead = dustAhead;
	}
	
	@Override
	public String toString() {
		return "D-A="+dustAhead  + " D-B="+isDustBelow() + " O-A="+isObstacleAhead();
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
