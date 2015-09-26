package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class LookTwoAheadState extends LookAheadState {
	private boolean dustTwoAhead;
	private boolean dustBelow;
	
	public LookTwoAheadState() {
	}

	public boolean isDustTwoAhead() {
		return dustTwoAhead;
	}

	public void setDustTwoAhead(boolean dustTwoAhead) {
		this.dustTwoAhead = dustTwoAhead;
	}

	@Override
	public String toString() {
		return "O-A="+isObstacleAhead() + " D-1A="+isDustAhead()  + " D-2A=" + dustTwoAhead + " D-B="+isDustBelow();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LookTwoAheadState) {
			LookTwoAheadState other = (LookTwoAheadState)o;
			return toString().equals(other.toString());
		}
		return false;
	}
}
