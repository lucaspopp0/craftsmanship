package edu.cwru.ams382lmp122.typecheck;

import java.util.Objects;

public final class Variable extends AbstractToken {
	
	private final String representation;
	private static Cache<String, Variable> cache = new Cache<>();
	
	private Variable(String representation) {
		this.representation = representation;
	}
	
	public static Variable build(String representation) throws NullPointerException {
		Objects.requireNonNull(representation, "representation cannot be null");
		return cache.get(representation, Variable::new);
	}
	
	@Override
	public TerminalSymbol getType() {
		return TerminalSymbol.VARIABLE;
	}
	
	@Override
	public boolean isOperator() {
		return false;
	}
	
	public String getRepresentation() {
		return this.representation;
	}
	
	@Override
	public String toString() {
		return this.getRepresentation();
	}
	
}
