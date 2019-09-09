package edu.cwru.ams382lmp122.typecheck;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

final class ParseState {
	
	final static ParseState FAILURE = new ParseState(false, null, null);
	
	private final boolean success;
	private final ParserNode node;
	private final List<Token> remainder;
	
	private ParseState(boolean success, ParserNode node, List<Token> remainder) {
		this.success = success;
		this.node = node;
		this.remainder = remainder;
	}
	
	static ParseState build(ParserNode node, List<Token> remainder) throws NullPointerException {
		Objects.requireNonNull(node, "Cannot build a ParseState with null node. For a failed state, please use ParseState.FAILURE.");
		Objects.requireNonNull(remainder, "Cannot build a ParseState with null remainder. Please use an empty list to indicate no remainder, or use ParseState.FAILURE to indicate a failure.");
		
		return new ParseState(true, node, new LinkedList<Token>(remainder));
	}
	
	boolean hasNoRemainder() {
		return remainder.isEmpty();
	}
	
	boolean getSuccess() {
		return this.success;
	}
	
	ParserNode getNode() {
		return this.node;
	}
	
	List<Token> getRemainder() {
		return new LinkedList<Token>(this.remainder);
	}
	
}
