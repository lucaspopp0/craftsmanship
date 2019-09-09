package channels;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ReaderTest {
    String receiverAddress;
    String callerAddress;
    @Before
    public void setUp()  {
        receiverAddress = "123";
        callerAddress = "45";
    }

    @Test
    public void basicExampleShouldConnect() {
        InputStream transmission = new ByteArrayInputStream("TO 1\nTO 1\nTO 1\nTO 1\nTO 1\nTO 1\nREP 2\nREP 3\nTO 1\nREP 2\nREP 3\nTHISIS 4\nREP 5".getBytes());

        assertTrue(Reader.shouldConnect(receiverAddress, callerAddress, transmission).shouldConnect());
    }

    @Test
    public void exampleThatShouldBeLogged() {
        InputStream transmission = new ByteArrayInputStream("TOqweqwe1\nTOqweqwe 1\nTqweqweO 1\nqweqweqweTO 1\nTO 1\nTO 1\nREP 2\nREP 3\nTO 1\nREP 2\nREP 3\nTHISIS 4\nREP 5".getBytes());
        assertTrue(Reader.shouldConnect(receiverAddress, callerAddress, transmission).shouldConnect());
    }

    @Test
    public void tooManyToos(){
        InputStream transmission = new ByteArrayInputStream("TO 1 TO 1 TO 1 \nTO 1 TO 1 TO 1 \nTO 1 TO 1 TO 1 \nTO 1 TO 1 TO 1 TO 1 \nTO 1 TO 1 TO 1 \nTO 1\nREP 2\nREP 3\nTO 1\nREP 2\nREP 3\nTHISIS 4\nREP 5".getBytes());
        assertFalse(Reader.shouldConnect(receiverAddress, callerAddress, transmission).shouldConnect());

    }

    @Test
    public void updateAddressesAtEnd() {
        InputStream callerAtEnd = new ByteArrayInputStream("TO 1\nREP 2\nREP 3\nTHISIS 4\nREP 5".getBytes());
        InputStream receiverAtEnd = new ByteArrayInputStream("THISIS 4\nREP 5\nTO 1\nREP 2\nREP 3".getBytes());

        assertTrue(Reader.shouldConnect(receiverAddress, callerAddress, callerAtEnd).shouldConnect());
        assertTrue(Reader.shouldConnect(receiverAddress, callerAddress, receiverAtEnd).shouldConnect());

        InputStream noReceiverAtEnd = new ByteArrayInputStream("THISIS 4\nREP 5".getBytes());
        InputStream noCallerAtEnd = new ByteArrayInputStream("TO 1\nREP 2\nREP 3".getBytes());

        assertFalse(Reader.shouldConnect(receiverAddress, callerAddress, noReceiverAtEnd).shouldConnect());
        assertFalse(Reader.shouldConnect(receiverAddress, callerAddress, noCallerAtEnd).shouldConnect());
    }

    @Test
    public void shouldConnectBranch(){
        InputStream no_sender = new ByteArrayInputStream("TO 1\nREP 2\nREP 3".getBytes());
        ConnectionResult result_sender = Reader.shouldConnect(receiverAddress,callerAddress,no_sender);
        assertFalse(result_sender.shouldConnect());
        assertNotNull(result_sender.getErrorMessage());

        InputStream no_receiver = new ByteArrayInputStream("THISIS 4\nREP 5".getBytes());
        ConnectionResult result_receiver = Reader.shouldConnect(receiverAddress,callerAddress,no_receiver);
        assertFalse(result_receiver.shouldConnect());

        InputStream wrong_sender = new ByteArrayInputStream("TO 1\nREP 2\nREP 3\nTHISIS 4\nREP 1".getBytes());
        ConnectionResult result_wrong_address = Reader.shouldConnect(receiverAddress,callerAddress,wrong_sender);
        assertFalse(result_wrong_address.shouldConnect());

        InputStream wrong_receiver = new ByteArrayInputStream("TO 1\nREP 2\nREP 7\nTHISIS 4\nREP 5".getBytes());
        ConnectionResult result_wrong_receiver = Reader.shouldConnect(receiverAddress,callerAddress,wrong_receiver);
        assertFalse(result_wrong_receiver.shouldConnect());
        assertNotNull(result_wrong_receiver.getErrorMessage());
    }

    @Test
    public void shouldStopParsing(){
        InputStream null_address_toos = new ByteArrayInputStream(("TO 1\nTO 1\nTO 1\nTO 1\nTO 1\nTO 1\nTO 1\nTO 1\nTO 1\nTO " +
                "1\nTO 1\nTO 1\nTO 1\nTO 1\nTO 1\nTO 1").getBytes());
        ConnectionResult result_null_address_toos = Reader.shouldConnect(receiverAddress,callerAddress,null_address_toos);
        assertFalse(result_null_address_toos.shouldConnect());

        InputStream null_address = new ByteArrayInputStream(("").getBytes());
        ConnectionResult result_null = Reader.shouldConnect(receiverAddress,callerAddress,null_address);
        assertFalse(result_null.shouldConnect());

        InputStream one_reciever = new ByteArrayInputStream(("TO 1\n REP 2\nREP 3").getBytes());
        ConnectionResult result_receiver= Reader.shouldConnect(receiverAddress,callerAddress,one_reciever);
        assertFalse(result_receiver.shouldConnect());

        InputStream one_sender = new ByteArrayInputStream(("THISIS 4\nREP 5").getBytes());
        ConnectionResult result_sender= Reader.shouldConnect(receiverAddress,callerAddress,one_sender);
        assertFalse(result_sender.shouldConnect());
    }

}
