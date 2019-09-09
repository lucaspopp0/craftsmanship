package edu.cwru.ams382lmp122.typecheck;

import java.util.List;

/**
 * Marker class for edu.cwru.ams382lmp122.typecheck.ParserNode Hierarchy
 */
interface Node<T extends Node> {

    /**
     * Retrieves the list of <code>Node</code> Children for this Node
     * @return  A list of the corresponding <code>Node</code> Type
     */
    List<T> getChildren();

    /**
     * Determines whether this node represents a single <code>Operator</code>
     * @return True if the node contains an <code>Operator</code>, false otherwise
     */
    boolean isOperator();

}
