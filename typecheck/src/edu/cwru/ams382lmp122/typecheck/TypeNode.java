package edu.cwru.ams382lmp122.typecheck;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * An interface that represents a <code>Type</code> within a Tree <code>ParserNode</code>
 */
interface TypeNode extends Node<TypeNode> {

    /**
     * Retrieves the children of this <code>TypeNode</code>
     * @return  A list of TypeNodes
     */
    @Override List<TypeNode> getChildren();

    /** Retrieves the single element stored within this node
     * @return  An instance of <code>TypeTreeElement</code>
     */
    TypeTreeElement getElement();

    /**
     * Evaluates the node's <code>Type</code> using the given conversion rules
     * @param rules                 a set of rules to be used for the evaluation
     * @return                      an optional containing the node's evaluated type, or empty if its type could not be determined
     * @throws MissingRuleException if an operation is encountered that is not covered by the rules
     */
    Optional<Type> evaluatedTypeForRules(Set<TypeConversionRule> rules) throws MissingRuleException;

}
