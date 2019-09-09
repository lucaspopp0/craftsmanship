package parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class InternalNode implements Node {
	
	private final List<Node> children;
	private List<Token> cachedList;
	private String cachedString;

	// Use this
	private InternalNode(List<Node> children) {
		this.children = new LinkedList<>(children);
	}

	// Use Arik's
	public static final InternalNode build(List<Node> children) throws NullPointerException {
		Objects.requireNonNull(children, "children cannot be null");
		return new InternalNode(children);
	}
	
	@Override
	public List<Node> getChildren() {
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
	public Optional<Node> firstChild() {
		return children.isEmpty() ? Optional.empty() : Optional.of(children.get(0));
	}
	
	@Override
	public boolean isSingleLeafParent() {
		return (children.size() == 1 && children.get(0) instanceof LeafNode);
	}

	// Use this one
	@Override
	public List<Token> toList() {
		// The value returned by this method should never change, so we will store it in a cache
		// If there is no existing cached value, compute the list from scratch and cache it
		if (cachedList == null) {
			cachedList = new LinkedList<>();
			
			for (Node child : children) {
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
			cachedString = "[" + String.join(", ", children.stream().map(Node::toString).toArray(String[]::new)) + "]";
		}
		
		return new String(cachedString);
	}

	/*
	TODO:
	- No need to make a new builder
	- Bad logic flattening operators. (-f*g) gives [-,f,*,g] when it should be [[-,f],*,g]
	 */

	// Use Arik's but fix the LinkedList stuff and fix bad flattening if there's time
	public static class Builder {
		
		private List<Node> children = new LinkedList<>();
		private boolean lastChildWasOperator = false;
		
		public boolean addChild(Node node) {
			lastChildWasOperator = node.isOperator();
			
			return children.add(node);
		}
		
		public Builder simplify() {
			Builder updated = new Builder();
			
			// Remove all unfruitful children
			List<Node> fruitfulChildren = this.fruitfulChildren();
			
			// If the only remaining child is a single InternalNode, replace it with its children
			if (fruitfulChildren.size() == 1 && fruitfulChildren.get(0) instanceof InternalNode) {
				fruitfulChildren = fruitfulChildren.get(0).getChildren();
			}
			
			for (Node child : fruitfulChildren) {
				updated.addSimplifiedChild(child);
			}
			
			return updated;
		}
		
		// Returns a copy of the builder's children, excluding internal nodes with no children
		private List<Node> fruitfulChildren() {
			List<Node> stripped = new LinkedList<>(this.children);
			stripped.removeIf(node -> (!node.isFruitful()));
			return stripped;
		}
		
		// Either adds the child, or replaces it with its children
		private void addSimplifiedChild(Node child) {
			if (shouldReplaceNodeWithChildren(child)) {
				for (Node grandchild : child.getChildren()) {
					addChild(grandchild);
				}
			} else {
				addChild(child);
			}
		}
		
		private boolean shouldReplaceNodeWithChildren(Node node) {
			return node.isSingleLeafParent() || (node.isStartedByOperator() && !this.lastChildWasOperator);
		}
		
		public InternalNode build() {
			return InternalNode.build(this.simplify().children);
		}
		
	}
	
}
