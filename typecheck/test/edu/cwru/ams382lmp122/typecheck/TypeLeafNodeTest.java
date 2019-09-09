package edu.cwru.ams382lmp122.typecheck;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests the methods within the TypeLeafNode class
 */
public class TypeLeafNodeTest {

    /**
     *  LeafNode with Operator for testing
     */
    private TypeLeafNode testOperatorNode;
    /**
     * LeafNode with Type for testing
     */
    private TypeLeafNode testTypeNode;

    /**
     * Sets up two types of LeafNodes for use in testing:
     * -One with an Operator
     * -One With an Integer
     */
    @Before
    public void setup() {
        testOperatorNode = TypeLeafNode.build(Operator.MINUS);
        testTypeNode = TypeLeafNode.build(Type.build("Integer"));
    }

    /**
     * Data Coverage | Example: element = null
     * Should return a NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void build_Null(){
        TypeLeafNode.build(null);
    }

    /**
     * Code Coverage | Example: element = Operator.PLUS
     * Should return a newly built TypeLeafNode (Expected condition)
     */
    @Test
    public void build_Nominal() {
        assertEquals(TypeLeafNode.build(Operator.PLUS).getElement(), Operator.PLUS);
    }

    /**
     * Branch & Code Coverage | Example: element = Type("Integer")
     * Should return false (Condition is false)
     */
    @Test
    public void isOperator_NonOperator() {
        assertFalse(testTypeNode.isOperator());
    }

    /**
     * Code Coverage | Example: element = Operator.MINUS
     * Should return true (Expected condition)
     */
    @Test
    public void isOperator_Nominal(){
        assertTrue(testOperatorNode.isOperator());
    }

    /**
     * Code Coverage | Example: Valid TypeLeafNode
     * Should always return null (Standard behavior)
     */
    @Test
    public void getChildren() {
        assertNull(testOperatorNode.getChildren());
        //Confirm routine always returns null
        assertNull(testTypeNode.getChildren());
    }

    /**
     * Branch & Code Coverage | Example: element = Operator.MINUS
     * Should return empty Optional value (
     */
    @Test
    public void evaluatedType_Empty() throws MissingRuleException {
        assertFalse(testOperatorNode.evaluatedTypeForRules(Collections.emptySet()).isPresent());
    }

    @Test
    public void evaluatedType_Nominal() throws MissingRuleException {
        Optional<Type> evaluatedType = testTypeNode.evaluatedTypeForRules(Collections.emptySet());
        assertTrue(evaluatedType.isPresent());
        //Assure type is correct
        assertEquals(evaluatedType.get(), Type.build("Integer"));
    }

}