package languagelearning;

public class VacuumCleanerAction extends AgentAction {
	public static VacuumCleanerAction COLLECT_DUST = new VacuumCleanerAction("COLLECT_DUST");
	
	protected VacuumCleanerAction(String action) {
		super(action);
	}
}
