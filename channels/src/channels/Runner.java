package channels;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String receiverAddress, callerAddress;

        System.out.println("Inputs:");

        try {
            receiverAddress = scanner.nextLine();
            callerAddress = scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("Missing either receiver or caller address");
            return;
        } catch (IllegalStateException e) {
            System.out.println("Missing either receiver or caller address");
            return;
        }

        ConnectionResult result = Reader.shouldConnect(receiverAddress, callerAddress, System.in);

        System.out.println("\nOutput:");

        if (result.getErrorMessage().isPresent()) {
            System.out.println(result.shouldConnect() + " " + result.getErrorMessage().get());
        } else {
            System.out.println(result.shouldConnect());
        }
    }

}
