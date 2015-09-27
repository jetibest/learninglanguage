package languagelearning.states;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PredicateState extends State {
	private Set<StateVariable> vars;
	
	public PredicateState() {
		vars = new HashSet<StateVariable>();
	}
	
	public void setVariable(StateVariable var) {
		if (!vars.contains(var)) {
			vars.add(var);
		}
	}
	
	public boolean hasVariable(StateVariable var) {
		return vars.contains(var);
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (StateVariable var: vars) {
			if (buffer.length() > 0) {
				buffer.append(",");
			}
			buffer.append(var.toString());
		}
		return buffer.toString();
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
