package storage;

import task.Deadline;
import task.Event;
import task.Task;
import task.ToDo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.time.format.DateTimeParseException;

import java.util.Scanner;

/**
 * Handles persistence of tasks by reading from and writing to a data file.
 *
 * The storage format uses pipe-delimited fields:
 * T | isDone | description [| notes]
 * D | isDone | description | deadline [| notes]
 * E | isDone | description | start | end [| notes]
 *
 * The notes field is optional.
 * Invalid or corrupted lines are skipped with a warning.
 */
public class Storage {

    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";

    private static final String DONE_FLAG = "1";
    private static final String NOT_DONE_FLAG = "0";

    private static final String SEPARATOR = " | ";
    private static final String FIELD_DELIMITER_REGEX = " \\| ";

    private static final int NOTES_INDEX_TODO = 3;
    private static final int NOTES_INDEX_DEADLINE = 4;
    private static final int NOTES_INDEX_EVENT = 5;

    private final String filePath;

    /**
     * Creates a Storage object with the specified file path.
     *
     * @param filePath Relative path to the data file
     */
    public Storage(String filePath) {
        assert filePath != null : "File path should not be null";
        assert !filePath.trim().isEmpty() : "File path should not be empty";

        this.filePath = filePath;
    }

    /**
     * Ensures the data file and its parent directories exist.
     * Creates them if they don't exist.
     *
     * @return the File object for the data file
     * @throws StorageException if directory or file creation fails
     */
    private File ensureFileExists() throws StorageException {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new StorageException("Hissterical! I can't create the data directory.");
            }
        }

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new StorageException("You've got to be kitten me. I can't create the data file.");
                }
            } catch (IOException e) {
                throw new StorageException("Hissterical! Look at what's happened: " + e.getMessage());
            }
        }

        return file;
    }

    /**
     * Loads tasks from disk into the given array.
     * Any invalid lines are skipped.
     *
     * @param tasks Array to hold loaded tasks.
     * @return Number of tasks loaded into the array.
     * @throws StorageException If directory or file creation fails, or file cannot be read.
     */
    public int load(Task[] tasks) throws StorageException {
        if (tasks == null) {
            throw new IllegalArgumentException("tasks array must not be null");
        }

        File file = ensureFileExists();

        int taskCount = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine() && taskCount < tasks.length) {
                String line = scanner.nextLine();
                Task task = parseTask(line);
                if (task != null) {
                    tasks[taskCount] = task;
                    taskCount++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new StorageException("Can't find the file, meow: " + e.getMessage());
        }

        return taskCount;
    }

    /**
     * Saves tasks to the data file for data persistence.
     *
     * @param tasks     the array of tasks to save.
     * @param taskCount the number of tasks in the array.
     */
    public void save(Task[] tasks, int taskCount) throws StorageException {
        assert tasks != null : "Task array should not be null";
        assert taskCount >= 0 : "Task count should not be negative";
        assert taskCount <= tasks.length : "Task count should not exceed array length";

        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < taskCount; i++) {
                if (tasks[i] == null) {
                    warn("Skipping null task while saving", "index=" + i);
                    continue;
                }
                writer.write(formatTask(tasks[i]));
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new StorageException("Someone's got a cat-titude today! The task can't be saved: " + e.getMessage());
        }
    }

    /**
     * Converts a {@code Task} into the storage file line format.
     * Notes are appended as an optional trailing field if present.
     *
     * @param task Task to format.
     * @return A single-line representation of the task suitable for saving.
     */
    private String formatTask(Task task) {
        String doneFlag = task.isDone() ? DONE_FLAG : NOT_DONE_FLAG;
        String notesField = task.hasNotes() ? SEPARATOR + task.getNotes() : "";

        return switch (task.getType()) {
            case Todo -> TYPE_TODO + SEPARATOR + doneFlag + SEPARATOR + task.getUserTask() + notesField;
            case Deadline -> {
                Deadline deadline = (Deadline) task;
                yield TYPE_DEADLINE + SEPARATOR + doneFlag + SEPARATOR + deadline.getUserTask()
                        + SEPARATOR + deadline.getStorageDeadline() + notesField;
            }
            case Event -> {
                Event event = (Event) task;
                yield TYPE_EVENT + SEPARATOR + doneFlag + SEPARATOR + event.getUserTask()
                        + SEPARATOR + event.getStorageStart()
                        + SEPARATOR + event.getStorageEnd() + notesField;
            }
        };
    }

    /**
     * Parses one line from the data file into a {@code Task}.
     * Restores the note if the optional notes field is present.
     *
     * Returns {@code null} if the line is blank, corrupted, or cannot be parsed.
     */
    private Task parseTask(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = splitLine(line);
        if (parts == null) {
            return null;
        }

        String taskType = parts[0].trim();
        boolean isDone = DONE_FLAG.equals(parts[1].trim());
        String description = parts[2].trim();

        Task task = buildTask(taskType, description, parts, line);
        if (task == null) {
            return null;
        }

        markDoneIfNeeded(task, isDone);
        restoreNotesIfPresent(task, taskType, parts);
        return task;
    }

    /**
     * Restores the notes field from storage if it was saved.
     * The notes index differs by task type since each has a different number of fields.
     */
    private void restoreNotesIfPresent(Task task, String taskType, String[] parts) {
        int notesIndex = switch (taskType) {
            case TYPE_TODO -> NOTES_INDEX_TODO;
            case TYPE_DEADLINE -> NOTES_INDEX_DEADLINE;
            case TYPE_EVENT -> NOTES_INDEX_EVENT;
            default -> -1;
        };

        if (notesIndex > 0 && parts.length > notesIndex) {
            String notes = parts[notesIndex].trim();
            if (!notes.isEmpty()) {
                task.setNotes(notes);
            }
        }
    }

    /**
     * Splits a stored line into components using the expected delimiter.
     *
     * @return The split parts, or {@code null} if the line is corrupted.
     */
    private String[] splitLine(String line) {
        String[] parts = line.split(FIELD_DELIMITER_REGEX);
        if (parts.length < 3) {
            warn("Skipping corrupted line", line);
            return null;
        }
        return parts;
    }

    /**
     * Constructs the appropriate {@code Task} subtype from parsed fields.
     *
     * @return The constructed task, or {@code null} if the type is unknown or invalid.
     */
    private Task buildTask(String taskType, String description, String[] parts, String line) {
        return switch (taskType) {
            case TYPE_TODO -> new ToDo(description);
            case TYPE_DEADLINE -> buildDeadline(description, parts, line);
            case TYPE_EVENT -> buildEvent(description, parts, line);
            default -> {
                warn("Unknown task type", line);
                yield null;
            }
        };
    }

    /**
     * Builds a {@code Deadline} task from the stored representation.
     */
    private Task buildDeadline(String description, String[] parts, String line) {
        if (parts.length < 4) {
            warn("Skipping corrupted deadline", line);
            return null;
        }

        try {
            String deadlineStr = parts[3].trim();
            return Deadline.createFromString(description, deadlineStr);
        } catch (DateTimeParseException e) {
            warn("Invalid date format in deadline (" + e.getMessage() + ")", line);
            return null;
        }
    }

    /**
     * Builds an {@code Event} task from the stored representation.
     */
    private Task buildEvent(String description, String[] parts, String line) {
        if (parts.length < 5) {
            warn("Skipping corrupted event", line);
            return null;
        }

        try {
            String startStr = parts[3].trim();
            String endStr = parts[4].trim();
            return Event.createFromString(description, startStr, endStr);
        } catch (DateTimeParseException e) {
            warn("Invalid date format in event (" + e.getMessage() + ")", line);
            return null;
        }
    }

    /**
     * Marks the task as done if the stored completion flag indicates so.
     */
    private void markDoneIfNeeded(Task task, boolean isDone) {
        if (isDone) {
            task.markDone();
        }
    }

    /**
     * Prints a warning message for a line that cannot be loaded.
     */
    private void warn(String message, String line) {
        System.out.println("Warning: " + message + ": " + line);
    }
}