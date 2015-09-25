package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class LookTwoAheadState extends State {
	private boolean obstacleAhead;
	private boolean dustOneAhead;
	private boolean dustTwoAhead;
	private boolean dustBelow;
	
	public LookTwoAheadState() {
	}

	public boolean isObstacleAhead() {
		return obstacleAhead;
	}

	public void setObstacleAhead(boolean obstacleAhead) {
		this.obstacleAhead = obstacleAhead;
	}

	public boolean isDustBelow() {
		return dustBelow;
	}

	public void setDustBelow(boolean dustBelow) {
		this.dustBelow = dustBelow;
	}

	public boolean isDustOneAhead() {
		return dustOneAhead;
	}

	public void setDustOneAhead(boolean dustOneAhead) {
		this.dustOneAhead = dustOneAhead;
	}

	public boolean isDustTwoAhead() {
		return dustTwoAhead;
	}

	public void setDustTwoAhead(boolean dustTwoAhead) {
		this.dustTwoAhead = dustTwoAhead;
	}

	@Override
	public String toString() {
		return "O-A="+obstacleAhead + " D-1A="+dustOneAhead  + " D-2A=" + dustTwoAhead + " D-B="+dustBelow;
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
