package edu.cwru.ams382lmp122.typecheck;

/** Thrown by <code>ExpressionTypeHandler</code> to indicate that two conflicting type rules have been found. */
public class ConflictingRulesException extends Exception {

    /** One of the conflicting rules */
    private TypeConversionRule rule1;

    /** The other conflicting rule */
    private TypeConversionRule rule2;

    /**
     * Constructs a new ConflictingRulesException with the specified rules
     * @param rule1 one of the conflicting rules
     * @param rule2 the other conflicting rule
     */
    ConflictingRulesException(TypeConversionRule rule1, TypeConversionRule rule2) {
        super("Encountered conflicting rules for expressions following " + rule1.conditionAsString() + ".");
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

}
