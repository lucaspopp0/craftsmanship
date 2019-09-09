package channels;

import java.io.IOException;
import java.io.InputStream;

public class Reader {

	private String receiverAddress, callerAddress, currentWorkingAddress;
	private AddressType currentAddressType;
	private int toCount;
	private Instruction lastInstruction;

	public static ConnectionResult shouldConnect(String transmissionReceiverAddress, String transmissionCallerAddress, InputStream transmission) {
		//assert transmissionReceiverAddress != null : "transmissionReceiverAddress cannot be null";
		//assert transmissionCallerAddress != null : "transmissionCallerAddress cannot be null";
		//assert transmission != null : "transmission cannot be null";

		Reader reader = new Reader();
		Parser parser = new Parser(transmission);

		try {
			Instruction nextInstruction;
			while (!reader.shouldStopParsing() && (nextInstruction = parser.readNextInstruction()) != null) {
				reader.processInstruction(nextInstruction);
				reader.lastInstruction = nextInstruction;
			}
		} catch (IOException exception) {
		    return new ConnectionResult(false, "Error reading from input");
		}

		return reader.shouldConnectGivenAddresses(transmissionReceiverAddress, transmissionCallerAddress);
	}

	private Reader() {}

	private ConnectionResult shouldConnectGivenAddresses(String givenReceiver, String givenCaller) {
		if (toCount > 10) {
			return new ConnectionResult(false, "Unexpectedly long preamble");
		} else {
			updateAddressesIfFinished();

			if (receiverAddress == null || callerAddress == null) {
				return new ConnectionResult(false, "Could not determine both addresses");
			} else {
				return new ConnectionResult(givenReceiver.equals(receiverAddress) && givenCaller.equals(callerAddress), null);
			}
		}
	}

	private boolean shouldStopParsing() {
		return 10 < toCount || (receiverAddress != null && callerAddress != null);
	}

	private void updateAddressesIfFinished() {
		if ((currentAddressType == AddressType.RECEIVER && receiverAddress == null) || (currentAddressType == AddressType.CALLER && callerAddress == null)) {
			storeAndResetCurrentWorkingAddress();
		}
	}

	private void processInstruction(Instruction instruction) {
		updateStateBasedOnCommand(instruction);

		if (instruction.isValid()) {
			if (toCount <= 1) {
				currentWorkingAddress += instruction.getValue();
			}
		}
	}

	private void updateStateBasedOnCommand(Instruction instruction) {
		if ("TO".equals(instruction.getCommand())) {
			if (lastInstruction == null || !"TO".equals(lastInstruction.getCommand())) {
				storeAndResetCurrentWorkingAddress();
				currentAddressType = AddressType.RECEIVER;
				toCount = 1;
			} else {
				toCount++;
			}
		} else {
			toCount = 0;

			if ("THISIS".equals(instruction.getCommand())) {
				storeAndResetCurrentWorkingAddress();
				currentAddressType = AddressType.CALLER;
			}
		}
	}

	private void storeAndResetCurrentWorkingAddress() {
		if (currentAddressType == AddressType.RECEIVER) {
			receiverAddress = currentWorkingAddress;
		} else if (currentAddressType == AddressType.CALLER) {
			callerAddress = currentWorkingAddress;
		}

		currentWorkingAddress = "";
	}
}
