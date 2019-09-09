package channels;

import java.util.Optional;

public class ConnectionResult {

    private boolean shouldConnect;
    private Optional<String> errorMessage;

    public ConnectionResult(boolean shouldConnect, String errorMessage) {
        this.shouldConnect = shouldConnect;
        this.errorMessage = Optional.ofNullable(errorMessage);
    }

    public boolean shouldConnect() {
        return shouldConnect;
    }

    public Optional<String> getErrorMessage() {
        return errorMessage;
    }
}
