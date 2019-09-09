package edu.cwru.ams382lmp122.typecheck;

import java.util.Objects;

/**
 * A Type element within the Type Tree
 */
public class Type implements TypeTreeElement {

    private static Cache<String, Type> cache = new Cache<>();
    private String stringValue;

    /**
     * Constructs this <code>Type</code> instance
     * @param stringValue   A String representing this type
     */
    private Type(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Builds a new <code>Type</code> Instance
     * @param stringValue   A String to represent the new Type
     * @return              A newly built Type instance
     */
    public static Type build(String stringValue) {
        Objects.requireNonNull(stringValue, "The String value should not be null!");

        return cache.get(stringValue, Type::new);
    }

    /**
     * Gets the <code>String</code> representation of this element
     * @return the StringValue
     */
    public String getStringValue(){
        return stringValue;
    }

    /**
     * Determines whether this element is an <code>Operator</code>
     *
     * @return True if the element represents an operator symbol, and false otherwise
     */
    @Override
    public boolean isOperator() {
        return false;
    }
}
