package test;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.TerminalSymbol;
import parser.Variable;

public class VariableTest {
	
	@Test (expected = NullPointerException.class)
	public void testNullBuild() {
		Variable.build(null);
	}
	
	@Test
	public void testGetRep() {
		Variable testRep = Variable.build("*");
		assertSame("Problem with Variable set or retrieval", testRep.getRepresentation(), "*");
	}
	
	@Test
	public void testGetType() {
		Variable testType = Variable.build("/");
		assertSame("Problem with Type retrieval", testType.getType(), TerminalSymbol.VARIABLE);
	}
	
	@Test
	public void testToString() {
		Variable stringTest = Variable.build("-");
		assertSame("problem with String representation", stringTest.toString(), "-");
	}
	
}
