//@author A0116014Y
package bakatxt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import bakatxt.core.BakaParser;

public class BakaProcessorTest {
    private BakaParser _parser;

    enum CommandType {
        ADD, DELETE, EDIT, EXIT, DISPLAY, UNDO, REDO, DEFAULT
    }

    @Before
    public void setUp() throws Exception {
        _parser = new BakaParser();
    }

    @Test
    public void testCommandType() {
        String input = "add lunch";
        String command = _parser.getCommand(input);
        CommandType commandType = CommandType.valueOf(command);
        assertEquals("ADD", commandType.toString());
    }

    @Test
    public void testCommandType2() {
        String input = "delete 3";
        String command = _parser.getCommand(input);
        CommandType commandType = CommandType.valueOf(command);
        assertEquals("DELETE", commandType.toString());
    }

    @Test
    public void testWrongCommandType() {
        String input = "remove 3";
        String command = _parser.getCommand(input);
        CommandType commandType;

        /*
         * The command word will be stored as a string and it will get
         * CommandType value but will return DEFAULT if a wrong command is
         * entered
         */

        try {
            commandType = CommandType.valueOf(command);
        } catch (IllegalArgumentException e) {
            commandType = CommandType.DEFAULT;
        }
        assertEquals("DEFAULT", commandType.toString());

    }
}
