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
}
