package edu.cwru.ams382lmp122.typecheck;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the build method for the Type class
 */
public class TypeTest {

    /**
     * Data Coverage | Example: stringValue= null
     * Should throw NullPointerException
     */
    @Test (expected = NullPointerException.class)
    public void build_Null() {
        Type.build(null);
    }

    /**
     * Code Coverage | Example: stringValue = "Integer"
     * Should give newly built Type with the given string
     */
    @Test
    public void build_Nominal(){
        Type testType = Type.build("Integer");
        assertEquals(testType.getStringValue(), "Integer");
        //Test that the Type has been cached
        assertEquals(Type.build("Integer"), testType);
    }

}