package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class LookAheadState extends State {
	private boolean obstacleAhead;
	private boolean dustAhead;
	private boolean dustBelow;
	
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
	
	

	public boolean isDustBelow() {
		return dustBelow;
	}

	public void setDustBelow(boolean dustBelow) {
		this.dustBelow = dustBelow;
	}

	@Override
	public String toString() {
		return "obstacleAhead="+obstacleAhead + " dustAhead="+dustAhead  + " dustBelow="+dustBelow;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LookAheadState) {
			LookAheadState other = (LookAheadState)o;
			return other.isDustAhead() == dustAhead && other.isObstacleAhead() == obstacleAhead && other.isDustBelow() == dustBelow;
		}
		return false;
	}
}
