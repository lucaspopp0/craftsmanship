package edu.cwru.ams382lmp122.typecheck;

/** Thrown by <code>ExpressionTypeHandler</code> when there is no <code>TypeConversionRule</code> defined for an expression */
public class MissingRuleException extends Exception {

    /** The left type of the expression */
    private Type leftType;

    /** The connecting operator of the expression */
    private TypeTreeElement operator;

    /** The right type of the expression */
    private Type rightType;

    /**
     * Constructs a new <code>MissingRuleException</code> with the given missing conditions
     * @param leftType  the left type of the missing rule
     * @param operator  the operator of the missing rule
     * @param rightType the right type of the missing rule
     */
    MissingRuleException(Type leftType, TypeTreeElement operator, Type rightType){
        super("Missing the conversion rule for " + leftType.getStringValue() + operator.toString() + rightType.toString());
        this.rightType = rightType;
        this.operator = operator;
        this.leftType = leftType;
    }
}
