package parser;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Objects;


// Use Arik's string mapping situation
public final class Connector extends AbstractToken {
	
	private static Cache<TerminalSymbol, Connector> cache = new Cache<>();
	private static EnumSet<TerminalSymbol> validConnectorTypes = EnumSet.of(TerminalSymbol.PLUS, TerminalSymbol.MINUS, TerminalSymbol.TIMES, TerminalSymbol.DIVIDE, TerminalSymbol.OPEN, TerminalSymbol.CLOSE);
	private static EnumMap<TerminalSymbol, String> stringMap = new EnumMap<>(TerminalSymbol.class);
	private static HashMap<String, TerminalSymbol> backwardsStringMap = new HashMap<>();
	
	static {
		// Initializes the map from TerminalSymbols to their string values, and another map from string values to TerminalSymbols
		stringMap.put(TerminalSymbol.PLUS, "+");
		backwardsStringMap.put("+", TerminalSymbol.PLUS);
		
		stringMap.put(TerminalSymbol.MINUS, "-");
		backwardsStringMap.put("-", TerminalSymbol.MINUS);
		
		stringMap.put(TerminalSymbol.TIMES, "*");
		backwardsStringMap.put("*", TerminalSymbol.TIMES);
		
		stringMap.put(TerminalSymbol.DIVIDE, "/");
		backwardsStringMap.put("/", TerminalSymbol.DIVIDE);
		
		stringMap.put(TerminalSymbol.OPEN, "(");
		backwardsStringMap.put("(", TerminalSymbol.OPEN);
		
		stringMap.put(TerminalSymbol.CLOSE, ")");
		backwardsStringMap.put(")", TerminalSymbol.CLOSE);
	}
	
	private TerminalSymbol type;
	
	private Connector(TerminalSymbol type) {
		this.type = type;
	}

	// Use this
	// Returns a new connector, throws exception if type is null
	public static final Connector build(TerminalSymbol type) throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(type, "type cannot be null");
		if (!typeIsValid(type)) throw new IllegalArgumentException("type must be one of the following: PLUS, MINUS, TIMES, DIVIDE, OPEN, CLOSE");
		
		return cache.get(type, Connector::new);
	}

	// Use this + TerminalSymbol.symbolFromParsing from Arik's
	// Builds the appropriate connector from a string value, or throws an exception
	static final Connector build(String stringValue) throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(stringValue, "Cannot build a connector from a null string value");
		TerminalSymbol type = backwardsStringMap.get(stringValue);
		
		if (type == null) throw new IllegalArgumentException("'" + stringValue + "' does not correspond to a valid connector type");
		
		return Connector.build(type);
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
		return Connector.stringMap.get(type);
	}
	
}
