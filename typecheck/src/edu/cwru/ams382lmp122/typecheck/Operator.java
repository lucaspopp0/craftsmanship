package edu.cwru.ams382lmp122.typecheck;

/**
 * An Operator element within the Type Tree
 */
public enum Operator implements TypeTreeElement {

    DIVIDE("/"), //Division
    MINUS("-"), //Subtraction
    PLUS("+"), //Addition
    TIMES("*") //Multiplication
    ;

    //The string representation of this Operator
    private final String representation;

    /**
     * Constructs a new operator with the given representation
     * @param representation   A String that will represent this Operator value
     */
    Operator(String representation) {
        this.representation = representation;
    }

    /**
     * Retrieves the String representation of this Operator
     * @return the representation of this Operator
     */
    @Override
    public String toString() {
        return representation;
    }

    static boolean correspondsToOperator(TerminalSymbol terminalSymbol) {
        for (Operator operator : values()) {
            if (operator.toString().equals(terminalSymbol.getRepresentation())) {
                return true;
            }
        }

        return false;
    }

    static Operator fromTerminalSymbol(TerminalSymbol terminalSymbol) {
        for (Operator operator : values()) {
            if (operator.toString().equals(terminalSymbol.getRepresentation())) {
                return operator;
            }
        }

        return null;
    }

    /**
     * Determines whether this element is an operator
     *
     * @return True if the element represents an operator symbol, and false otherwise
     */
    @Override
    public boolean isOperator() {
        return true;
    }}
