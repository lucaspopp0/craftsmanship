package edu.cwru.ams382lmp122.typecheck;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a Leaf Node within the Type Tree
 */
class TypeLeafNode implements TypeNode {

    /**
     * The element stored within this node
     */
    private TypeTreeElement element;

    /**
     * Constructs a new <code>TypeLeafNode</code> with a given element
     *
     * @param element   A type of TypeTreeElement
     */
    private TypeLeafNode(TypeTreeElement element) {
        this.element = element;
    }

    /**
     * Builds a new instance of a <code>TypeLeafNode</code> with a given element
     *
     * @param element   A type of TypeTreeElement
     * @return          A newly constructed instance of a TypeLeafNode
     */
    public static TypeLeafNode build(TypeTreeElement element){
        return new TypeLeafNode(Objects.requireNonNull(element, "The given element cannot be null"));
    }

    /**
     * Determines whether the element in this node is an <code>Operator</code>
     *
     * @return  true if the value for element is an instance of <code>Operator</code>, false otherwise
     */
    public boolean isOperator(){
        return element instanceof Operator;
    }

    /**
     * Retrieves the element stored within this node
     *
     * @return  An instance of <code>TypeTreeElement</code>
     */
    public TypeTreeElement getElement() {
        return element;
    }

    /**
     * Retrieves the children of this <code>TypeNode</code>
     *
     * @return A list of <code>TypeNode</code>
     */
    @Override
    public List<TypeNode> getChildren() { return null; }

    @Override
    public Optional<Type> evaluatedTypeForRules(Set<TypeConversionRule> rules) throws MissingRuleException {
        return element instanceof Type ? Optional.of((Type)element) : Optional.empty();
    }

}
