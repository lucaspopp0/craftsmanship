package parser;

import java.util.List;
import java.util.Optional;

// Use Arik's
public interface Node {
	
	public List<Node> getChildren();
	
	public boolean isFruitful();
	
	public boolean isOperator();
	
	public boolean isStartedByOperator();
	
	public Optional<Node> firstChild();
	
	public boolean isSingleLeafParent();
	
	public List<Token> toList();
	
}
