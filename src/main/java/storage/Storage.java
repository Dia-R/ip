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
 * T | isDone | description
 * D | isDone | description | deadline
 * E | isDone | description | start | end
 *
 * Invalid or corrupted lines are skipped with a warning.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage object with the specified file path.
     *
     * @param filePath Relative path to the data file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from disk into the given array.
     * Ensures parent directories exist and creates the data file if missing.
     * Any invalid lines are skipped.
     *
     * @param tasks Array to hold loaded tasks.
     * @return Number of tasks loaded into the array.
     * @throws StorageException If directory or file creation fails, or file cannot be read.
     */
    public int load(Task[] tasks) throws StorageException {
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
                    throw new StorageException("You've got to be kitten me. I can't create the data file");
                }
            } catch (IOException e) {
                throw new StorageException("Hissterical! Look at what's happened: " + e.getMessage());
            }
            return 0;
        }

        int taskCount = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine() && taskCount < tasks.length) {
                String line = scanner.nextLine();
                Task task = parseTask(line);
                if (task != null) {
                    tasks[taskCount++] = task;
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
     * @param tasks the array of tasks to save.
     * @param taskCount the number of tasks in the array.
     */
    public void save(Task[] tasks, int taskCount) throws StorageException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < taskCount; i++) {
                writer.write(formatTask(tasks[i]) + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new StorageException("Someone's got a cat-titude today! The task can't be saved: " + e.getMessage());
        }
    }

    /**
     * Converts a {@code Task} into the storage file line format.
     *
     * @param task Task to format.
     * @return A single-line representation of the task suitable for saving.
     */
    private String formatTask(Task task) {
        StringBuilder sb = new StringBuilder();

        switch (task.getType()) {
        case Todo:
            sb.append("T | ");
            sb.append(task.isDone() ? "1" : "0");
            sb.append(" | ");
            sb.append(task.getUserTask());
            break;

        case Deadline:
            Deadline deadline = (Deadline) task;
            sb.append("D | ");
            sb.append(deadline.isDone() ? "1" : "0");
            sb.append(" | ");
            sb.append(deadline.getUserTask());
            sb.append(" | ");
            sb.append(deadline.getStorageDeadline());
            break;

        case Event:
            Event event = (Event) task;
            sb.append("E | ");
            sb.append(event.isDone() ? "1" : "0");
            sb.append(" | ");
            sb.append(event.getUserTask());
            sb.append(" | ");
            sb.append(event.getStorageStart());
            sb.append(" | ");
            sb.append(event.getStorageEnd());
            break;
        }

        return sb.toString();
    }

    /**
     * Parses one line from the data file into a {@code Task}.
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
        boolean isDone = "1".equals(parts[1].trim());
        String description = parts[2].trim();

        Task task = buildTask(taskType, description, parts, line);
        if (task == null) {
            return null;
        }

        markDoneIfNeeded(task, isDone);
        return task;
    }

    /**
     * Splits a stored line into components using the expected delimiter.
     *
     * @return The split parts, or {@code null} if the line is corrupted.
     */
    private String[] splitLine(String line) {
        try {
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                warn("Skipping corrupted line", line);
                return null;
            }
            return parts;
        } catch (Exception e) {
            warn("Error splitting line (" + e.getMessage() + ")", line);
            return null;
        }
    }

    /**
     * Constructs the appropriate {@code Task} subtype from parsed fields.
     *
     * @return The constructed task, or {@code null} if the type is unknown or invalid.
     */
    private Task buildTask(String taskType, String description, String[] parts, String line) {
        switch (taskType) {
        case "T":
            return new ToDo(description);

        case "D":
            return buildDeadline(description, parts, line);

        case "E":
            return buildEvent(description, parts, line);

        default:
            System.out.println("Unknown task type: " + taskType);
            return null;
        }
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