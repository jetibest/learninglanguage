package languagelearning.actions;

public class AgentAction extends Action {
	public static AgentAction TURN_LEFT = new AgentAction("TURN_LEFT");
	public static AgentAction TURN_RIGHT = new AgentAction("TURN_RIGHT");
	public static AgentAction MOVE_FORWARD = new AgentAction("MOVE_FORWARD");
	
	protected AgentAction(String action) {
		super(action);
	}
}
