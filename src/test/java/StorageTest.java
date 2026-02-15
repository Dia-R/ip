import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import storage.Storage;
import storage.StorageException;
import task.Deadline;
import task.Event;
import task.Task;
import task.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Storage class.
 * Tests file I/O operations for task persistence.
 * Includes both positive and negative test cases.
 */
public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private String testFilePath;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test_tasks.txt").toString();
        storage = new Storage(testFilePath);
    }

    /**
     * Positive: Verify that multiple tasks of different types can be saved
     * and loaded correctly, preserving order and task information.
     */
    @Test
    public void save_multipleDifferentTasks_loadsCorrectly() throws StorageException {
        Task[] tasks = new Task[3];
        tasks[0] = new ToDo("buy milk");
        tasks[1] = Deadline.createFromString("submit report", "2024-12-15 1800");
        tasks[2] = Event.createFromString("team meeting", "2024-12-15 1400", "2024-12-15 1600");

        storage.save(tasks, 3);

        Task[] loadedTasks = new Task[100];
        int count = storage.load(loadedTasks);

        assertEquals(3, count);
        assertEquals("buy milk", loadedTasks[0].getUserTask());
        assertTrue(loadedTasks[0] instanceof ToDo);
        assertEquals("submit report", loadedTasks[1].getUserTask());
        assertTrue(loadedTasks[1] instanceof Deadline);
        assertEquals("team meeting", loadedTasks[2].getUserTask());
        assertTrue(loadedTasks[2] instanceof Event);
    }

    /**
     * Positive: Verify that task completion status (marked/unmarked) is correctly
     * preserved after saving and loading.
     */
    @Test
    public void save_taskCompletionStatus_preservesStatus() throws StorageException {
        Task[] tasks = new Task[2];
        tasks[0] = new ToDo("completed task");
        tasks[0].markDone();
        tasks[1] = new ToDo("incomplete task");

        storage.save(tasks, 2);

        Task[] loadedTasks = new Task[100];
        storage.load(loadedTasks);

        assertTrue(loadedTasks[0].isDone());
        assertFalse(loadedTasks[1].isDone());
    }

    /**
     * Positive: Verify that loading from a non-existent file creates the file
     * and returns zero tasks without throwing exceptions.
     */
    @Test
    public void load_nonExistentFile_createsEmptyFile() throws StorageException {
        Task[] tasks = new Task[100];
        int count = storage.load(tasks);

        assertEquals(0, count);
        assertTrue(new File(testFilePath).exists());
    }

    /**
     * Verify that corrupted lines in the storage file are skipped
     * gracefully without preventing loading of valid tasks.
     */
    @Test
    public void load_corruptedData_skipsInvalidLines() throws IOException, StorageException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("T | 0 | valid task 1\n");
            writer.write("CORRUPTED INVALID LINE\n");
            writer.write("T | 0 | valid task 2\n");
        }

        Task[] loadedTasks = new Task[100];
        int count = storage.load(loadedTasks);

        assertEquals(2, count);
        assertEquals("valid task 1", loadedTasks[0].getUserTask());
        assertEquals("valid task 2", loadedTasks[1].getUserTask());
    }

    /**
     * Verify that tasks with invalid date formats are skipped
     * without crashing the loading process.
     */
    @Test
    public void load_invalidDateFormat_skipsTask() throws IOException, StorageException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("D | 0 | bad deadline | invalid-date-format\n");
            writer.write("T | 0 | valid todo\n");
        }

        Task[] loadedTasks = new Task[100];
        int count = storage.load(loadedTasks);

        assertEquals(1, count);
        assertEquals("valid todo", loadedTasks[0].getUserTask());
    }

    /**
     * Verify that tasks with unknown type identifiers are skipped
     * gracefully without affecting other valid tasks.
     */
    @Test
    public void load_unknownTaskType_skipsTask() throws IOException, StorageException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("X | 0 | unknown type task\n");
            writer.write("T | 0 | valid todo\n");
        }

        Task[] loadedTasks = new Task[100];
        int count = storage.load(loadedTasks);

        assertEquals(1, count);
        assertEquals("valid todo", loadedTasks[0].getUserTask());
    }
}