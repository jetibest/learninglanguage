package languagelearning;

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
