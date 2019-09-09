package edu.cwru.ams382lmp122.typecheck;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ExpressionTypeHandlerTest {

    private static Map<Variable, Type> variableTypeMap;
    private static Set<TypeConversionRule> typeConversionRules;

    @BeforeClass
    public static void setup() {
        variableTypeMap = new HashMap<>();
        typeConversionRules = new HashSet<>();

        variableTypeMap.put(Variable.build("a"), Type.build("int"));
        variableTypeMap.put(Variable.build("b"), Type.build("int"));
        variableTypeMap.put(Variable.build("c"), Type.build("double"));

        typeConversionRules.add(TypeConversionRule.build(Type.build("int"), Operator.PLUS, Type.build("double"), Type.build("double")));
        typeConversionRules.add(TypeConversionRule.build(Type.build("int"), Operator.PLUS, Type.build("int"), Type.build("int")));
        typeConversionRules.add(TypeConversionRule.build(Type.build("int"), Operator.TIMES, Type.build("double"), Type.build("double")));
    }

    /*
     * Method: build
     * Checks that a null pointer exception is thrown when the variable type map is null
     */
    @Test (expected = NullPointerException.class)
    public void buildWithNullTypeMapShouldThrow() throws ConflictingRulesException {
        Set<TypeConversionRule> rules = new HashSet<>();

        ExpressionTypeHandler.build(null, rules);
    }

    /*
     * Method: build
     * Checks that a null pointer exception is thrown when rules is null
     */
    @Test (expected = NullPointerException.class)
    public void buildWithNullRulesShouldThrow() throws ConflictingRulesException {
        Map<Variable, Type> typeMap = new HashMap<>();

        ExpressionTypeHandler.build(typeMap, null);
    }

    /*
     * Method: build
     * Checks that a illegal argument exception is thrown when the variable type map is invalid
     */
    @Test (expected = IllegalArgumentException.class)
    public void buildWithInvalidMapShouldThrow() throws ConflictingRulesException {
        Map<Variable, Type> typeMap = new HashMap<>();
        typeMap.put(null, null);

        Set<TypeConversionRule> rules = new HashSet<>();

        ExpressionTypeHandler.build(typeMap, rules);
    }

    /*
     * Method: build
     * Checks that an exception is thrown when there are conflicting rules
     */
    @Test (expected = ConflictingRulesException.class)
    public void buildWithConflictingRulesShouldThrow() throws ConflictingRulesException {
        Map<Variable, Type> typeMap = new HashMap<>();
        Set<TypeConversionRule> rules = new HashSet<>();
        rules.add(TypeConversionRule.build(Type.build("int"), Operator.PLUS, Type.build("double"), Type.build("String")));
        rules.add(TypeConversionRule.build(Type.build("int"), Operator.PLUS, Type.build("double"), Type.build("float")));

        ExpressionTypeHandler.build(typeMap, rules);
    }

    /*
     * Method: build
     * Duplicate rules should not throw
     */
    @Test
    public void buildWithDuplicateRulesShouldNotThrow() throws ConflictingRulesException {
        Map<Variable, Type> typeMap = new HashMap<>();
        Set<TypeConversionRule> rules = new HashSet<>();
        rules.add(TypeConversionRule.build(Type.build("int"), Operator.PLUS, Type.build("double"), Type.build("String")));
        rules.add(TypeConversionRule.build(Type.build("int"), Operator.PLUS, Type.build("double"), Type.build("String")));

        try {
            ExpressionTypeHandler.build(typeMap, rules);
        } catch (ConflictingRulesException e) {
            fail();
        }
    }

    /*
     * Method: build
     * Nominal case
     */
    @Test
    public void nominalBuildShouldNotThrow() {
        Map<Variable, Type> typeMap = new HashMap<>();
        Set<TypeConversionRule> rules = new HashSet<>();

        try {
            ExpressionTypeHandler.build(typeMap, rules);
        } catch (NullPointerException | IllegalArgumentException | ConflictingRulesException e) {
            fail();
        }
    }

    /*
     * Method: variableTypeMapIsValid
     * Checks that the method returns true for a valid map
     */
    @Test
    public void validVariableTypeMapTest() {
        Map<Variable, Type> typeMap = new HashMap<>();
        typeMap.put(Variable.build("a"), Type.build("int"));

        assertTrue(ExpressionTypeHandler.TestHook.variableTypeMapIsValid(typeMap));
    }

    /*
     * Method: variableTypeMapIsValid
     * Checks that the method returns false for an invalid map
     */
    @Test
    public void invalidVariableTypeMapTest() {
        Map<Variable, Type> typeMap = new HashMap<>();
        typeMap.put(null, Type.build("int"));

        assertFalse(ExpressionTypeHandler.TestHook.variableTypeMapIsValid(typeMap));

        typeMap.remove(null);
        typeMap.put(Variable.build("a"), null);

        assertFalse(ExpressionTypeHandler.TestHook.variableTypeMapIsValid(typeMap));
    }

    /*
     * Method: evaluateExpressionType
     * Checks that a null pointer exception is thrown when the tree root is null
     */
    @Test (expected = NullPointerException.class)
    public void evaluateWithNullRootShouldThrow() {
        try {
            ExpressionTypeHandler.build(variableTypeMap, typeConversionRules).evaluateExpressionType(null);
        } catch (IllegalArgumentException | ConflictingRulesException | MissingTypeException | MissingRuleException e) {
            fail();
        }
    }

    /*
     * Method: evaluateExpressionType
     * Checks that an exception is thrown when the tree is invalid
     */
    @Test (expected = IllegalArgumentException.class)
    public void evaluateWithInvalidTreeShouldThrow() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Variable.build("b")));
        ParserInternalNode root = ParserInternalNode.build(children);

        try {
            ExpressionTypeHandler.build(variableTypeMap, typeConversionRules).evaluateExpressionType(root);
        } catch (NullPointerException | ConflictingRulesException | MissingTypeException | MissingRuleException e) {
            fail();
        }
    }

    /*
     * Method: evaluateExpressionType
     * Nominal
     */
    @Test public void evaluateWithValidTreeShouldRun() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.PLUS)));
        children.add(ParserLeafNode.build(Variable.build("c")));
        ParserInternalNode root = ParserInternalNode.build(children);

        try {
            Optional<Type> result = ExpressionTypeHandler.build(variableTypeMap, typeConversionRules).evaluateExpressionType(root);
            assertTrue(result.isPresent());
            assertEquals(Type.build("double"), result.get());
        } catch (Exception e) {
            fail();
        }
    }

    /*
     * Method: convertParserTreeToTypeTree
     * Branch coverage: Tree with internal nodes
     */
    @Test
    public void convertParserTreeWithInternalNodes() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.PLUS)));
        children.add(ParserLeafNode.build(Variable.build("c")));
        ParserInternalNode nestedChild = ParserInternalNode.build(children);

        children.clear();
        children.add(ParserLeafNode.build(Variable.build("b")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.PLUS)));
        children.add(nestedChild);
        ParserInternalNode root = ParserInternalNode.build(children);

        try {
            TypeInternalNode typeRoot = ExpressionTypeHandler.TestHook.convertParserTreeToTypeTree(root, variableTypeMap, typeConversionRules);

            assertEquals(3, typeRoot.getChildren().size());
            assertEquals(3, typeRoot.getChildren().get(2).getChildren().size());
        } catch (ConflictingRulesException | MissingTypeException e) {
            fail();
        }
    }

    /*
     * Method: simplifiedChildrenList
     * Has leading minus
     */
    @Test
    public void simplifyLeadingMinus() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.MINUS)));
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.PLUS)));
        children.add(ParserLeafNode.build(Variable.build("b")));

        try {
            assertEquals(3, ExpressionTypeHandler.TestHook.simplifiedChildrenList(children).size());
        } catch (ConflictingRulesException e) {
            fail();
        }
    }

    /*
     * Method: simplifiedChildrenList
     * Is surrounded by parentheses
     */
    @Test
    public void simplifyParentheses() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.OPEN)));
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.CLOSE)));

        try {
            assertEquals(1, ExpressionTypeHandler.TestHook.simplifiedChildrenList(children).size());
        } catch (ConflictingRulesException e) {
            fail();
        }
    }

    /*
     * Method: simplifiedChildrenList
     * InternalNode is surrounded by parentheses
     */
    @Test
    public void simplifyParenthesesAroundInternalNode() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.PLUS)));
        children.add(ParserLeafNode.build(Variable.build("b")));
        ParserInternalNode node = ParserInternalNode.build(children);

        children.clear();
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.OPEN)));
        children.add(node);
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.CLOSE)));

        try {
            children = ExpressionTypeHandler.TestHook.simplifiedChildrenList(children);
            assertEquals(3, children.size());
            assertFalse(children.get(0) instanceof ParserLeafNode && ((ParserLeafNode)children.get(0)).getToken().matches(TerminalSymbol.OPEN));
            assertFalse(children.get(2) instanceof ParserLeafNode && ((ParserLeafNode)children.get(0)).getToken().matches(TerminalSymbol.CLOSE));
        } catch (ConflictingRulesException e) {
            fail();
        }
    }

    /*
     * Method: parserNodeMatchesTerminalSymbol
     * Does match
     */
    @Test
    public void parserNodeDoesMatch() {
        assertTrue(ExpressionTypeHandler.TestHook.parserNodeMatchesTerminalSymbol(ParserLeafNode.build(Connector.build(TerminalSymbol.PLUS)), TerminalSymbol.PLUS));
    }

    /*
     * Method: parserNodeMatchesTerminalSymbol
     * Leaf node but does not match
     */
    @Test
    public void parserNodeDoesNotMatch_wrongSymbol() {
        assertFalse(ExpressionTypeHandler.TestHook.parserNodeMatchesTerminalSymbol(ParserLeafNode.build(Connector.build(TerminalSymbol.PLUS)), TerminalSymbol.MINUS));
    }

    /*
     * Method: parserNodeMatchesTerminalSymbol
     * Not a leaf node
     */
    @Test
    public void parserNodeDoesNotMatch_wrongNode() {
        assertFalse(ExpressionTypeHandler.TestHook.parserNodeMatchesTerminalSymbol(ParserInternalNode.build(new LinkedList<>()), TerminalSymbol.PLUS));
    }

    /*
     * Method: listOfChildrenHasLeadingMinus
     * True if leading minus
     */
    @Test
    public void listOfChildrenHasLeadingMinus_nominal() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.MINUS)));
        children.add(ParserLeafNode.build(Variable.build("a")));
        assertTrue(ExpressionTypeHandler.TestHook.listOfChildrenHasLeadingMinus(children));
    }

    /*
     * Method: listOfChildrenHasLeadingMinus
     * False with only one child
     */
    @Test
    public void listOfChildrenHasLeadingMinus_isFalseWithOneChild() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Variable.build("a")));
        assertFalse(ExpressionTypeHandler.TestHook.listOfChildrenHasLeadingMinus(children));
    }

    /*
     * Method: listOfChildrenIsSurroundedByParentheses
     * True if surrounded by parentheses
     */
    @Test
    public void listOfChildrenIsSurroundedByParentheses_nominal() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.OPEN)));
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.CLOSE)));
        assertTrue(ExpressionTypeHandler.TestHook.listOfChildrenIsSurroundedByParentheses(children));
    }

    /*
     * Method: listOfChildrenIsSurroundedByParentheses
     * False with only one parenthesis
     */
    @Test
    public void listOfChildrenIsSurroundedByParentheses_isFalseIfMoreThanThreeChildren() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.OPEN)));
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.CLOSE)));
        children.add(ParserLeafNode.build(Variable.build("b")));
        assertFalse(ExpressionTypeHandler.TestHook.listOfChildrenIsSurroundedByParentheses(children));
    }

    /*
     * Method: listOfChildrenIsSurroundedByParentheses
     * False with only one parenthesis
     */
    @Test
    public void listOfChildrenIsSurroundedByParentheses_isFalseIfNotSurrounded() {
        List<ParserNode> children = new LinkedList<>();
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.OPEN)));
        children.add(ParserLeafNode.build(Variable.build("a")));
        children.add(ParserLeafNode.build(Connector.build(TerminalSymbol.OPEN)));
        assertFalse(ExpressionTypeHandler.TestHook.listOfChildrenIsSurroundedByParentheses(children));
    }

    /*
     * Method: findTypeOfVariable
     * Throws if type not found
     */
    @Test (expected = MissingTypeException.class)
    public void findTypeOfVariableThrowsIfTypeNotFound() throws MissingTypeException {
        ExpressionTypeHandler.TestHook.findTypeOfVariable(Variable.build("k"), variableTypeMap);
    }

    /*
     * Method: isLeafAndHasValidToken
     * Test for leaf node with invalid token (parenthesis)
     */
    @Test
    public void isLeafButInvalidToken() {
        ParserNode node = ParserLeafNode.build(Connector.build(TerminalSymbol.OPEN));
        assertFalse(ExpressionTypeHandler.TestHook.isLeafAndHasValidToken(node));
    }

}