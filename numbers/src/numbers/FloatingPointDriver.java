package numbers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Optional;

/* Driver to build and run FloatingPointParser on input readers */
public final class FloatingPointDriver {
	
	public static void main(String[] args) {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		FloatingPointDriver driver = new FloatingPointDriver();
		Optional<Double> result = driver.runFloatingPointParser(input);
		printOutput(result);
	}
	
	// Prints out the given Double (or "Invalid Input" if given empty result)
	public final static void printOutput(Optional<Double> result) {
		System.out.println(result.isPresent() ? result.get() : "Invalid Input");
	}
	
	// Retrieves input from the given BufferedReader and parses it to a Double
	public final Optional<Double> runFloatingPointParser(BufferedReader input) {
		FloatingPointParser parser = getFloatingPointParser(input);
		return parser.isValidInput() ? Optional.of(parser.parseDouble()) : Optional.empty();
	}
	
	// Reads from the input reader and builds a parser
	private final FloatingPointParser getFloatingPointParser(BufferedReader input) throws NullPointerException {
		Objects.requireNonNull(input, "No input source provided");
		
		String inputText = null;
		
		try {
			inputText = input.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		if (inputText != null) {
			inputText = inputText.toUpperCase().replaceAll("\\s", "");
		}
		
		return FloatingPointParser.build(inputText);
	}
	
	static class TestHook {
		
		public static FloatingPointParser getFloatingPointParser(BufferedReader input) {
			return (new FloatingPointDriver()).getFloatingPointParser(input);
		}
		
		public static FloatingPointParser getFloatingPointParserWrapped(String input) {
			BufferedReader mockInput = new BufferedReader(new StringReader(input));
			return (new FloatingPointDriver()).getFloatingPointParser(mockInput);
		}
		
	}
	
}
