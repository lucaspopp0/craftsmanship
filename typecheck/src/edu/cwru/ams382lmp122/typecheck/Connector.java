package edu.cwru.ams382lmp122.typecheck;

import java.util.EnumSet;
import java.util.Objects;

public final class Connector extends AbstractToken {

	/**
	 * The type of the connector or terminal symbol
	 */
	private TerminalSymbol type;

	/**
	 * A cache used to avoid creating duplicate variables with the same representation
	 */
	private static Cache<TerminalSymbol, Connector> cache = new Cache<>();
	private static EnumSet<TerminalSymbol> validConnectorTypes = EnumSet.of(TerminalSymbol.PLUS, TerminalSymbol.MINUS, TerminalSymbol.TIMES, TerminalSymbol.DIVIDE, TerminalSymbol.OPEN, TerminalSymbol.CLOSE);

	private Connector(TerminalSymbol type) {
		this.type = type;
	}

	// Returns a new connector, throws exception if type is null
	public static Connector build(TerminalSymbol type) throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(type, "type cannot be null");
		if (!typeIsValid(type)) throw new IllegalArgumentException("type must be one of the following: PLUS, MINUS, TIMES, DIVIDE, OPEN, CLOSE");
		
		return cache.get(type, Connector::new);
	}

	// Builds the appropriate connector from a string value, or throws an exception
	static Connector build(String stringValue) throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(stringValue, "Cannot build a connector from a null string value");
		TerminalSymbol type = null;

		for (TerminalSymbol symbol : validConnectorTypes) {
			if (stringValue.equals(symbol.getRepresentation())) {
				type = symbol;
			}
		}

		if (type == null) throw new IllegalArgumentException("'" + stringValue + "' does not correspond to a valid connector type");

		return Connector.build(type);
	}

	static boolean isValidStringRepresentation(String stringRepresentation) {
		for (TerminalSymbol symbol : validConnectorTypes) {
			if (stringRepresentation.equals(symbol.getRepresentation())) {
				return true;
			}
		}

		return false;
	}
	
	// Returns true if the type is PLUS, MINUS, TIMES, DIVIDE, OPEN, or CLOSE
	private static boolean typeIsValid(TerminalSymbol type) {
		return Connector.validConnectorTypes.contains(type);
	}
	
	@Override
	public TerminalSymbol getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return type.getRepresentation();
	}
	
}
