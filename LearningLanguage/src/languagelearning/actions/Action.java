package languagelearning.actions;

/*
 * Generic action, which agents can execute
 */
public class Action {
	private String action;
	
	protected Action(String action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return action;
	}

}
