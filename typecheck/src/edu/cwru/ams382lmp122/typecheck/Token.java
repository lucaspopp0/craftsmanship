package edu.cwru.ams382lmp122.typecheck;

public interface Token {

	TerminalSymbol getType();

	boolean matches(TerminalSymbol type);
	
	boolean isOperator();

}
