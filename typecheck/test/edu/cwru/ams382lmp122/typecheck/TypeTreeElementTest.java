package edu.cwru.ams382lmp122.typecheck;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the static method within the TypeTreeElementInterface
 */
public class TypeTreeElementTest {

    /**
     * Data Coverage | Example: Token = null
     * Should return false
     */
    @Test
    public void isValidElement_Null() {
        assertFalse(TypeTreeElement.isValidElement(null));
    }

    /**
     * Branch Coverage | Example: Token = ")", Neither variable nor connector operator
     * Should return false (second condition false)
     */
    @Test
    public void isValidElement_Parenthesis(){
        Token token = Connector.build(TerminalSymbol.OPEN);
        assertFalse(TypeTreeElement.isValidElement(token));
    }

    /**
     * Branch and Code Coverage | Example: Token = Variable ("a")
     * Should return true (First condition true & expected)
     */
    @Test
    public void isValidElement_NominalVariable(){
        Variable variable = Variable.build("a");
        assertTrue(TypeTreeElement.isValidElement(variable));
    }

    /**
     * Code Coverage | Example: Token = Connector ("+")
     * Should return true (Expected condition)
     */
    @Test
    public void isValidElement_NominalConnector(){
        Token token = Connector.build(TerminalSymbol.PLUS);
        assertTrue(TypeTreeElement.isValidElement(token));
    }
}