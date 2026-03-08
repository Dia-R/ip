import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import task.Task;
import task.TaskList;
import task.ToDo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for TaskList class.
 * Tests task management operations including add, delete, mark, and unmark.
 */
public class TaskListTest {
    private TaskList taskList;
    private Task todo1;
    private Task todo2;
    private Task todo3;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todo1 = new ToDo("buy milk");
        todo2 = new ToDo("read book");
        todo3 = new ToDo("exercise");
    }

    @Test
    public void addTask_singleTask_success() {
        taskList.addTask(todo1);
        assertEquals(1, taskList.getTaskCount());
        assertEquals(todo1, taskList.getTask(0));
    }

    @Test
    public void addTask_multipleTasks_success() {
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);
        
        assertEquals(3, taskList.getTaskCount());
        assertEquals(todo1, taskList.getTask(0));
        assertEquals(todo2, taskList.getTask(1));
        assertEquals(todo3, taskList.getTask(2));
    }

    @Test
    public void getTask_validIndex_returnsCorrectTask() {
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        
        assertEquals(todo1, taskList.getTask(0));
        assertEquals(todo2, taskList.getTask(1));
    }

    @Test
    public void getTask_invalidIndex_throwsException() {
        taskList.addTask(todo1);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTask(5);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTask(-1);
        });
    }

    @Test
    public void markTask_unmarkedTask_becomesMarked() {
        taskList.addTask(todo1);
        assertFalse(todo1.isDone());
        
        Task marked = taskList.markTask(1);
        
        assertTrue(marked.isDone());
        assertEquals(todo1, marked);
    }

    @Test
    public void markTask_alreadyMarked_remainsMarked() {
        taskList.addTask(todo1);
        taskList.markTask(1);
        assertTrue(todo1.isDone());
        
        taskList.markTask(1);
        assertTrue(todo1.isDone());
    }

    @Test
    public void markTask_invalidIndex_throwsException() {
        taskList.addTask(todo1);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.markTask(5);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.markTask(0);  // 1-indexed
        });
    }

    @Test
    public void unmarkTask_markedTask_becomesUnmarked() {
        taskList.addTask(todo1);
        todo1.markDone();
        assertTrue(todo1.isDone());
        
        Task unmarked = taskList.unmarkTask(1);
        
        assertFalse(unmarked.isDone());
        assertEquals(todo1, unmarked);
    }

    @Test
    public void deleteTask_validIndex_removesTask() {
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);
        assertEquals(3, taskList.getTaskCount());
        
        Task deleted = taskList.deleteTask(2);
        
        assertEquals(todo2, deleted);
        assertEquals(2, taskList.getTaskCount());
        assertEquals(todo1, taskList.getTask(0));
        assertEquals(todo3, taskList.getTask(1));
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() {
        taskList.addTask(todo1);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(5);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(0);
        });
    }

    @Test
    public void isEmpty_emptyList_returnsTrue() {
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void isEmpty_nonEmptyList_returnsFalse() {
        taskList.addTask(todo1);
        assertFalse(taskList.isEmpty());
    }

    @Test
    public void isEmpty_afterDelete_returnsTrue() {
        taskList.addTask(todo1);
        taskList.deleteTask(1);
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void getAllTasks_emptyList_returnsEmptyArray() {
        String[] tasks = taskList.getAllTasks();
        assertEquals(0, tasks.length);
    }

    @Test
    public void getAllTasks_multipleTasks_returnsFormattedStrings() {
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        
        String[] tasks = taskList.getAllTasks();
        
        assertEquals(2, tasks.length);
        assertTrue(tasks[0].contains("buy milk"));
        assertTrue(tasks[1].contains("read book"));
        assertTrue(tasks[0].contains("1."));
        assertTrue(tasks[1].contains("2."));
    }

    @Test
    public void getTaskCount_emptyList_returnsZero() {
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    public void getTaskCount_afterOperations_returnsCorrectCount() {
        assertEquals(0, taskList.getTaskCount());
        
        taskList.addTask(todo1);
        assertEquals(1, taskList.getTaskCount());
        
        taskList.addTask(todo2);
        assertEquals(2, taskList.getTaskCount());
        
        taskList.deleteTask(1);
        assertEquals(1, taskList.getTaskCount());
    }

    @Test
    public void clearAll_nonEmptyList_removesAllTasks() {
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);

        taskList.clearAll();

        assertEquals(0, taskList.getTaskCount());
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void clearAll_emptyList_remainsEmpty() {
        taskList.clearAll();

        assertEquals(0, taskList.getTaskCount());
        assertTrue(taskList.isEmpty());
    }
}
