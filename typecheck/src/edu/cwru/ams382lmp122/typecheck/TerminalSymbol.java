package edu.cwru.ams382lmp122.typecheck;

import java.util.List;
import java.util.Objects;

public enum TerminalSymbol implements Symbol {

	VARIABLE(null), // Letter variables
	PLUS("+"), 	  // Plus sign for addition
	MINUS("-"), 	  // Minus sign for subtraction
	TIMES("*"),    // Sign for multiplication
	DIVIDE("/"),   // Sign for division
	OPEN("("), 	  // Open parenthesis
	CLOSE(")");	  // Closed parenthesis

	/**
	 * The string representation of the Terminal Symbol
	 */
	private String representation;

	/**
	 * Constructs a TerminalSymbol with its string representation
	 * @param representation	the given string representation
	 */
	TerminalSymbol(String representation){
		this.representation = representation;
	}

	/**
	 * Gets the TerminalSymbol's string representation
	 * @return	the string representation of the TerminalSymbol
	 */
	String getRepresentation() {
		return this.representation;
	}

	// Use this
	@Override
	public ParseState parse(List<Token> input) throws NullPointerException {
		Objects.requireNonNull(input, "Cannot parse a null input");
		
		if (!input.isEmpty() && input.get(0).matches(this)) {
			return ParseState.build(ParserLeafNode.build(input.get(0)), input.subList(1, input.size()));
		}
		
		return ParseState.FAILURE;
	}

	/**
	 * Finds a TerminalSymbol that can match the given string representation
	 * @param representation	the given string representation of a TerminalSymbol
	 * @return					a TerminalSymbol that can have the given string representation
	 */
	static TerminalSymbol symbolFromParsing(String representation) {
		Objects.requireNonNull(representation, "The input representation must not be null!");
		for (TerminalSymbol symbol : TerminalSymbol.values()) {
			if (representation.equals(symbol.getRepresentation()))
				return symbol;
		}
		return TerminalSymbol.VARIABLE;
	}
	
}
