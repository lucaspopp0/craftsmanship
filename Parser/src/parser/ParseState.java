package parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

// Use this
final class ParseState {
	
	public final static ParseState FAILURE = new ParseState(false, null, null);
	
	private final boolean success;
	private final Node node;
	private final List<Token> remainder;
	
	private ParseState(boolean success, Node node, List<Token> remainder) {
		this.success = success;
		this.node = node;
		this.remainder = remainder;
	}
	
	public static ParseState build(Node node, List<Token> remainder) throws NullPointerException {
		Objects.requireNonNull(node, "Cannot build a ParseState with null node. For a failed state, please use ParseState.FAILURE.");
		Objects.requireNonNull(remainder, "Cannot build a ParseState with null remainder. Please use an empty list to indicate no remainder, or use ParseState.FAILURE to indicate a failure.");
		
		return new ParseState(true, node, new LinkedList<Token>(remainder));
	}
	
	public boolean hasNoRemainder() {
		return remainder.isEmpty();
	}
	
	public boolean getSuccess() {
		return this.success;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public List<Token> getRemainder() {
		return new LinkedList<Token>(this.remainder);
	}
	
}
