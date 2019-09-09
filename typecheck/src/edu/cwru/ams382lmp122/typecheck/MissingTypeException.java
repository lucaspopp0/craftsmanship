package edu.cwru.ams382lmp122.typecheck;

/** Thrown by <code>ExpressionTypeHandler</code> if there is no type defined for the specified variable */
public class MissingTypeException extends Exception {

    /** The variable that is missing a Type */
    private Variable variable;

    /**
     * Constructs the new exception to be thrown for the specified variable
     * @param variable the variable missing a type
     */
    MissingTypeException(Variable variable){
        super("No type found for variable: " + variable.toString());
        this.variable = variable;
    }


}
