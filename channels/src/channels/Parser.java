package channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    private BufferedReader bufferedReader;
    private LinkedList<String> remainingPieces;

    public Parser(InputStream stream) {
        // assert stream != null : "The input stream is null";
        bufferedReader = new BufferedReader(new InputStreamReader(stream));
        remainingPieces = new LinkedList<>();
    }

    public Instruction readNextInstruction() throws IOException {
        // If there is input left over from the last line, use it. Otherwise, parse the next line into pieces
        if (remainingPieces.isEmpty()) {
            String nextLine = bufferedReader.readLine();

            if (nextLine == null) {
                return null;
            } else {
                remainingPieces.addAll(splitIntoListByWhiteSpace(nextLine));
            }
        }

        return parse(remainingPieces);
    }

    private Instruction parse(LinkedList<String> strings){
        // assert strings.size() > 0 : "Passed an empty list of strings to parse";

        String firstPiece = strings.removeFirst();

        if (Instruction.isValidCommand(firstPiece) && !strings.isEmpty()) {
            String secondPiece = strings.removeFirst();

            if (Instruction.isValidValue(secondPiece)) {
                // all pieces are valid
                return new Instruction(firstPiece, secondPiece);
            } else {
                // the first piece is okay but the second piece is bad
                return new Instruction(firstPiece,null);
            }
        } else {
            // Totally invalid instruction
            return Instruction.UNREADABLE;
        }
    }

    private static List<String> splitIntoListByWhiteSpace(String string){
        return Arrays.asList(string.split("\\s+", 0));
    }

    class TestHook{

        Instruction parse(LinkedList<String> strings){
            return Parser.this.parse(strings);
        }

        List<String> splitIntoListByWhiteSpace(String string){
            return Parser.this.splitIntoListByWhiteSpace(string);
        }

    }

}
