package edu.cwru.ams382lmp122.typecheck;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the methods for the TypeInternalNode class
 */
public class TypeInternalNodeTest {

    /**
     * The Internal Node to be used for testing
     */
    private static TypeInternalNode testNodeOne;
    private static TypeInternalNode testNodeTwo;
    private static Set<TypeConversionRule> testRulesOne;
    private static Set<TypeConversionRule> testRulesTwo;

    /**
     * Sets up an Internal Node to be used for testing
     * testNode: [(Integer * Double) - Float]
     */
    @BeforeClass
    public static void setUp() throws MissingRuleException {
        //A non-nested internalNode to be tested
        testNodeOne = TypeInternalNode.build(Arrays.asList(TypeLeafNode.build(Type.build("Integer")), TypeLeafNode.build(Operator.TIMES), TypeLeafNode.build(Type.build("Double"))));
        TypeLeafNode leafOne = TypeLeafNode.build(Operator.MINUS);
        TypeLeafNode leafTwo =  TypeLeafNode.build(Type.build("Float"));

        //A nested internalNode to be tested
        testNodeTwo = TypeInternalNode.build(
                Arrays.asList(
                        testNodeOne,
                        leafOne,
                        leafTwo
                )
        );

        testRulesOne = new HashSet<>();
        testRulesOne.add(TypeConversionRule.build(Type.build("Integer"), Operator.TIMES, Type.build("Double"), Type.build("Double")));

        testRulesTwo = new HashSet<>(testRulesOne);
        testRulesTwo.add(TypeConversionRule.build(Type.build("Double"), Operator.MINUS, Type.build("Float"), Type.build("Float")));
    }

    /**
     * Data Coverage | Example: Rules = empty
     * Should throw MissingRuleException (Bad Data)
     */
    @Test (expected = MissingRuleException.class)
    public void evaluateTypeUsingRules_MissingRules() throws MissingRuleException {
        TypeInternalNode testNode = TypeInternalNode.build(testNodeOne.getChildren());
        //Throw Exception since there is no rule to evaluate
        testNode.evaluatedTypeForRules(Collections.emptySet());
    }

    /**
     * Data Coverage | Example: InternalNode holding another InternalNode
     * Should Evaluate as normal by calling routine on Internal child (Expected condition)
     */
    @Test
    public void evaluateTypeUsingRules_NestedNode() throws MissingRuleException {
        Optional<Type> evaluatedType = testNodeTwo.evaluatedTypeForRules(testRulesTwo);
        assertTrue(evaluatedType.isPresent());
        evaluatedType.ifPresent(type -> assertEquals(type.getStringValue(), "Float"));
    }

    /**
     * Code Coverage | Example: Final Type = Double
     * Should evaluate to type Double based on given Rules (Expected condition)
     */
    @Test
    public void evaluateTypeUsingRules_Nominal() throws MissingRuleException {
        Optional<Type> evaluatedType = testNodeOne.evaluatedTypeForRules(testRulesOne);
        assertTrue(evaluatedType.isPresent());
        evaluatedType.ifPresent(type -> assertEquals(type.getStringValue(), "Double"));
    }

    /**
     * Data & Code Coverage | Example: children = null
     * Should throw NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void build_Null() {
        TypeInternalNode.build(null);
    }

    /**
     * Code Coverage | Example: children = testNodeOne (Integer * Double)
     * Should build an InternalNode with the given child, and initialize proper field values (As expected)
     */
    @Test
    public void build_Nominal() throws MissingRuleException {
        TypeInternalNode buildTest = TypeInternalNode.build(Collections.singletonList(testNodeOne));
        assertTrue(buildTest.getChildren().contains(testNodeOne));
    }

    /**
     * Code Coverage | Example: Standard TypeInternalNode
     * Should always return false (Standard behavior)
     */
    @Test
    public void isOperator() {
        assertFalse(testNodeOne.isOperator());
        assertFalse(testNodeTwo.isOperator());
    }

    /**
     * Code Coverage | Example: Standard TypeInternalNode
     * Should always return null for getElement (Standard behavior)
     */
    @Test
    public void getElement(){
        assertNull(testNodeOne.getElement());
    }
}