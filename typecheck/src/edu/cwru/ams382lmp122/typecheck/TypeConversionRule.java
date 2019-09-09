package edu.cwru.ams382lmp122.typecheck;

import java.util.Objects;

/** A rule describing the resulting type of a single arithmetic operation */
public class TypeConversionRule {

    /** The type on the left side of the operator */
    private Type leftType;

    /** The operator of the expression */
    private Operator operator;

    /** The type on the right side of the operator */
    private Type rightType;

    /** The resulting type of the operation */
    private Type resultingType;

    /**
     * Builds a <code>TypeConversionRule</code> with the given left type, operator, right type, and resulting type
     * @param leftType              the type on the left side of the operator
     * @param operator              the operator in the expression
     * @param rightType             the type on the right side of the operator
     * @param resultingType         what the resulting type of the operation should be
     * @return                      the new <code>TypeConversionRule</code>
     * @throws NullPointerException if <code>leftType</code>, <code>operator</code>, <code>rightType</code>, or <code>resultingType</code> is null
     */
    public static TypeConversionRule build(Type leftType, Operator operator, Type rightType, Type resultingType) throws NullPointerException {
        Objects.requireNonNull(leftType, "leftType cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(rightType, "rightType cannot be null");
        Objects.requireNonNull(resultingType, "resultingType cannot be null");

        return new TypeConversionRule(leftType, operator, rightType, resultingType);
    }

    /**
     * Constructs a new <code>TypeConversionRule</code> with the given left type, operator, right type, and resulting type
     * @param leftType              the type on the left side of the operator
     * @param operator              the operator in the expression
     * @param rightType             the type on the right side of the operator
     * @param resultingType         what the resulting type of the operation should be
     */
    private TypeConversionRule(Type leftType, Operator operator, Type rightType, Type resultingType) {
        this.leftType = leftType;
        this.operator = operator;
        this.rightType = rightType;
        this.resultingType = resultingType;
    }

    /**
     * Returns a boolean indicating whether or not a given left type, operator, and right type satisfy the conditions of the rule
     * @param leftType  the left type to check
     * @param operator  the operator to check
     * @param rightType the right type to check
     * @return          true if the conditions are satisfied, false otherwise
     */
    boolean matches(Type leftType, TypeTreeElement operator, Type rightType) {
        return this.leftType.equals(leftType)
                && this.operator.equals(operator)
                && this.rightType.equals(rightType);
    }

    /**
     * Returns a boolean indicating whether the given type rule has the same left type, operator, and right type as this rule
     * @param rule  the rule to check against
     * @return      true if the rules conflict
     */
    boolean conflictsWith(TypeConversionRule rule) {
        return rule.matches(leftType, operator, rightType);
    }

    /**
     * Returns the string values of the conditions separated by spaces
     * @return the string values of the conditions separated by spaces
     */
    String conditionAsString() {
        return leftType.getStringValue() + " " + operator + " " + rightType.getStringValue();
    }

    /**
     * Returns the rule's left type
     * @return the rule's left type
     */
    Type getLeftType() {
        return leftType;
    }

    /**
     * Returns the rule's operator
     * @return the rule's operator
     */
    Operator getOperator() {
        return operator;
    }

    /**
     * Returns the rule's right type
     * @return the rule's right type
     */
    Type getRightType() {
        return rightType;
    }

    /**
     * Returns the rule's resulting type
     * @return the rule's resulting type
     */
    Type getResultingType() {
        return resultingType;
    }

    /**
     * Returns true if the given object is a <code>TypeConversionRule</code> and that rule is equivalent to this one, and false otherwise
     * @param o the object to check against
     * @return  a boolean indicating whether this <code>TypeConversionRule</code> is equivalent to the given object
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof TypeConversionRule) {
            TypeConversionRule otherRule = (TypeConversionRule)o;

            return otherRule.leftType.equals(leftType) && otherRule.operator.equals(operator) && otherRule.rightType.equals(rightType) && otherRule.resultingType.equals(resultingType);
        }

        return false;
    }

}
