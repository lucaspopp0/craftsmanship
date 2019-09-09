package edu.cwru.ams382lmp122.typecheck;

import java.util.*;

/** A handler used to evaluate the resulting type of an expression, given a list of variable types and a set of type conversion rules */
class ExpressionTypeHandler {

    /** A map of variables to their corresponding types */
    private Map<Variable, Type> variableTypeMap;

    /** A set of rules to follow for type synthesis */
    private Set<TypeConversionRule> typeConversionRules;

    /**
     * Constructs an <code>ExpressionTypeHandler</code> with the specified variable types and type conversion rules
     * @param variableTypeMap       map of variables to their types
     * @param typeConversionRules   set of type conversion rules to use to determine an expression's type
     */
    private ExpressionTypeHandler(Map<Variable, Type> variableTypeMap, Set<TypeConversionRule> typeConversionRules) {
        this.variableTypeMap = variableTypeMap;
        this.typeConversionRules = typeConversionRules;
    }

    /**
     * Returns a new <code>ExpressionTypeHandler</code> with the specified variable types and type conversion rules
     * @param variableTypeMap               map of variables to their types
     * @param typeConversionRules           set of type conversion rules to use to determine an expression's type
     * @return                              a new <code>ExpressionTypeHandler</code> with the specified variable types and type conversion rules
     * @throws NullPointerException         if <code>variableTypeMap</code> or <code>typeConversionRules</code> is null
     * @throws IllegalArgumentException     if <code>variableTypeMap</code> contains a null key/value
     * @throws ConflictingRulesException    if two rules from <code>typeConversionRules</code> conflict
     */
    static ExpressionTypeHandler build(Map<Variable, Type> variableTypeMap, Set<TypeConversionRule> typeConversionRules) throws NullPointerException, IllegalArgumentException, ConflictingRulesException {
        Objects.requireNonNull(variableTypeMap, "Map of variable types cannot be null");
        Objects.requireNonNull(typeConversionRules, "Set of type conversion rules cannot be null");

        if (!variableTypeMapIsValid(variableTypeMap)) throw new IllegalArgumentException("Map of variable types cannot contain a null key/value");

        for (TypeConversionRule rule : typeConversionRules) {
            Optional<TypeConversionRule> conflictingRule = conflictingRuleFromSet(rule, typeConversionRules);

            if (conflictingRule.isPresent()) throw new ConflictingRulesException(rule, conflictingRule.get());
        }

        return new ExpressionTypeHandler(variableTypeMap, typeConversionRules);
    }

    /**
     * Returns an <code>Optional</code> indicating whether the given rule conflicts with any other rules in the given set
     * @param rule  the rule to check against
     * @param rules the set of rules to check for conflicts
     * @return      an optional containing the conflicting rule if one exists, and an empty optional otherwise
     */
    private static Optional<TypeConversionRule> conflictingRuleFromSet(TypeConversionRule rule, Set<TypeConversionRule> rules) {
        for (TypeConversionRule setRule : rules) {
            if (!setRule.equals(rule) && setRule.conflictsWith(rule)) {
                return Optional.of(setRule);
            }
        }

        return Optional.empty();
    }

    /**
     * Returns a <code>boolean</code> indicating whether the given map of variable types is valid
     * @param   types a map of <code>Variable</code> objects to their associated <code>Type</code> representations
     * @return  a <code>boolean</code> indicating whether the given map of variable types contains any <code>null</code> keys/values
     */
    private static boolean variableTypeMapIsValid(Map<Variable, Type> types) {
        return !types.containsKey(null) && !types.containsValue(null);
    }

    /**
     * Evaluates the given expression tree and returns its equivalent type
     * @param expressionRoot            the root of the parse tree
     * @return                          the evaluated type of the expression
     * @throws NullPointerException     if <code>expressionRoot</code> is null
     * @throws IllegalArgumentException if the parse tree is invalid
     * @throws MissingTypeException     if the parse tree references a variable which is not covered by the <code>ExpressionTypeHandler</code>'s <code>variableTypeMap</code>
     * @throws MissingRuleException     if the evaluation encounters a type/operator/type combination not covered by the <code>ExpressionTypeHandler</code>'s <code>typeConversionRules</code>
     */
    Optional<Type> evaluateExpressionType(ParserInternalNode expressionRoot) throws NullPointerException, IllegalArgumentException, MissingTypeException, MissingRuleException {
        Objects.requireNonNull(expressionRoot, "Expression root cannot be null");

        if (!parserTreeIsValid(expressionRoot)) throw new IllegalArgumentException("Provided expression tree was invalid");

        TypeInternalNode typeTreeRoot = convertParserTreeToTypeTree(expressionRoot);
        return typeTreeRoot.evaluatedTypeForRules(typeConversionRules);
    }

    /**
     * Returns true if the parse tree is a valid equation
     * @param root  the root of the parse tree
     * @return      a <code>boolean</code> indicating whether or not the parse tree is valid
     */
    private boolean parserTreeIsValid(ParserInternalNode root) {
        return NonTerminalSymbol.parseInput(root.toList()).isPresent();
    }

    /**
     * Converts a tree of parser nodes to a tree of type nodes
     * @param parserTreeRoot        the root of the parser tree
     * @return                      the root of the type tree
     * @throws MissingTypeException if the parser tree references a variable not covered by the <code>ExpressionTypeHandler</code>'s <code>variableTypeMap</code>
     */
    private TypeInternalNode convertParserTreeToTypeTree(ParserInternalNode parserTreeRoot) throws MissingTypeException {
        List<TypeNode> typeChildren = new LinkedList<>();
        List<ParserNode> parserChildren = simplifiedChildrenList(parserTreeRoot.getChildren());

        for (ParserNode child : parserChildren) {
            if (isLeafAndHasValidToken(child)) {
                typeChildren.add(TypeLeafNode.build(elementForToken(((ParserLeafNode)child).getToken())));
            } else if (child instanceof ParserInternalNode) {
                typeChildren.add(convertParserTreeToTypeTree((ParserInternalNode)child));
            }
        }

        return TypeInternalNode.build(typeChildren);
    }

    private static boolean isLeafAndHasValidToken(ParserNode child) {
        return child instanceof ParserLeafNode && TypeTreeElement.isValidElement(((ParserLeafNode)child).getToken());
    }

    /**
     * Simplifies a list of children to remove a leading minus or surrounding parentheses
     * @param children  the list of children to simplify
     * @return          the simplified list of children
     */
    private List<ParserNode> simplifiedChildrenList(List<ParserNode> children) {
        if (listOfChildrenHasLeadingMinus(children)) {
            return simplifiedChildrenList(children.subList(1, children.size()));
        } else if (listOfChildrenIsSurroundedByParentheses(children)) {
            if (children.get(1) instanceof ParserInternalNode) {
                return simplifiedChildrenList(((ParserInternalNode)children.get(1)).getChildren());
            } else {
                return simplifiedChildrenList(children.subList(1, 2));
            }
        }

        return children;
    }

    /**
     * Returns a boolean indicating whether or not a <code>ParserNode</code> is a leaf node, and if that node's token matches the given symbol
     * @param node      the node to check
     * @param symbol    the symbol to match
     * @return          true if the node is a leaf node and its token matches the given symbol, and false otherwise
     */
    private static boolean parserNodeMatchesTerminalSymbol(ParserNode node, TerminalSymbol symbol) {
        return (node instanceof ParserLeafNode) && ((ParserLeafNode)node).getToken().matches(symbol);
    }

    /**
     * Returns a boolean indicating whether or not the given list of children has a leading minus
     * @param children  the list of children to check
     * @return          a boolean indicating whether or not the given list of children has a leading minus
     */
    private static boolean listOfChildrenHasLeadingMinus(List<ParserNode> children) {
        return children.size() > 1 && parserNodeMatchesTerminalSymbol(children.get(0), TerminalSymbol.MINUS);
    }

    /**
     * Returns a boolean indicating whether or not the given list of children is surrounded by parentheses
     * @param children  the list of children to check
     * @return          a boolean indicating whether or not the given list of children is surrounded by parentheses
     */
    private static boolean listOfChildrenIsSurroundedByParentheses(List<ParserNode> children) {
        return children.size() == 3 && parserNodeMatchesTerminalSymbol(children.get(0), TerminalSymbol.OPEN) && parserNodeMatchesTerminalSymbol(children.get(2), TerminalSymbol.CLOSE);
    }

    /**
     * Returns the corresponding <code>TypeTreeElement</code> representation of the given token
     * @param token                 the token to convert
     * @return                      the <code>TypeTreeElement</code> representation of the given token
     * @throws MissingTypeException if the token is a variable which is not referenced in the <code>ExpressionTypeHandler</code>'s <code>variableTypeMap</code>
     */
    private TypeTreeElement elementForToken(Token token) throws MissingTypeException {
        if (token instanceof Variable) {
            return findTypeOfVariable((Variable)token);
        } else if (token instanceof Connector) {
            return Operator.fromTerminalSymbol(((Connector)token).getType());
        }

        return null;
    }

    /**
     * Returns the type of the given variable, according to the <code>variableTypeMap</code>
     * @param variable              a variable
     * @return                      the variable's assigned type
     * @throws MissingTypeException if the variable is not referenced in the <code>variableTypeMap</code>
     */
    private Type findTypeOfVariable(Variable variable) throws MissingTypeException {
        Type variableType = variableTypeMap.get(variable);

        if (variableType == null) throw new MissingTypeException(variable);

        return variableType;
    }

    static class TestHook {

        static boolean variableTypeMapIsValid(Map<Variable, Type> types) {
            return ExpressionTypeHandler.variableTypeMapIsValid(types);
        }

        static boolean isLeafAndHasValidToken(ParserNode node) {
            return ExpressionTypeHandler.isLeafAndHasValidToken(node);
        }

        static TypeInternalNode convertParserTreeToTypeTree(ParserInternalNode parserTreeRoot, Map<Variable, Type> variableTypeMap, Set<TypeConversionRule> typeConversionRules) throws ConflictingRulesException, MissingTypeException {
            return ExpressionTypeHandler.build(variableTypeMap, typeConversionRules).convertParserTreeToTypeTree(parserTreeRoot);
        }

        static List<ParserNode> simplifiedChildrenList(List<ParserNode> children) throws ConflictingRulesException {
            return ExpressionTypeHandler.build(new HashMap<>(), new HashSet<>()).simplifiedChildrenList(children);
        }

        static boolean parserNodeMatchesTerminalSymbol(ParserNode node, TerminalSymbol symbol) {
            return ExpressionTypeHandler.parserNodeMatchesTerminalSymbol(node, symbol);
        }

        static boolean listOfChildrenHasLeadingMinus(List<ParserNode> children) {
            return ExpressionTypeHandler.listOfChildrenHasLeadingMinus(children);
        }

        static boolean listOfChildrenIsSurroundedByParentheses(List<ParserNode> children) {
            return ExpressionTypeHandler.listOfChildrenIsSurroundedByParentheses(children);
        }

        static Type findTypeOfVariable(Variable variable, Map<Variable, Type> variableTypeMap) throws MissingTypeException {
            try {
                return ExpressionTypeHandler.build(variableTypeMap, new HashSet<>()).findTypeOfVariable(variable);
            } catch (ConflictingRulesException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
