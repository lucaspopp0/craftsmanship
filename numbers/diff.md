## InputTest.java, DriverTest.java, ParserTest.java

* Added additional tests to `InputTest`
* Created test files for `FloatingPointDriver` and `FloatingPointParser`

## FloatingPointDriver

* Added imports for

``` java
java.io.StringReader
java.util.Objects
java.io.IOException
```

* Removed unnecessary `StringBuilder` implementation to reduce complexity

```java
private final FloatingPointParser getFloatingPointParser(BufferedReader input) {
FloatingPointParser parser = null;
if (input != null) {
    ...  			
}
if (parser == null) {
	return FloatingPointParser.build("input that is really bad");
} else {
	return parser;
  ```

* Simplified to

```java
private final FloatingPointParser getFloatingPointParser(BufferedReader input) throws NullPointerException {
	Objects.requireNonNull(input, "No input source provided");

	String inputText = null;

  ...

  if (inputText != null) {
		inputText = inputText.toUpperCase().replaceAll("\\s", "");
	}

	return FloatingPointParser.build(inputText);
}
```



* Added `try(){} catch(){}` block for `IOException`

```java
try {
			inputText = input.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
```



* Added testhook

## FloatingPointParser.java

* Added test hook
* Capitalized `FloatingPointParser.EXPONENTIAL`

```java
// Original
private static final char EXPONENTIAL = 'e';

// Changed to
private static final char EXPONENTIAL = 'E';
```

* Corrected default exponent from `1` to `0`

```java
// Original
private static final DecimalInput DEFAULT_EXPONENT = new DecimalInput("1");

// Changed to
private static final DecimalInput DEFAULT_EXPONENT = new DecimalInput("0");
```

## DecimalInput.java

* Added `+` as a valid sign
* Added methods to test hook
* Renamed `isNotWithinString` to `charIsNotWithinString`

```java
// Original
Set<Character> signs = new HashSet<>();
signs.add('-');
SIGN_SET = Collections.unmodifiableSet(signs);

// Changed to
Set<Character> signs = new HashSet<>();
signs.add('-');
signs.add('+');

SIGN_SET = Collections.unmodifiableSet(signs);
```
* Removed repeated code defining `VALID_CHAR_SET`

```java
// Original
validChars.add('0');
validChars.add('1');
...
validChars.add('8');
validChars.add('9');
validChars.add(DECIMAL);
validChars.add(PADDING);

// Changed to
for (int i = 0; i <= 9; i++) {
	validChars.add((char) (i + '0'));
}

validChars.add(DECIMAL);
validChars.add(PADDING);
```

* Used `Objects.requireNonNull` instead of assertions in constructor, and automatically trim input instead of failing

```java
// Original
DecimalInput (String number) {
	assert number != null : "Number given should not be null.";
	assert number.trim().equals(number) : "Number given should not have leading or trailing whitespace: \""+number+"\"";
	this.isPositive = isNumberPositive(number);
	this.number = removeSign(number);
}

// Changed to
DecimalInput(String number) {
	Objects.requireNonNull(number, "The inputted number is null");

	number = number.trim();
	this.isPositive = isNumberPositive(number);
	this.number = removeSign(number);
}
```

* Removed unnecessary plus sign in `toString()`

```java
// Original
return (isPositive ? "+" : "-")+removePadding(number);

// Changed to
return (isPositive ? "" : "-") + removePadding(number);
```

* Use a `Stream` reduction to check if decimal point is valid, to avoid complexity > 4 and `ArrayIndexOutOfBoundsException`

```java
// Original
String[] numbers = getAllChunks();
return numbers.length <= 2 && !numbers[0].isEmpty() && !numbers[1].isEmpty();

// Changed to
String[] numbers = getAllChunks();
boolean hasEmptyChunk = Arrays.stream(numbers).map((s) -> s.isEmpty()).reduce(false, (result, element) -> result || element);
return !hasEmptyChunk && (0 < numbers.length && numbers.length <= 2);
```

* Separated conditions in hasValidPadding into their own methods for readability, and added conditions validating the number of chunks

```java
// Original
return (numbers.length == 2 ? isNotWithinString(PADDING,numbers[1]) : false) && hasValidLeadingPadding(numbers[0]);

// Changed to
return paddingIsValidForLeadingChunk(numbers) && paddingIsValidIfTrailingChunk(numbers);
```

* Removed redundant substring end in `removeSign`

```java
// Original
number.substring(1, number.length())

// Changed to
number.substring(1)
```
* Fixed error where `isNumberPositive` only treated explicitly `+` numbers as positive

```java
// Original
private static boolean isNumberPositive(String number) {
	return !number.isEmpty() && number.charAt(0) == '+';
}

// Changed to
private static boolean isNumberPositive(String number) {
	return !number.isEmpty() && !number.startsWith("-");
}
```
