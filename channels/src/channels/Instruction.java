package channels;

import java.util.HashSet;

public class Instruction {

	public static final Instruction UNREADABLE = new Instruction(null,null);
	private static final HashSet<String> validCommands = new HashSet<>();
	
	static {
		validCommands.add("TO");
		validCommands.add("REP");
		validCommands.add("THISIS");
	}
	
	private String value;
	private String command;
	
	public Instruction(String command, String value) {
		this.value = value;
		this.command = command;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getCommand() {
		return command;
	}
	
	public boolean isValid() {
		return isValidCommand(command) && isValidValue(value);
	}

	public static boolean isValidCommand(String command) {
		return validCommands.contains(command);
	}

	public static boolean isValidValue(String value) {
		return value != null && value.matches("^\\d$");
	}
	
}
