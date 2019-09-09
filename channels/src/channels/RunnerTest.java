package channels;

import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class RunnerTest {


    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    @After
    public void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private ByteArrayInputStream testIn;

    @Test
    public void mainTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        boolean value = false;
        final String input = "123\n45\nTO 1\nTO 1\nREP 7\nREP 3\nTO 1\nREP 2\nREP 3\nTHISIS 4\nREP 5";
        provideInput(input);
        Runner.main(new String[] {});

        String[] lines = outContent.toString().split("\n");

        assertTrue(lines[lines.length - 1].indexOf("true") == 0);
    }
}