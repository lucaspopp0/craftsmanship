package numbers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

public class DriverTest {
	
	// Tests the main method with a valid input
	@Test
	public void testMainWithValidInput() {
		InputStream systemIn = System.in;
		PrintStream systemOut = System.out;
		ByteArrayInputStream tempIn = new ByteArrayInputStream("123e3".getBytes());
		ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
		
		System.setIn(tempIn);
		System.setOut(new PrintStream(tempOut));
		
		FloatingPointDriver.main(new String[] {});
		
		System.setIn(systemIn);
		System.setOut(systemOut);
		
		String output = new String(tempOut.toByteArray());
		
		assertEquals("123000.0", output.trim());
	}
	
	// Tests the main method with an invalid input
	@Test
	public void testMainWithInvalidInput() {
		InputStream systemIn = System.in;
		PrintStream systemOut = System.out;
		ByteArrayInputStream tempIn = new ByteArrayInputStream("".getBytes());
		ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
		
		System.setIn(tempIn);
		System.setOut(new PrintStream(tempOut));
		
		FloatingPointDriver.main(new String[] {});
		
		System.setIn(systemIn);
		System.setOut(systemOut);
		
		String output = new String(tempOut.toByteArray());
		
		assertEquals("Invalid Input", output.trim());
	}
	
	/** getFloatingPointParser tests **/
	
	// Test with null input source
	@Test (expected = NullPointerException.class)
	public void parserShouldFailWithNullInput() {
		FloatingPointDriver.TestHook.getFloatingPointParser(null);
	}
	
	// Test to avoid reintroduction of earlier bug, parser was not using the formatted string
	@Test
	public void parserShouldMakeInputUpperCase() {
		FloatingPointParser parser = FloatingPointDriver.TestHook.getFloatingPointParserWrapped("123e3");
		assertTrue(parser.isValidInput());
	}
	
}
