package edu.cwru.ams382lmp122.typecheck;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ParserInternalNode implements ParserNode {

    private final List<ParserNode> children;
    private List<Token> cachedList;
    private String cachedString;

    private ParserInternalNode(List<ParserNode> children) {
        this.children = new LinkedList<>(children);
    }

    /**
     * Creates and instantiates a new InternalNode
     * @param children a new list of children node to be created in the InternalNode
     * @return the newly instantiated internalNode made from the given list
     */
    public static ParserInternalNode build(List<ParserNode> children) {
        return new ParserInternalNode(Objects.requireNonNull(children, "Children list cannot be null"));
    }

    @Override
    public List<ParserNode> getChildren() {
        return new LinkedList<>(children);
    }

    @Override
    public boolean isFruitful() {
        return !children.isEmpty();
    }

    @Override
    public boolean isOperator() {
        return false;
    }

    @Override
    public boolean isStartedByOperator() {
        return !children.isEmpty() && children.get(0).isOperator();
    }

    @Override
    public Optional<ParserNode> firstChild() {
        return children.isEmpty() ? Optional.empty() : Optional.of(children.get(0));
    }

    @Override
    public boolean isSingleLeafParent() {
        return (children.size() == 1 && children.get(0) instanceof ParserLeafNode);
    }

    // Use this one
    @Override
    public List<Token> toList() {
        // The value returned by this method should never change, so we will store it in a cache
        // If there is no existing cached value, compute the list from scratch and cache it
        if (cachedList == null) {
            cachedList = new LinkedList<>();

            for (ParserNode child : children) {
                cachedList.addAll(child.toList());
            }
        }

        // Return the cached value
        return new LinkedList<>(cachedList);
    }

    // Use this
    @Override
    public String toString() {
        if (cachedString == null) {
            cachedString = "[" + String.join(", ", children.stream().map(ParserNode::toString).toArray(String[]::new)) + "]";
        }

        return cachedString;
    }

    /**
     * Nested class used to build an InternalNode
     */
    public static class Builder {

        /**
         * List of children within the InternalNode, initialized as empty list
         */
        private List<ParserNode> children = new LinkedList<>();

        /**
         * Adds a new node to the list of children being built
         * @param node A new node to add to the end of list
         * @return true after node is added
         */
        public boolean addChild(ParserNode node) {
            return this.children.add(node);
        }

        /**
         * Simplifies the InternalNode by clearing unneeded children
         * @return new Builder instance containing simplified InternalNode
         */
        public Builder simplify() {
            //Temporary list for simplified changes
            List<ParserNode> newChildren = new LinkedList<ParserNode>(children);
            for (ParserNode child : children) {
                //If the child is not fruitful, remove from the children list
                if (!child.isFruitful()) {
                    newChildren.remove(child);
                }
                else if (child instanceof ParserInternalNode) {
                    simplifyInternalNode((ParserInternalNode) child, newChildren);
                }
            }
            splitSingleInternalNode(newChildren);
            children = newChildren;
            return this;
        }

        /**
         * Builds a new InternalNode instance with the simplified children list
         * @return the newly built instance of InternalNode
         */
        public ParserInternalNode build() {
            return ParserInternalNode.build(simplify().children);
        }

        /**
         * Checks to see if the children list contains a single InternalNode and splits its children if it does
         */
        private void splitSingleInternalNode(List<ParserNode> children) {
            if(children.size() == 1 && children.get(0) instanceof ParserInternalNode) {
                replaceInternalNodeWithChildren((ParserInternalNode) children.get(0), children );
            }
        }

        /**
         * Replaces an InternalNode in a list with its stored children
         * @param node		the InternalNode to be split
         * @param nodeList	given list containing node that is to be modified
         */
        private void replaceInternalNodeWithChildren(ParserInternalNode node, List<ParserNode> nodeList) {
            nodeList.addAll(nodeList.indexOf(node), node.getChildren());
            nodeList.remove(node);
        }

        private void simplifyInternalNode(ParserInternalNode node, List<ParserNode> nodeList) {

            if ((node.isStartedByOperator() && !hasOperatorBefore(nodeList, node))
                    || node.isSingleLeafParent()) {
                replaceInternalNodeWithChildren(node, nodeList);
            }
        }

        /**
         * Checks to see if a given InternalNode child in list is preceded by an operator
         * @param children 	a list of Nodes
         * @param node 	an InternalNode within the given list
         * @return			true if an operator precedes the child, and false otherwise
         */
        private boolean hasOperatorBefore(List<ParserNode> children, ParserInternalNode node) {
            if (children.indexOf(node) == 0) {
                return false;
            }
            else {
                return getPreviousNode(children, node).isOperator();
            }
        }

        /**
         * Returns the Node that comes before a given Node within a list
         * @param children 		the list of Nodes to be used
         * @param currentChild 	one of the Children within the list of Nodes
         * @return 				A Node that precedes the given node
         */
        private ParserNode getPreviousNode(List<ParserNode> children, ParserNode currentChild) {
            return children.get(children.indexOf(currentChild)-1);
        }
    }

}
