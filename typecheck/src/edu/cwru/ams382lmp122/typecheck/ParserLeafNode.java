package edu.cwru.ams382lmp122.typecheck;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ParserLeafNode implements ParserNode {

	/**
	 * Token that is stored in this leaf node
	 */
	private final Token token;

	/**
	 * Constructs the LeafNode and initializes its token value
	 * @param token
	 */
	private ParserLeafNode(Token token) {
		//Set the given value to the value stored in token
		this.token = token;
	}

	/**
	 * Retrieves the token in this node
	 * @return value stored in token
	 */
	public Token getToken() {
		return this.token;
	}

	/**
	 * Builds a new LeafNode that will be represented with this class
	 * @param token the Token value that will be stored in the new LeafNode
	 * @return the newly constructed LeafNode that contains the given Token value
	 */
	public static ParserLeafNode build(Token token) {
		return new ParserLeafNode(Objects.requireNonNull(token, "Token type cannot be null"));
	}

	/**
	 * Overrides toString method and returns the Token stored in the LeafNode instance to a string representation
	 * @return returns a string representation of the token value
	 */
	@Override
	public String toString() {
		return this.getToken().toString();
	}

	/**
	 * Adds the token in this LeafNode instance to an empty list and returns that list
	 * @return a new list containing the single value stored in token
	 */
	public List<Token> toList(){
		return Arrays.asList(this.getToken());
	}

	/**
	 * Gets the children of the LeafNode
	 * @return null since a LeafNode does not contain node children
	 */
	@Override
	public List<ParserNode> getChildren() {
		return null;
	}

	/**
	 * Retrieves the first child of the node if it has one
	 * @return empty since this is a LeafNode
	 */
	@Override
	public Optional<ParserNode> firstChild() {
		return Optional.empty();
	}

	/**
	 * Determines whether the LeafNode holds a value
	 * @return true since a LeafNode always has a stored Token
	 */
	@Override
	public boolean isFruitful() {
		return true;
	}

	/**
	 * Determines whether a node is a leaf corresponding to an operator
	 * @return true if it the leaf does correspond to an operator and false otherwise
	 */
	@Override
	public boolean isOperator() {
		return this.getToken().isOperator();
	}

	/**
	 * Determines if the first child of the node is started by an operator
	 * @return false because a LeafNode doesn't have any children
	 */
	@Override
	public boolean isStartedByOperator() {
		return false;
	}

	/**
	 * Determines whether the node's only child is a leaf
	 * @return false because a LeafNode doesn't have any children
	 */
	@Override
	public boolean isSingleLeafParent() {
		return false;
	}

}
