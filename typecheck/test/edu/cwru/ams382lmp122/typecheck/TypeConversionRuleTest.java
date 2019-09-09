package edu.cwru.ams382lmp122.typecheck;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the methods for the TypeConversionRule class
 */
public class TypeConversionRuleTest {

    /**
     * Conversion Rule to be used for testing
     */
    private TypeConversionRule testRuleOne;

    /**
     * Sets up a standard conversion rule with values to be used across testing
     */
    @Before
    public void setUp() {
        testRuleOne = TypeConversionRule.build(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Double"),
                Type.build("Double")
        );
    }

    /**
     * Data Coverage | Example: leftType = null
     * Should throw new NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void build_LeftNull() {
        TypeConversionRule.build(
                null,
                Operator.TIMES,
                Type.build("Double"),
                Type.build("Double")
        );
    }

    /**
     * Data Coverage | Example: operator = null
     * Should throw new NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void build_OperatorNull() {
        TypeConversionRule.build(
                Type.build("Double"),
                null,
                Type.build("Double"),
                Type.build("Double")
        );
    }

    /**
     * Data Coverage | Example: rightType = null
     * Should throw new NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void build_rightNull() {
        TypeConversionRule.build(
                Type.build("Double"),
                Operator.TIMES,
                null,
                Type.build("Double")
        );
    }

    /**
     * Data Coverage | Example: result = null
     * Should throw new NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void build_resultNull() {
        TypeConversionRule.build(
                Type.build("Double"),
                Operator.TIMES,
                Type.build("Double"),
                null
        );
    }

    /**
     * Code Coverage | Example: Integer * Double = Double
     * Should return newly built TypeConversionRule from the given values(Bad Data)
     */
    @Test
    public void build_Nominal() {
        TypeConversionRule buildTest = TypeConversionRule.build(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Double"),
                Type.build("Double")
        );
        //Test values are stored correctly
        assertEquals(buildTest.getLeftType().getStringValue(), testRuleOne.getLeftType().getStringValue());
        assertEquals(buildTest.getRightType().getStringValue(), testRuleOne.getRightType().getStringValue());
        assertEquals(buildTest.getOperator().toString(), testRuleOne.getOperator().toString());
        assertEquals(buildTest.getResultingType().getStringValue(), testRuleOne.getResultingType().getStringValue());
    }

    /**
     * Branch Coverage | Example: LeftType mismatches
     * Should return false (First condition false)
     */
    @Test
    public void matches_LeftFalse() {
        assertFalse(testRuleOne.matches(
                Type.build("Float"),
                Operator.TIMES,
                Type.build("Double")
                ));
    }

    /**
     * Branch Coverage | Example: Operator mismatches
     * Should return false (Second condition false)
     */
    @Test
    public void matches_OperatorFalse() {
        assertFalse(testRuleOne.matches(
                Type.build("Integer"),
                Operator.MINUS,
                Type.build("Double")
        ));
    }

    /**
     * Branch Coverage | Example: rightType mismatches
     * Should return false (Third condition false)
     */
    @Test
    public void matches_RightFalse() {
        assertFalse(testRuleOne.matches(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Float")
        ));
    }

    /**
     * Code Coverage | Example: All arguments match
     * Should return true (Nominal condition)
     */
    @Test
    public void matches_Nominal(){
        assertTrue(testRuleOne.matches(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Double")
        ));
    }

    /**
     * Code Coverage | Example: Matching Rule
     */
    @Test
    public void conflictsWith() {
        TypeConversionRule ruleCase = TypeConversionRule.build(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Double"),
                Type.build("Double")
        );
        assertTrue(testRuleOne.conflictsWith(ruleCase));
    }

    /**
     * Code Coverage | Example: "Integer * Double"
     */
    @Test
    public void conditionAsString() {
        assertEquals(testRuleOne.conditionAsString(), "Integer * Double");
    }

    /**
     * Branch Coverage | Example: LeftType false
     */
    @Test
    public void equals_LeftFalse() {
        TypeConversionRule ruleCase = TypeConversionRule.build(
                Type.build("Float"),
                Operator.TIMES,
                Type.build("Double"),
                Type.build("Double")
        );
        assertFalse(testRuleOne.equals(ruleCase));
    }

    /**
     * Branch Coverage | Example: Operator false
     */
    @Test
    public void equals_OperatorFalse(){
        TypeConversionRule ruleCase = TypeConversionRule.build(
                Type.build("Integer"),
                Operator.MINUS,
                Type.build("Double"),
                Type.build("Double")
        );
        assertFalse(testRuleOne.equals(ruleCase));
    }

    /**
     * Branch Coverage | Example: rightType false
     */
    @Test
    public void equals_rightFalse(){
        TypeConversionRule ruleCase = TypeConversionRule.build(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Float"),
                Type.build("Double")
        );
        assertFalse(testRuleOne.equals(ruleCase));
    }

    /**
     * Branch Coverage | Example: resultType false
     */
    @Test
    public void equals_resultFalse(){
        TypeConversionRule ruleCase = TypeConversionRule.build(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Double"),
                Type.build("Integer")
        );
        assertFalse(testRuleOne.equals(ruleCase));
    }

    /**
     * Branch & Data Coverage | Example: null case
     */
    @Test
    public void equals_NonRule(){
        assertFalse(testRuleOne.equals(null));
    }

    /**
     * Code Coverage | Example: Matching rule
     */
    @Test
    public void equals_Nominal(){
        TypeConversionRule ruleCase = TypeConversionRule.build(
                Type.build("Integer"),
                Operator.TIMES,
                Type.build("Double"),
                Type.build("Double")
        );
        assertTrue(testRuleOne.equals(ruleCase));
    }

}