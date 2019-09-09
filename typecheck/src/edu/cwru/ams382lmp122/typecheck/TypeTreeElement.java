package edu.cwru.ams382lmp122.typecheck;

/**
 * Interface representing a type of element within the TypeTree
 */
interface TypeTreeElement {

    /**
     * Checks to see if a given Token from a parsed tree is a valid parse tree token
     * @param token     The Token to be evaluated
     * @return          True if the Token is valid, and false otherwise
     */
    static boolean isValidElement(Token token) {
        return (token instanceof Variable) || ((token instanceof Connector) && Operator.correspondsToOperator((token).getType()));
    }

    /**
     * Determines whether this element is an operator
     * @return  True if the element represents an operator symbol, and false otherwise
     */
    boolean isOperator();

}
