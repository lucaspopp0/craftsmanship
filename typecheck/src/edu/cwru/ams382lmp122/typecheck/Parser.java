package edu.cwru.ams382lmp122.typecheck;

import java.util.*;

public class Parser {

	public static void main(String[] args) {
		ParserInternalNode root = (ParserInternalNode)NonTerminalSymbol.parseInput(tokenize("a+b/c")).get();
		Map<Variable, Type> variableTypeMap = new HashMap<>();
		variableTypeMap.put(Variable.build("a"), Type.build("int"));
		variableTypeMap.put(Variable.build("b"), Type.build("int"));
		variableTypeMap.put(Variable.build("c"), Type.build("int"));

		Set<TypeConversionRule> rules = new HashSet<>();
		rules.add(TypeConversionRule.build(Type.build("int"), Operator.PLUS, Type.build("double"), Type.build("double")));
		rules.add(TypeConversionRule.build(Type.build("int"), Operator.DIVIDE, Type.build("int"), Type.build("double")));

		try {
			Optional<Type> out = Parser.evaluateExpressionType(root, variableTypeMap, rules);
			System.out.println(out);
		} catch (Exception e) {
			e.printStackTrace();
		}


//		Scanner stdin = new Scanner(System.in);
//	    String input;
//
//	    System.out.print("Input: ");
//
//	    while ((input = stdin.next()) != null) {
//            Optional<ParserNode> root = NonTerminalSymbol.parseInput(tokenize(input));
//
//            if (root.isPresent()) {
//                System.out.println("Parsed: " + root.get().toString());
//            } else {
//                System.out.printf("Error parsing expression '%s'\n\n", input);
//            }
//
//            System.out.print("Input: ");
//        }
	}
	
	// Converts a string to a list of tokens
	private static List<Token> tokenize(String input) {
		List<Token> out = new LinkedList<>();
		
		String nextLetter;
		Token nextToken;
		for (int i = 0; i < input.length(); i++) {
			nextLetter = input.substring(i, i + 1);

			if (Connector.isValidStringRepresentation(nextLetter)) {
				nextToken = Connector.build(nextLetter);
			} else {
				nextToken = Variable.build(nextLetter);
			}
			
			out.add(nextToken);
		}
		
		return out;
	}

	public static Optional<Type> evaluateExpressionType(ParserInternalNode parseTreeRoot, Map<Variable, Type> variableTypeMap, Set<TypeConversionRule> rules) throws NullPointerException, IllegalArgumentException, MissingTypeException, ConflictingRulesException, MissingRuleException {
		return ExpressionTypeHandler.build(variableTypeMap, rules).evaluateExpressionType(parseTreeRoot);
	}
	
}
