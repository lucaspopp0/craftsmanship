package numbers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/* Validation class for simple decimal numbers (an integer with optional fractional chunk) */
/* Global Precondition: number is not null nor does it have leading or trailing spaces 
 */
class DecimalInput {
	
	private static final char DECIMAL = '.';
	private static final char PADDING = '_';
	private static final Set<Character> SIGN_SET;
	private static final Set<Integer> VALID_CHAR_SET;
	
	static {
		// Setup set of valid signs
		Set<Character> signs = new HashSet<>();
		signs.add('-');
		signs.add('+');
		
		SIGN_SET = Collections.unmodifiableSet(signs);
	}
	
	static {
		// Setup set of valid characters in a decimal
		Set<Character> validChars = new HashSet<>();
		
		for (int i = 0; i <= 9; i++) {
			validChars.add((char) (i + '0'));
		}
		
		validChars.add(DECIMAL);
		validChars.add(PADDING);
		
		VALID_CHAR_SET = Collections.unmodifiableSet(validChars.stream().mapToInt(Character::charValue).mapToObj(Integer::valueOf).collect(Collectors.toSet()));
	}
	
	private final boolean isPositive;
	private final String number;
	
	DecimalInput(String number) {
		Objects.requireNonNull(number, "The inputted number is null");
		
		number = number.trim();
		this.isPositive = isNumberPositive(number);
		this.number = removeSign(number);
	}
	
	public String toString() {
		return (isPositive ? "" : "-") + removePadding(number);
	}
	
	boolean isInteger() {
		return charIsNotWithinString(DECIMAL, number);
	}
	
	boolean isValid() {
		return hasValidChars() && hasValidDecimalPoint() && hasValidPadding();
	}
	
	private boolean hasValidChars() {
		return number.chars().allMatch(VALID_CHAR_SET::contains);
	}
	
	/*
	 * A number is considered to have a valid decimal point if none exist, or only one exists that splits the string into two further numbers.
	 */
	private boolean hasValidDecimalPoint() {
		String[] numbers = getAllChunks();
		
		boolean hasEmptyChunk = Arrays.stream(numbers).map((s) -> s.isEmpty()).reduce(false, (result, element) -> result || element);
		
		return !hasEmptyChunk && (0 < numbers.length && numbers.length <= 2);
	}
	
	/*
	 * A number is considered to have valid padding if they only appear in the place of a comma in the leading number.
	 */
	private boolean hasValidPadding() {
		String[] numbers = getAllChunks();
		
		return paddingIsValidForLeadingChunk(numbers) && paddingIsValidIfTrailingChunk(numbers);
	}
	
	private String[] getAllChunks() {
		return number.split("\\.", -1);
	}
	
	private boolean paddingIsValidForLeadingChunk(String[] numbers) {
		return 0 < numbers.length && hasNoEdgePadding(numbers[0]) && hasValidMiddlePadding(numbers[0]);
	}
	
	private boolean paddingIsValidIfTrailingChunk(String[] numbers) {
		return numbers.length != 2 || charIsNotWithinString(PADDING, numbers[1]);
	}
	
	private static boolean hasNoEdgePadding(String leading) {
		return leading.charAt(0) != PADDING && leading.charAt(leading.length() - 1) != PADDING;
	}
	
	private static boolean hasValidMiddlePadding(String leading) {
		final AtomicInteger digitCount = new AtomicInteger();
		return new StringBuilder(leading)
				.reverse()
				.chars()
				.peek(i -> digitCount.incrementAndGet())
				.filter(i -> PADDING == (char) i)
				.noneMatch(i -> digitCount.get() % 3 == 0);
	}
	
	private static String removePadding(String number) {
		return number.replaceAll(char2String(PADDING), "");
	}
	
	private static String removeSign(String number) {
		return number.isEmpty() || !SIGN_SET.contains(number.charAt(0)) ? number : number.substring(1);
	}
	
	private static boolean isNumberPositive(String number) {
		return !number.isEmpty() && !number.startsWith("-");
	}
	
	private static boolean charIsNotWithinString(char c, String str) {
		return str.indexOf(c) < 0;
	}
	
	private static String char2String(char ch) {
		return "" + ch;
	}
	
	class TestHook {
		
		boolean hasValidMiddlePadding(String leading) {
			return DecimalInput.hasValidMiddlePadding(leading);
		}
		
		boolean charIsNotWithinString(char c, String str) {
			return DecimalInput.charIsNotWithinString(c, str);
		}
		
		String getRegexOf(char c) {
			return DecimalInput.char2String(c);
		}
		
		boolean hasNoEdgePadding(String string) {
			return DecimalInput.hasNoEdgePadding(string);
		}
		
		boolean hasValidPadding() {
			return DecimalInput.this.hasValidPadding();
		}
		
		boolean paddingIsValidForLeadingChunk(String[] chunks) {
			return DecimalInput.this.paddingIsValidForLeadingChunk(chunks);
		}
		
		boolean isNumberPositive(String number) {
			return DecimalInput.isNumberPositive(number);
		}
		
		String removeSign(String number) {
			return DecimalInput.removeSign(number);
		}
		
		boolean hasValidDecimalPoint(String number) {
			return new DecimalInput(number).hasValidDecimalPoint();
		}
		
		String toString(String s) {
			return new DecimalInput(s).toString();
		}
		
	}
	
}
