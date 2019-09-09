package parser;

import java.util.EnumSet;

// Use this one
public abstract class AbstractToken implements Token {
	
	private static final EnumSet<TerminalSymbol> validOperators = EnumSet.of(TerminalSymbol.PLUS, TerminalSymbol.MINUS, TerminalSymbol.TIMES, TerminalSymbol.DIVIDE);
	
	public final boolean matches(TerminalSymbol type) {
		return this.getType().equals(type);
	}
	
	public boolean isOperator() {
		return AbstractToken.validOperators.contains(this.getType());
	}
	
}
