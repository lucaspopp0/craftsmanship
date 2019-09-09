package channels;

import org.junit.Test;

import static org.junit.Assert.*;

public class InstructionTest {

    @Test
    public void isValid() {
        Instruction result_ff = new Instruction("NO", "NEVER");
        Instruction result_ft = new Instruction("NO", "1");
        Instruction result_tf = new Instruction("TO", "NEVER");
        Instruction result_tt = new Instruction("TO", "1");
        assertFalse(result_ff.isValid());
        assertFalse(result_ft.isValid());
        assertFalse(result_tf.isValid());
        assertTrue(result_tt.isValid());
    }

    @Test
    public void isValidValue() {
        assertFalse(Instruction.isValidValue(null));
        assertTrue(Instruction.isValidValue("7"));
        assertFalse(Instruction.isValidValue("Not_Valid"));
    }
}