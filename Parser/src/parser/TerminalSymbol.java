package parser;

import java.util.List;
import java.util.Objects;

public enum TerminalSymbol implements Symbol {

	// Use Arik's
	VARIABLE, PLUS, MINUS, TIMES, DIVIDE, OPEN, CLOSE;

	// Use this
	@Override
	public ParseState parse(List<Token> input) throws NullPointerException {
		Objects.requireNonNull(input, "Cannot parse a null input");
		
		if (!input.isEmpty() && input.get(0).matches(this)) {
			return ParseState.build(LeafNode.build(input.get(0)), input.subList(1, input.size()));
		}
		
		return ParseState.FAILURE;
	}
	
}
