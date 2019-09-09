package test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import parser.Connector;
import parser.InternalNode;
import parser.LeafNode;
import parser.Node;
import parser.TerminalSymbol;
import parser.Variable;

public class InternalNodeBuilderTest {
	
	private InternalNode.Builder builder;
	
	private InternalNode newChildlessNode() {
		return InternalNode.build(new LinkedList<Node>());
	}
	
	@Before
	public void setup() {
		builder = new InternalNode.Builder();
	}
	
	@Test
	public void shouldRemoveChildlessNodes() {
		builder.addChild(LeafNode.build(Variable.build("a")));
		builder.addChild(newChildlessNode());
		builder.addChild(LeafNode.build(Connector.build(TerminalSymbol.PLUS)));
		builder.addChild(newChildlessNode());
		builder.addChild(LeafNode.build(Variable.build("b")));
		builder.addChild(newChildlessNode());
		
		InternalNode simplified = builder.build();
		assertEquals("[a, +, b]", simplified.toString());
	}
	
	@Test
	public void shouldReplaceSingleInternalNodeWithChildren() {
		List<Node> children = new LinkedList<Node>();
		children.add(LeafNode.build(Variable.build("a")));
		children.add(LeafNode.build(Connector.build(TerminalSymbol.PLUS)));
		children.add(LeafNode.build(Variable.build("b")));
		
		builder.addChild(InternalNode.build(children));
		
		InternalNode simplified = builder.build();
		assertEquals("[a, +, b]", simplified.toString());
	}
	
	@Test
	public void shouldReplaceSingleLeafParents() {
		List<Node> rootChildren = new LinkedList<Node>();
		rootChildren.add(LeafNode.build(Variable.build("a")));
		rootChildren.add(LeafNode.build(Connector.build(TerminalSymbol.PLUS)));
		
		List<Node> grandChildren = new LinkedList<Node>();
		grandChildren.add(LeafNode.build(Variable.build("b")));
		
		rootChildren.add(InternalNode.build(grandChildren));
		
		builder.addChild(InternalNode.build(rootChildren));
		
		InternalNode simplified = builder.build();
		assertEquals("[a, +, b]", simplified.toString());
	}
	
	@Test
	public void shouldCorrectlyReplaceNodesThatStartWithOperators() {
		List<Node> rootChildren = new LinkedList<Node>();
		rootChildren.add(LeafNode.build(Variable.build("a")));
		
		List<Node> grandChildren = new LinkedList<Node>();
		grandChildren.add(LeafNode.build(Connector.build(TerminalSymbol.PLUS)));
		grandChildren.add(LeafNode.build(Variable.build("b")));
		
		rootChildren.add(InternalNode.build(grandChildren));
		
		builder.addChild(InternalNode.build(rootChildren));
		
		InternalNode simplified = builder.build();
		assertEquals("[a, +, b]", simplified.toString());
	}
	
}
