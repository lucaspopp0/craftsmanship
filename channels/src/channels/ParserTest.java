package channels;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {
    private Parser.TestHook hook;
    private Parser parser;

    @Before
    public void setUp()  {
        InputStream is = new ByteArrayInputStream( "TO 1 \nREP 2 \nREP 3".getBytes() );
        parser = new Parser(is);
        hook = parser.new TestHook();
    }

    @Test
    public void splitIntoListByWhiteSpaceTest() {
        LinkedList<String> result =  new LinkedList<>(hook.splitIntoListByWhiteSpace("TO 1 REP 2 REP 3"));
        assertEquals("TO", result.getFirst());
        assertEquals("1", result.get(1));
    }

    @Test
    public void parseTest() {
        LinkedList<String> to_parse =  new LinkedList<>(hook.splitIntoListByWhiteSpace("TO 1 REP 2 REP 3"));
        System.out.println(to_parse.toString());
        Instruction result = hook.parse(to_parse);
        assertEquals("TO",result.getCommand());
        assertEquals("1",result.getValue());
        System.out.println(to_parse.toString());

        Instruction result1 = hook.parse(to_parse);
        assertEquals("REP",result1.getCommand());
        assertEquals("2",result1.getValue());
        System.out.println(to_parse.toString());

        Instruction result2 = hook.parse(to_parse);
        assertEquals("REP",result2.getCommand());
        assertEquals("3",result2.getValue());
        System.out.println(to_parse.toString());

        LinkedList<String> bad_second_statement =  new LinkedList<>(hook.splitIntoListByWhiteSpace("TO NEVER"));
        Instruction result_bad_second = hook.parse(bad_second_statement);
        assertEquals("TO",result_bad_second.getCommand());
        assertEquals(null,result_bad_second.getValue());

        LinkedList<String> empty_statement =  new LinkedList<>(hook.splitIntoListByWhiteSpace(""));
        Instruction result_empty = hook.parse(empty_statement);
        assertNull(result_empty.getCommand());

        LinkedList<String> invalid_first =  new LinkedList<>(hook.splitIntoListByWhiteSpace("NAH 1"));
        Instruction result_invalid_first = hook.parse(invalid_first);
        assertNull(result_invalid_first.getCommand());

        LinkedList<String> valid_empty =  new LinkedList<>(hook.splitIntoListByWhiteSpace("TO"));
        Instruction result_valid_empty = hook.parse(valid_empty);
        assertNull(result_valid_empty.getCommand());
        assertNull(result_valid_empty.getValue());
    }



    @Test
    public void readNextInstruction() throws  Exception{
        InputStream is_bad_data = new ByteArrayInputStream( "GARBLE \n NARBLE 2 \nQWERTY 3".getBytes() );
        Parser bad_input_parser = new Parser(is_bad_data);

        InputStream is_good_bad = new ByteArrayInputStream( "TO DONTACCEPTME\n GARBLE 2\nREP 3".getBytes() );
        Parser good_bad_parser = new Parser(is_good_bad);
        Instruction good_bad_result = good_bad_parser.readNextInstruction();
        Instruction bad_result = bad_input_parser.readNextInstruction();
        Instruction result_1 = parser.readNextInstruction();
        Instruction result_2 = parser.readNextInstruction();
        Instruction result_3 = parser.readNextInstruction();

        assertEquals("TO",result_1.getCommand());
        assertEquals("1",result_1.getValue());

        assertEquals("REP", result_2.getCommand());
        assertEquals("2", result_2.getValue());

        assertEquals("REP", result_3.getCommand());
        assertEquals("3", result_3.getValue());

        assertEquals(null,bad_result.getValue());
        assertEquals(null,bad_result.getCommand());

        assertEquals("TO",good_bad_result.getCommand());
        assertEquals(null,good_bad_result.getValue());

    }

}