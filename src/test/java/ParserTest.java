import commands.*;
import org.junit.jupiter.api.Test;
import parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Parser class.
 * Tests parsing of various user commands into Command objects.
 */
public class ParserTest {

    @Test
    public void parse_byeCommand_returnsExitCommand() {
        Command command = Parser.parse("bye");
        assertTrue(command instanceof ExitCommand);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_byeWithSpaces_returnsExitCommand() {
        Command command = Parser.parse("  bye  ");
        assertTrue(command instanceof ExitCommand);
    }

    @Test
    public void parse_byeUpperCase_returnsExitCommand() {
        Command command = Parser.parse("BYE");
        assertTrue(command instanceof ExitCommand);
    }

    @Test
    public void parse_listCommand_returnsListCommand() {
        Command command = Parser.parse("list");
        assertTrue(command instanceof ListCommand);
        assertFalse(command.isExit());
    }

    @Test
    public void parse_listWithSpaces_returnsListCommand() {
        Command command = Parser.parse("  list  ");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    public void parse_markCommand_returnsMarkCommand() {
        Command command = Parser.parse("mark 1");
        assertTrue(command instanceof MarkCommand);
    }

    @Test
    public void parse_markWithMultipleSpaces_returnsMarkCommand() {
        Command command = Parser.parse("mark   3");
        assertTrue(command instanceof MarkCommand);
    }

    @Test
    public void parse_unmarkCommand_returnsUnmarkCommand() {
        Command command = Parser.parse("unmark 2");
        assertTrue(command instanceof UnmarkCommand);
    }

    @Test
    public void parse_deleteCommand_returnsDeleteCommand() {
        Command command = Parser.parse("delete 1");
        assertTrue(command instanceof DeleteCommand);
    }

    @Test
    public void parse_findCommand_returnsFindCommand() {
        Command command = Parser.parse("find 2024-12-15");
        assertTrue(command instanceof FindCommand);
    }

    @Test
    public void parse_todoCommand_returnsAddCommand() {
        Command command = Parser.parse("todo buy milk");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_deadlineCommand_returnsAddCommand() {
        Command command = Parser.parse("deadline return book /by 2024-12-15 1800");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_eventCommand_returnsAddCommand() {
        Command command = Parser.parse("event meeting /from 2024-12-15 1400 /to 2024-12-15 1600");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_unknownCommand_returnsAddCommand() {
        // Unknown commands are treated as potential task additions
        Command command = Parser.parse("random text");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_emptyString_returnsAddCommand() {
        Command command = Parser.parse("");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_mixedCase_parsesCorrectly() {
        Command command1 = Parser.parse("LiSt");
        assertTrue(command1 instanceof ListCommand);
        
        Command command2 = Parser.parse("MaRk 1");
        assertTrue(command2 instanceof MarkCommand);
        
        Command command3 = Parser.parse("ToDo buy milk");
        assertTrue(command3 instanceof AddCommand);
    }

    @Test
    public void parse_noteCommand_returnsNoteCommand() {
        Command command = Parser.parse("note 1 bring umbrella");
        assertTrue(command instanceof NoteCommand);
    }

    @Test
    public void parse_clearCommand_returnsClearCommand() {
        Command command = Parser.parse("clear");
        assertTrue(command instanceof ClearCommand);
    }
}
