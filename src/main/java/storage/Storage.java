package storage;

import task.Deadline;
import task.Event;
import task.Task;
import task.ToDo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to a file on disk.
 *
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage object with the specified file path.
     *
     * @param filePath relative path to the data file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file into the task list.
     * Creates the file and necessary directories if they don't exist.
     *
     * @param tasks array to hold tasks
     * @return the number of tasks loaded
     * @throws StorageException if there is an error reading the file
     */
    public int load(Task[] tasks) throws StorageException, FileNotFoundException {
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
                    throw new StorageException("You’ve got to be kitten me. I can't create the data file");
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
                if (task !=null) {
                    tasks[taskCount++] = task;
                }
            }
        }

        return taskCount;
    }

    /**
     * Saves tasks to the data file
     *
     * @param tasks the array of tasks to save
     * @param taskCount the number of tasks in the array
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
        sb.append(parseDeadline(deadline));
        break;

        case Event:
        Event event = (Event) task;
        sb.append("E | ");
        sb.append(event.isDone() ? "1" : "0");
        sb.append(" | ");
        sb.append(event.getUserTask());
        sb.append(" | ");
        sb.append(parseEvent(event));
        break;}

        return sb.toString();
    }

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

    private Task buildDeadline(String description, String[] parts, String line) {
        if (parts.length < 4) {
            warn("Skipping corrupted deadline", line);
            return null;
        }
        String deadline = parts[3].trim();
        return new Deadline(description, deadline);
    }

    private Task buildEvent(String description, String[] parts, String line) {
        if (parts.length < 4) {
            warn("Skipping corrupted event", line);
            return null;
        }

        String[] times = parts[3].trim().split(" to ");
        if (times.length != 2) {
            warn("Skipping corrupted event", line);
            return null;
        }

        String start = times[0].trim();
        String end = times[1].trim();
        return new Event(description, start, end);
    }

    private void markDoneIfNeeded(Task task, boolean isDone) {
        if (isDone) {
            task.markDone();
        }
    }

    private void warn(String message, String line) {
        System.out.println("Warning: " + message + ": " + line);
    }

    private String parseEvent(Event event) {
        String str = event.toString();
        int fromIndex = str.indexOf("(from: ");
        if (fromIndex != -1) {
            String timePart = str.substring(fromIndex + 7, str.length() - 1);
            return timePart.replace(" to: ", " to ");
        }
        return "";
    }

    private String parseDeadline(Deadline deadline) {
        String str = deadline.toString();
        int byIndex = str.indexOf("(by: ");
        if (byIndex != -1) {
            return str.substring(byIndex + 5, str.length() - 1);
        }
        return "";
    }





}
