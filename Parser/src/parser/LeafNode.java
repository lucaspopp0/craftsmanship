package parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Use Arik's
public final class LeafNode implements Node {
	
	private final Token token;

	public static final LeafNode build(Token token) throws NullPointerException {
		Objects.requireNonNull(token, "token cannot be null");
		return new LeafNode(token);
	}
	
	private LeafNode(Token token) {
		this.token = token;
	}
	
	@Override
	public List<Node> getChildren() {
		return null;
	}
	
	@Override
	public boolean isFruitful() {
		return true;
	}
	
	@Override
	public boolean isOperator() {
		return token.isOperator();
	}
	
	@Override
	public boolean isStartedByOperator() {
		return false;
	}
	
	@Override
	public Optional<Node> firstChild() {
		return Optional.empty();
	}
	
	@Override
	public boolean isSingleLeafParent() {
		return false;
	}
	
	@Override
	public List<Token> toList() {
		return Arrays.asList(token);
	}
	
	public Token getToken() {
		return this.token;
	}
	
	@Override
	public String toString() {
		return token.toString();
	}
}
