package commands;

import storage.Storage;
import storage.StorageException;
import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.ToDo;
import ui.Ui;
import java.time.format.DateTimeParseException;

/**
 * Represents a command to add a task (todo, deadline, or event).
 */
public class AddCommand extends Command {
    private String taskString;

    /**
     * Creates an AddCommand with the specified task string.
     *
     * @param taskString the full task string (e.g., "todo read book")
     */
    public AddCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the add command by parsing and adding the task.
     *
     * @param tasks the task list to add to
     * @param ui the UI to display messages
     * @param storage the storage to save tasks
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            if (taskString.startsWith("todo")) {
                handleTodo(tasks, ui, storage);
            } else if (taskString.startsWith("deadline")) {
                handleDeadline(tasks, ui, storage);
            } else if (taskString.startsWith("event")) {
                handleEvent(tasks, ui, storage);
            } else {
                ui.showError("Wait a meow-nute... You've got me feeling purr-plexed...");
            }
        } catch (Exception e) {
            ui.showError("Something went cat-astrophically wrong: " + e.getMessage());
        }
    }

    private void handleTodo(TaskList tasks, Ui ui, Storage storage) {
        String desc = taskString.length() > 4 ? taskString.substring(4).trim() : "";
        if (desc.isEmpty()) {
            ui.showError("Nyat today! Give me a description too please!");
            return;
        }
        ToDo todo = new ToDo(desc);
        tasks.addTask(todo);
        saveToStorage(tasks, storage, ui);
        ui.showTaskAdded(todo, tasks.getTaskCount());
    }

    private void handleDeadline(TaskList tasks, Ui ui, Storage storage) {
        String deadlineArgs = taskString.length() > 8 ? taskString.substring(8).trim() : "";
        String[] parts = deadlineArgs.split(" /by ", 2);

        String description = parts.length > 0 ? parts[0].trim() : "";
        String by = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty() || by.isEmpty()) {
            ui.showDeadlineFormatHelp();
            return;
        }

        try {
            Deadline deadline = Deadline.createFromString(description, by);
            tasks.addTask(deadline);
            saveToStorage(tasks, storage, ui);
            ui.showTaskAdded(deadline, tasks.getTaskCount());
        } catch (DateTimeParseException e) {
            ui.showDateFormatError();
        }
    }

    private void handleEvent(TaskList tasks, Ui ui, Storage storage) {
        String eventArgs = taskString.length() > 5 ? taskString.substring(5).trim() : "";

        int fromIndex = eventArgs.indexOf(" /from ");
        if (fromIndex == -1) {
            ui.showEventFormatHelp();
            return;
        }

        String description = eventArgs.substring(0, fromIndex).trim();
        String timeString = eventArgs.substring(fromIndex + 7);

        int toIndex = timeString.indexOf(" /to ");
        if (toIndex == -1) {
            ui.showEventFormatHelp();
            return;
        }

        String start = timeString.substring(0, toIndex).trim();
        String end = timeString.substring(toIndex + 5).trim();

        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            ui.showEventFormatHelp();
            return;
        }

        try {
            Event event = Event.createFromString(description, start, end);
            tasks.addTask(event);
            saveToStorage(tasks, storage, ui);
            ui.showTaskAdded(event, tasks.getTaskCount());
        } catch (DateTimeParseException e) {
            ui.showDateFormatError();
        }
    }

    private void saveToStorage(TaskList tasks, Storage storage, Ui ui) {
        try {
            Task[] taskArray = new Task[tasks.getTaskCount()];
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                taskArray[i] = tasks.getTask(i);
            }
            storage.save(taskArray, tasks.getTaskCount());
        } catch (StorageException e) {
            ui.showError("Oh no! Failed to save tasks: " + e.getMessage());
        }
    }
}