package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class DustBelowState extends State {
	private boolean dustBelow;
	
	public DustBelowState() {
	}

	public boolean isDustBelow() {
		return dustBelow;
	}

	public void setDustBelow(boolean dustBelow) {
		this.dustBelow = dustBelow;
	}

	@Override
	public String toString() {
		return "D-B="+dustBelow;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof DustBelowState) {
			DustBelowState other = (DustBelowState)o;
			return toString().equals(other.toString());
		}
		return false;
	}
}
