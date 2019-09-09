package numbers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import numbers.DecimalInput.TestHook;

public class InputTest {
	
	// For using decimalHook methods that are not object-specific
	private static final TestHook decimalHook = new DecimalInput("1").new TestHook();
	private static final DecimalInput testDecimal = new DecimalInput("-1234.56");
	
	@Test
	public void not_within_string() {
		assertTrue(decimalHook.charIsNotWithinString('c', "this is a test"));
		assertFalse(decimalHook.charIsNotWithinString('t', "this is a test"));
	}
	
	@Test
	public void getRegexOf() {
		assertEquals("s", decimalHook.getRegexOf('s'));
		assertNotSame("c", decimalHook.getRegexOf('p'));
	}
	
	@Test
	public void toString_pos_neg() {
		assertEquals("-1234.56", testDecimal.toString());
		assertEquals("11.2", (new DecimalInput("11.2")).toString());
	}
	
	@Test
	public void isIntegerTests() {
		assertFalse(testDecimal.isInteger());
		assertTrue(new DecimalInput("146").isInteger());
	}
	
	@Test
	public void isValid_nominal() {
		assertTrue(testDecimal.isValid());
	}
	
	@Test
	public void isValid_invalidChar() {
		assertFalse((new DecimalInput("a567")).isValid());
		assertFalse((new DecimalInput("a41.65")).isValid());
		assertFalse((new DecimalInput("a___123.4")).isValid());
	}
	
	@Test
	public void isValid_invalidDecimal() {
		assertFalse((new DecimalInput("1.23.123")).isValid());
		assertFalse((new DecimalInput("1_2.23.123")).isValid());
	}
	
	@Test
	public void isValid_invalidPadding() {
		assertFalse((new DecimalInput("__1__2_____4__5")).isValid());
	}
	
	/* Example: 1_234 -> valid */
	@Test
	public void test_padding_nominal() {
		assertTrue(decimalHook.hasValidMiddlePadding("1_234"));
	}
	
	/* Example: 1__234 -> valid */
	@Test
	public void test_padding_long_underscore() {
		assertTrue(decimalHook.hasValidMiddlePadding("1__234"));
	}
	
	/* Example: 12_34 -> invalid */
	@Test
	public void test_padding_bad_underscore() {
		assertFalse(decimalHook.hasValidMiddlePadding("12_34"));
	}
	
	/* Example: _1_234 -> invalid */
	@Test
	public void test_padding_leading_underscore() {
		assertFalse(decimalHook.hasValidMiddlePadding("_1_234"));
	}
	
	@Test
	public void hasNoEdgePadding() {
		assertTrue(decimalHook.hasNoEdgePadding("1,256"));
		assertTrue(decimalHook.hasNoEdgePadding("8__________932"));
		assertFalse(decimalHook.hasNoEdgePadding("123_____"));
		assertFalse(decimalHook.hasNoEdgePadding("_____123"));
		assertFalse(decimalHook.hasNoEdgePadding("___123___"));
		
	}
	
	// Test for empty array of chunks
	@Test
	public void hasValidLeadingPadding() {
		assertFalse(decimalHook.paddingIsValidForLeadingChunk(new String[] {}));
	}
	
	@Test
	public void hasValidPadding() {
		DecimalInput.TestHook result = testDecimal.new TestHook();
		DecimalInput.TestHook result_invalid = new DecimalInput("__123.___12_3.2_34_4_4").new TestHook();
		DecimalInput.TestHook result_invalid_padding = new DecimalInput("1__2_3.4_5_6").new TestHook();
		DecimalInput.TestHook invalid_padding_distance = new DecimalInput("1_20").new TestHook();
		assertTrue(result.hasValidPadding());
		assertFalse(result_invalid.hasValidPadding());
		assertFalse(result_invalid_padding.hasValidPadding());
		assertFalse(invalid_padding_distance.hasValidPadding());
	}
	
	@Test (expected = NullPointerException.class)
	public void decimal_input_constructor_null() {
		new DecimalInput(null);
	}
	
	@Test
	public void decimal_input_constructor_size() {
		assertEquals("123", (new DecimalInput("     123     ")).toString());
	}
	
	@Test
	public void isNumberPositive() {
		assertFalse(decimalHook.isNumberPositive(""));
	}
	
	@Test
	public void removeSign() {
		assertEquals("", decimalHook.removeSign(""));
	}
	
	@Test
	public void hasValidDecimalPoint() {
		assertFalse(decimalHook.hasValidDecimalPoint(".1"));
		assertFalse(decimalHook.hasValidDecimalPoint("0."));
	}
	
}
