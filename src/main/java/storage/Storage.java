package storage;

import task.Deadline;
import task.Event;
import task.Task;
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
        break;

        case Event:
        Event event = (Event) task;
        sb.append("E | ");
        sb.append(event.isDone() ? "1" : "0");
        sb.append(" | ");
        sb.append(event.getUserTask());
        sb.append(" | ");
        break;}

        return sb.toString();
    }

    private Task parseTask(String line) {
        Task task = null;
        return task;
    }






}
