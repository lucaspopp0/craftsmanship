package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import parser.Connector;
import parser.Node;
import parser.NonTerminalSymbol;
import parser.TerminalSymbol;
import parser.Token;
import parser.Variable;

public final class NonTerminalSymbolTest {
	
	private static HashMap<String, TerminalSymbol> stringMap = new HashMap<>();
	
	static {
		// Initializes a map from connector strings to their values
		stringMap.put("+", TerminalSymbol.PLUS);
		stringMap.put("-", TerminalSymbol.MINUS);
		stringMap.put("*", TerminalSymbol.TIMES);
		stringMap.put("/", TerminalSymbol.DIVIDE);
		stringMap.put("(", TerminalSymbol.OPEN);
		stringMap.put(")", TerminalSymbol.CLOSE);
	}
	
	@Test (expected = NullPointerException.class)
	public void parseCorrectlyThrowsNullPointerException() {
		NonTerminalSymbol.EXPRESSION.parse(null);
	}
	
	@Test (expected = NullPointerException.class)
	public void parseInputCorrectlyThrowsNullPointerException() {
		NonTerminalSymbol.parseInput(null);
	}
	
	@Test
	public void example1ShouldWork() {
		Optional<Node> root = NonTerminalSymbol.parseInput(tokenize("a + b / c"));
		
		assertTrue("Should successfully parse [a,+,b,/,c] as an EXPRESSION", root.isPresent());
		assertEquals("Parsed result should have be [a, +, [b, /, c]]", "[a, +, [b, /, c]]", root.get().toString());
	}
	
	@Test
	public void example2ShouldWork() {
		Optional<Node> root = NonTerminalSymbol.parseInput(tokenize("a * b / c"));
		
		assertTrue("Should successfully parse [a,*,b,/,c] as an EXPRESSION", root.isPresent());
		assertEquals("Parsed result should have be [a, *, b, /, c]", "[a, *, b, /, c]", root.get().toString());
	}
	
	// Helper method. Converts a string into a list of tokens
	private List<Token> tokenize(String input) {
		List<Token> out = new LinkedList<>();
		String stripped = input.replaceAll("\\s+", "");
		
		String nextLetter;
		Token nextToken;
		for (int i = 0; i < stripped.length(); i++) {
			nextLetter = stripped.substring(i, i + 1);
			
			try {
				nextToken = Connector.build(NonTerminalSymbolTest.stringMap.get(nextLetter));
			} catch (NullPointerException e) {
				nextToken = Variable.build(nextLetter);
			}
			
			out.add(nextToken);
		}
		
		return out;
	}
	
}
