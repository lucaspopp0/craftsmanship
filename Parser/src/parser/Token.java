package parser;

public interface Token {

	public TerminalSymbol getType();

	public boolean matches(TerminalSymbol type);
	
	boolean isOperator();

}
