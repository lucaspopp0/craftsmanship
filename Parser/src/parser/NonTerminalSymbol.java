package parser;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public enum NonTerminalSymbol implements Symbol {
	
	EXPRESSION, EXPRESSION_TAIL, TERM, TERM_TAIL, UNARY, FACTOR;
	
	private static final EnumMap<NonTerminalSymbol, Map<TerminalSymbol, SymbolSequence>> productions = makeProductionsMap();
	
	public static final Optional<Node> parseInput(List<Token> input) throws NullPointerException {
		Objects.requireNonNull(input, "Cannot parse a null input");
		
		ParseState result = NonTerminalSymbol.EXPRESSION.parse(input);
		
		if (result.getSuccess() && result.hasNoRemainder()) {
			return Optional.of(result.getNode());
		}
		
		return Optional.empty();
	}
	
	@Override
	public ParseState parse(List<Token> input) throws NullPointerException {
		Objects.requireNonNull(input, "Cannot parse a null input");
		
		TerminalSymbol lookAhead = !input.isEmpty() ? input.get(0).getType() : null;
		SymbolSequence production = productions.get(this).get(lookAhead);
		
		if (production == null) return ParseState.FAILURE;
		
		return production.match(input);
	}
	
	private static EnumMap<NonTerminalSymbol, Map<TerminalSymbol, SymbolSequence>> makeProductionsMap() {
		EnumMap<NonTerminalSymbol, Map<TerminalSymbol, SymbolSequence>> map = new EnumMap<NonTerminalSymbol, Map<TerminalSymbol, SymbolSequence>>(NonTerminalSymbol.class);
		
		// Initialize sub-maps
		for (NonTerminalSymbol key : NonTerminalSymbol.class.getEnumConstants()) {
			map.put(key, new HashMap<TerminalSymbol, SymbolSequence>());
		}
		
		// Set productions for EXPRESSION
		mapValueToKeys(map.get(EXPRESSION), SymbolSequence.build(TERM, EXPRESSION_TAIL), TerminalSymbol.MINUS, TerminalSymbol.OPEN, TerminalSymbol.VARIABLE);
		
		// Set productions for EXPRESSION_TAIL (has epsilons)
		map.get(EXPRESSION_TAIL).put(TerminalSymbol.PLUS, SymbolSequence.build(TerminalSymbol.PLUS, TERM, EXPRESSION_TAIL));
		map.get(EXPRESSION_TAIL).put(TerminalSymbol.MINUS, SymbolSequence.build(TerminalSymbol.MINUS, TERM, EXPRESSION_TAIL));
		map.get(EXPRESSION_TAIL).put(TerminalSymbol.CLOSE, SymbolSequence.EPSILON);
		map.get(EXPRESSION_TAIL).put(null, SymbolSequence.EPSILON);
		
		// Set productions for TERM
		mapValueToKeys(map.get(TERM), SymbolSequence.build(UNARY, TERM_TAIL), TerminalSymbol.MINUS, TerminalSymbol.OPEN, TerminalSymbol.VARIABLE);
		
		// Set productions for TERM_TAIL (has epsilons)
		map.get(TERM_TAIL).put(TerminalSymbol.TIMES, SymbolSequence.build(TerminalSymbol.TIMES, UNARY, TERM_TAIL));
		map.get(TERM_TAIL).put(TerminalSymbol.DIVIDE, SymbolSequence.build(TerminalSymbol.DIVIDE, UNARY, TERM_TAIL));
		mapValueToKeys(map.get(TERM_TAIL), SymbolSequence.EPSILON, TerminalSymbol.CLOSE, TerminalSymbol.PLUS, TerminalSymbol.MINUS);
		map.get(TERM_TAIL).put(null, SymbolSequence.EPSILON);
		
		// Set productions for UNARY
		map.get(UNARY).put(TerminalSymbol.MINUS, SymbolSequence.build(TerminalSymbol.MINUS, FACTOR));
		mapValueToKeys(map.get(UNARY), SymbolSequence.build(FACTOR), TerminalSymbol.OPEN, TerminalSymbol.VARIABLE);
		
		// Set productions for FACTOR
		map.get(FACTOR).put(TerminalSymbol.OPEN, SymbolSequence.build(TerminalSymbol.OPEN, EXPRESSION, TerminalSymbol.CLOSE));
		map.get(FACTOR).put(TerminalSymbol.VARIABLE, SymbolSequence.build(TerminalSymbol.VARIABLE));
		
		return map;
	}
	
	// Assigns multiple keys to a single value
	private static void mapValueToKeys(Map<TerminalSymbol, SymbolSequence> map, SymbolSequence value, TerminalSymbol... keys) {
		for (TerminalSymbol key : keys) {
			map.put(key, value);
		}
	}
	
}
