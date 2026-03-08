import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import task.TaskType;
import task.ToDo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for ToDo class.
 * Tests basic task functionality and string representation.
 */
public class ToDoTest {
    private ToDo todo;

    @BeforeEach
    public void setUp() {
        todo = new ToDo("buy milk");
    }

    @Test
    public void constructor_validDescription_createsTask() {
        assertEquals("buy milk", todo.getUserTask());
        assertEquals(TaskType.Todo, todo.getType());
        assertFalse(todo.isDone());
    }

    @Test
    public void markDone_unmarkedTask_becomesMarked() {
        assertFalse(todo.isDone());
        todo.markDone();
        assertTrue(todo.isDone());
    }

    @Test
    public void markDone_alreadyMarked_remainsMarked() {
        todo.markDone();
        assertTrue(todo.isDone());
        
        todo.markDone();
        assertTrue(todo.isDone());
    }

    @Test
    public void markNotDone_markedTask_becomesUnmarked() {
        todo.markDone();
        assertTrue(todo.isDone());
        todo.unmarkDone();
        assertFalse(todo.isDone());
    }

    @Test
    public void markNotDone_unmarkedTask_remainsUnmarked() {
        assertFalse(todo.isDone());
        todo.unmarkDone();
        assertFalse(todo.isDone());
    }

    @Test
    public void toString_unmarkedTask_correctFormat() {
        String result = todo.toString();

        assertTrue(result.contains("[ ]"));
        assertTrue(result.contains("buy milk"));
    }

    @Test
    public void toString_markedTask_correctFormat() {
        todo.markDone();
        String result = todo.toString();

        assertTrue(result.contains("[X]"));
        assertTrue(result.contains("buy milk"));
    }

    @Test
    public void getType_returnsTodoType() {
        assertEquals(TaskType.Todo, todo.getType());
    }

    @Test
    public void getUserTask_returnsDescription() {
        assertEquals("buy milk", todo.getUserTask());
    }

    @Test
    public void setNotes_validNote_storesNote() {
        todo.setNotes("buy the organic kind");
        assertTrue(todo.hasNotes());
        assertEquals("buy the organic kind", todo.getNotes());
    }

    @Test
    public void toString_withNote_includesNoteInOutput() {
        todo.setNotes("buy the organic kind");
        String result = todo.toString();
        assertTrue(result.contains("Note:"));
        assertTrue(result.contains("buy the organic kind"));
    }
}
