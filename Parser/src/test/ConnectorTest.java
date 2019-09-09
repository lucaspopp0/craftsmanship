package test;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.Connector;
import parser.TerminalSymbol;

public class ConnectorTest {
	
	@Test (expected = NullPointerException.class)
	public void testBuildNull() {
		Connector.build(null);
	}
	
	@Test
	public void testGetType() {
		Connector typeTest = Connector.build(TerminalSymbol.TIMES);
		assertSame("Connector Type Getter Error", typeTest.getType(), TerminalSymbol.TIMES);
	}
	
	@Test
	public void testBuildInput() {
		Connector connectorIn = Connector.build(TerminalSymbol.DIVIDE);
		assertSame("Connector Type not set properly", connectorIn.getType(), TerminalSymbol.DIVIDE);
	}
	
	@Test
	public void testToString() {
		Connector valTest1 = Connector.build(TerminalSymbol.DIVIDE);
		assertSame("Connector to String not properly Set", valTest1.toString(), "/");
	}
	
}
