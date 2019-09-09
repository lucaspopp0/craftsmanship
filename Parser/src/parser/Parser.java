package parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

// Use this
public class Parser {
	
	public static void main(String[] args) {
		String input = String.join("", args).replaceAll("\\s+", "");
		Optional<Node> root = NonTerminalSymbol.parseInput(tokenize(input));

		if (root.isPresent()) {
			System.out.println("Success: " + root.get().toString());
		} else {
			System.out.printf("Error parsing expression '%s'\n", input);
		}
	}
	
	// Converts a string to a list of tokens
	public static List<Token> tokenize(String input) {
		List<Token> out = new LinkedList<>();
		
		String nextLetter;
		Token nextToken;
		for (int i = 0; i < input.length(); i++) {
			nextLetter = input.substring(i, i + 1);

			// TODO: Bad practice using exceptions like this
			try {
				nextToken = Connector.build(nextLetter);
			} catch (Exception e) {
				nextToken = Variable.build(nextLetter);
			}
			
			out.add(nextToken);
		}
		
		return out;
	}
	
}
