package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import parser.Connector;
import parser.LeafNode;
import parser.TerminalSymbol;

public final class LeafNodeTest {
	
	@Test (expected = NullPointerException.class)
	public void nullTokenShouldThrowException() {
		LeafNode.build(null);
	}
	
	@Test
	public void toListShouldReturnCorrectly() {
		LeafNode node = LeafNode.build(Connector.build(TerminalSymbol.PLUS));
		assertEquals("toList() must return a single element list", 1, node.toList().size());
		assertSame("The only element in the list returned by toList() should be the node's token", node.getToken(), node.toList().get(0));
	}
	
}
