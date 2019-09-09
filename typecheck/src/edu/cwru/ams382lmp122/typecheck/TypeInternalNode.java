package edu.cwru.ams382lmp122.typecheck;

import java.util.*;

/**
 * Represents an Internal Node within the Type Tree
 */
class TypeInternalNode implements TypeNode {

    /**
     * The list of element children stored within this node
     */
    private List<TypeNode> children;

    /**
     * Constructs a new instance an internal <code>Node</code> with a given list of children
     *
     * @param children  A list of <code>TypeNode</code> that will be contained in the new InternalNode
     */
    private TypeInternalNode(List<TypeNode> children) {
        this.children = children;
    }

    /**
     * Finds a rule that matches a given expression set within a given set of rules
     *
     * @param leftType              The Type that is on the left side of the expression
     * @param operator              The connecting operator for this expression
     * @param rightType             The Type that is one the right side of the expression
     * @param rules                 The set of rules used for evaluation
     * @return                      A TypeConversionRule that matches the given expression
     * @throws MissingRuleException When the list of rules do not have a conversion for the given expression
     */
    private TypeConversionRule matchingRule(Type leftType, TypeTreeElement operator, Type rightType, Set<TypeConversionRule> rules) throws MissingRuleException {
        for (TypeConversionRule rule : rules){
            if (rule.matches(leftType, operator, rightType)){
                return rule;
            }
        }
        throw new MissingRuleException(leftType, operator, rightType);
    }

    @Override
    public Optional<Type> evaluatedTypeForRules(Set<TypeConversionRule> rules) throws MissingRuleException {
        Type leftType = null;
        TypeTreeElement operator = null;
        Type rightType;

        //Run through each node in the children list and evaluate connected expressions
        for (TypeNode node : children){
            //Store a connecting operator
            if (node.isOperator()) {
                operator = node.getElement();
            }
            //Store the second available type as the Right Type
            else if (leftType == null) {
                leftType = node.evaluatedTypeForRules(rules).get();
            } else {
                rightType = node.evaluatedTypeForRules(rules).get();
                TypeConversionRule matchingRule = matchingRule(leftType, operator, rightType, rules);
                leftType = matchingRule.getResultingType();
                operator = null;
            }
        }

        return Optional.ofNullable(leftType);
    }

    /**
     * Builds a new instance of an <code>TypeInternalNode</code>
     *
     * @param children  A list of <code>TypeNode</code> children that the <code>InternalNode</code> will contain
     * @return          Returns a newly constructed <code>TypeInternalNode</code> instance
     */
    static TypeInternalNode build(List<TypeNode> children){
        return new TypeInternalNode(Objects.requireNonNull(children, "The children list cannot be null!"));
    }

    /**
     * Retrieves the children of this <code>TypeNode</code>
     *
     * @return A list of <code>TypeNode</code>
     */
    @Override
    public List<TypeNode> getChildren() {
        return children;
    }

    /**
     * Retrieves the single element stored within this node
     *
     * @return An instance of <code>TypeTreeElement</code>
     */
    @Override
    public TypeTreeElement getElement() {
        return null;
    }

    /**
     * Determines whether this <code>Node</code> represents a single <code>Operator</code>
     *
     * @return False since this is an internal <code>Node</code>
     */
    @Override
    public boolean isOperator() {
        return false;
    }

}
