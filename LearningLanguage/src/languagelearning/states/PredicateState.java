package languagelearning.states;

import java.util.HashSet;
import java.util.Set;

public class PredicateState extends State {
	private Set<StateVariable> vars;
	private String stringValue;
	
	public PredicateState() {
		vars = new HashSet<StateVariable>();
	}
	
	public void setVariable(StateVariable var) {
		if (!vars.contains(var)) {
			vars.add(var);
			stringValue = null;
		}
	}
	
	public boolean hasVariable(StateVariable var) {
		return vars.contains(var);
	}
	
	public String toString() {
		if (stringValue == null) {
			StringBuffer buffer = new StringBuffer();
			
			for (StateVariable var: StateVariable.values()) {
				if (vars.contains(var)) {
					if (buffer.length() > 0) {
						buffer.append(" ");
					}
					buffer.append(var.toString());
				}
			}
			if (buffer.length() == 0) {
				buffer.append("EMPTY");
			}
			stringValue = buffer.toString();
		}
		return stringValue;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PredicateState) {
			return this.toString().equals(o.toString());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
