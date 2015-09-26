package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class DustTwoAheadBelowAndObstacleAheadState extends DustAheadBelowAndObstacleAheadState {
	private boolean dustTwoAhead;
	
	public DustTwoAheadBelowAndObstacleAheadState() {
	}

	public boolean isDustTwoAhead() {
		return dustTwoAhead;
	}

	public void setDustTwoAhead(boolean dustTwoAhead) {
		this.dustTwoAhead = dustTwoAhead;
	}

	@Override
	public String toString() {
		return "D-1A="+isDustAhead()  + " D-2A=" + dustTwoAhead + " D-B="+isDustBelow() + " O-A="+isObstacleAhead();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof DustTwoAheadBelowAndObstacleAheadState) {
			DustTwoAheadBelowAndObstacleAheadState other = (DustTwoAheadBelowAndObstacleAheadState)o;
			return toString().equals(other.toString());
		}
		return false;
	}
}
