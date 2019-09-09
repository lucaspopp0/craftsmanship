package edu.cwru.ams382lmp122.typecheck;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the operator enum class and its static methods
 */
public class OperatorTest {

    /**
     * Code Coverage | Example: representation = "+"
     */
    @Test
    public void toString_Nominal() {
        assertEquals(Operator.PLUS.toString(), "+");
    }

    /**
     * Data Coverage | Example: terminalSymbol = null
     * Should throw NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void correspondsToOperator_Null() {
        Operator.correspondsToOperator(null);
    }

    /**
     * Branch & Code Coverage | Example: terminalSymbol = TerminalSymbol.OPEN
     * Should return false (Condition is false)
     */
    @Test
    public void correspondsToOperator_NonOperator(){
        assertFalse(Operator.correspondsToOperator(TerminalSymbol.OPEN));
    }

    /**
     * Code Coverage | Example: terminalSymbol = TerminalSymbol.PLUS
     * Should return true (Expected case)
     */
    @Test
    public void correspondsToOperator_Nominal(){
        assertTrue(Operator.correspondsToOperator(TerminalSymbol.PLUS));
    }

    /**
     * Data Coverage | Example: terminalSymbol = null
     * Should throw NullPointerException (Bad Data)
     */
    @Test (expected = NullPointerException.class)
    public void fromTerminalSymbol_Null() {
        Operator.fromTerminalSymbol(null);
    }

    /**
     * Branch & Code Coverage | Example: terminalSymbol = TerminalSymbol.CLOSE
     * Should return null (Condition is false)
     */
    @Test
    public void fromTerminalSymbol_NonOperator(){
        assertNull(Operator.fromTerminalSymbol(TerminalSymbol.CLOSE));
    }

    /**
     * Code Coverage | Example: terminalSymbol = TerminalSymbol.MINUS
     * Should return corresponding Operator.MINUS (Expected case)
     */
    @Test
    public void fromTerminalSymbol_Nominal(){
        assertEquals(Operator.fromTerminalSymbol(TerminalSymbol.MINUS), Operator.MINUS);
    }
}