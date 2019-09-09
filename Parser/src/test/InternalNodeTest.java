package test;

import static org.junit.Assert.assertSame;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import parser.Connector;
import parser.InternalNode;
import parser.LeafNode;
import parser.Node;
import parser.TerminalSymbol;
import parser.Token;

public final class InternalNodeTest {
	
	@Test (expected = NullPointerException.class)
	public void nullChildrenShouldThrowException() {
		InternalNode.build(null);
	}
	
	@Test
	public void toListConcatenatesChildren() {
		List<Node> children = new LinkedList<>();
		children.add(LeafNode.build(Connector.build(TerminalSymbol.PLUS)));
		children.add(LeafNode.build(Connector.build(TerminalSymbol.MINUS)));
		children.add(LeafNode.build(Connector.build(TerminalSymbol.TIMES)));
		children.add(LeafNode.build(Connector.build(TerminalSymbol.DIVIDE)));
		
		InternalNode node = InternalNode.build(children);
		List<Token> returnedList = node.toList();
		
		for (int i = 0; i < returnedList.size(); i++) {
			assertSame("List returned by toList() should be a concatination of child lists", returnedList.get(i), ((LeafNode) node.getChildren().get(i)).getToken());
		}
	}
	
}
