package numbers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ParserTest {
	
	private static final FloatingPointParser.TestHook floatingPointHook = FloatingPointParser.build("").new TestHook();
	private static final FloatingPointParser invalExpo = FloatingPointParser.build("12E-1.2");

	// Tests that all versions of 1234.56 from the assignment evaluate to the same thing
	@Test
	public void testPadding(){
		Double correctValue = new Double(1234.56);

		assertEquals(correctValue, FloatingPointParser.build("00123456E-2").parseDouble());
		assertEquals(correctValue, FloatingPointParser.build("1234.56").parseDouble());
		assertEquals(correctValue, FloatingPointParser.build("1_234.56").parseDouble());
		assertEquals(correctValue, FloatingPointParser.build("1__234.56").parseDouble());
		assertEquals(correctValue, FloatingPointParser.build("1234_560E-3").parseDouble());
		assertEquals(correctValue, FloatingPointParser.build("0_012_345.60E-01").parseDouble());
		assertEquals(correctValue, FloatingPointParser.build("01.23456E+003").parseDouble());
		assertEquals(correctValue, FloatingPointParser.build("12.3456E2").parseDouble());
		assertThat(correctValue, not(equalTo(FloatingPointParser.build("12.3456E3").parseDouble())));
	}

	// Nominal case test of isValidInput
	@Test
	public void isValidInput_nominal(){
		FloatingPointParser test = FloatingPointParser.build("1234.5E-2");
		assertTrue(test.isValidInput());
	}

	// Test for isValidInput where first condition (containsAtLeastOneFloatingChunk) is false
	@Test
	public void isValidInput_branches(){
		FloatingPointParser noFloatChunk = FloatingPointParser.build("1234");
		assertFalse(noFloatChunk.isValidInput());
	}
	
	// Test for isValidInput where second condition (hasValidBase) is false
	@Test
	public void isValidInputInvalidBase() {
		FloatingPointParser invalidBase = FloatingPointParser.build("E-3");
		assertFalse(invalidBase.isValidInput());
	}
	
	// Test for isValidInput where third condition (hasValidIntegerExponent) is false
	@Test
	public void isValidInputInvalidExponent() {
		assertFalse(invalExpo.isValidInput());
	}

	// Parsing invalid double should throw a number format exception, since assertions don't run in ant
	@Test(expected = NumberFormatException.class)
	public void parseDouble_invalid_input(){
		invalExpo.parseDouble();
	}

	// Tests that an invalid exponent should not be valid
	@Test
	public void hasValidIntegerExponent(){
		assertFalse(floatingPointHook.hasValidIntegerExponent("1.2E1_22___34"));
	}

	// Tests for containsAtLeastOneFloatingChunk
	@Test
	public void containsAtLeastOneFloatingChunk(){
		assertTrue(floatingPointHook.containsAtLeaftOneFloatingChunk("123E1"));
		assertTrue(floatingPointHook.containsAtLeaftOneFloatingChunk("12.3E1"));
		assertFalse(floatingPointHook.containsAtLeaftOneFloatingChunk("123"));
		assertTrue(floatingPointHook.containsAtLeaftOneFloatingChunk("12.3"));
	}

	// Building a parser with a null input should be invalid
	@Test
	public void invalidBuild(){
		assertFalse(FloatingPointParser.build(null).isValidInput());
	}
	
}
