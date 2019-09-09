package parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

final class SymbolSequence {

	// Use Arik's
	public static final SymbolSequence EPSILON = SymbolSequence.build();
	
	private final List<Symbol> production;
	
	private SymbolSequence(List<Symbol> production) {
		this.production = production;
	}
	
	public static SymbolSequence build(List<Symbol> symbols) throws NullPointerException {
		Objects.requireNonNull(symbols, "symbols cannot be null");
		return new SymbolSequence(symbols);
	}

	// Use Arik's
	public static SymbolSequence build(Symbol... symbols) {
		return SymbolSequence.build(Arrays.asList(symbols));
	}

	// Use this
	public ParseState match(List<Token> input) throws NullPointerException {
		Objects.requireNonNull(input, "cannot match to a null input");
		
		List<Token> remainder = input;
		InternalNode.Builder builder = new InternalNode.Builder();
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
