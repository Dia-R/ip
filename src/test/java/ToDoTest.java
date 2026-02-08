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
        
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[ ]"));
        assertTrue(result.contains("buy milk"));
    }

    @Test
    public void toString_markedTask_correctFormat() {
        todo.markDone();
        String result = todo.toString();
        
        assertTrue(result.contains("[T]"));
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
}
