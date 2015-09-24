package languagelearning.states;

/*
 * Basic environment/perception state - can see ahead and detect dust or obstacle
 */
public class BasicEnvironmentState extends EnvironmentState {
	private boolean obstacleAhead;
	private boolean dustAhead;
	
	public BasicEnvironmentState(boolean obstacleAhead,boolean dustAhead) {
	}

	public boolean isObstacleAhead() {
		return obstacleAhead;
	}

	public boolean isDustAhead() {
		return dustAhead;
	}
}
