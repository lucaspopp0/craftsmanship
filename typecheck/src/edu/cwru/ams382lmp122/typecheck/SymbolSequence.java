package edu.cwru.ams382lmp122.typecheck;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class SymbolSequence {


	/*
	 * Represents a list of symbols to be produced
	 */
	private final List<Symbol> production;

	/**
	 * Represents a SymbolSequence state with an empty production
	 */
	static final SymbolSequence EPSILON = new SymbolSequence(Collections.emptyList());

	/*
	 * Constructor that initializes class and sets value for production
	 */
	private SymbolSequence(List<Symbol> production) {
		this.production = production;
	}

	/*
	 * Getter for production
	 * @return The list of Symbols stored in production
	 */
	final List<Symbol> getProduction(){
		return this.production;
	}

	/**
	 * SymbolSequence Builder that calls the constructor and returns an instance of SymbolSequence with the given production
	 * @param production given list of Symbols for new SymbolSequence
	 * @return 			 instance of SymbolSequence with production initialized as the given argument
	 */
	static SymbolSequence build(List<Symbol> production) {
		Objects.requireNonNull(production, "Production list cannot be null!");

		return new SymbolSequence(production);
	}

	static SymbolSequence build(Symbol... symbols) {
		return SymbolSequence.build(Arrays.asList(symbols));
	}

	// Use this
	ParseState match(List<Token> input) throws NullPointerException {
		Objects.requireNonNull(input, "cannot match to a null input");
		
		List<Token> remainder = input;
		ParserInternalNode.Builder builder = new ParserInternalNode.Builder();
		ParseState state = null;
		
		for (Symbol symbol : production) {
			state = symbol.parse(remainder);
			
			if (!state.getSuccess()) {
				return ParseState.FAILURE;
			} else {
				builder.addChild(state.getNode());
				remainder = state.getRemainder();
			}
		}
		
		return ParseState.build(builder.build(), remainder);
	}
	
	@Override
	public String toString() {
		return production.toString();
	}
	
}
