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

    /**
     * Executes the add command for GUI mode and returns the result.
     *
     * @param tasks the task list to add to
     * @param storage the storage to save tasks
     * @return the response message
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        try {
            if (taskString.startsWith("todo")) {
                return handleTodoGui(tasks, storage);
            } else if (taskString.startsWith("deadline")) {
                return handleDeadlineGui(tasks, storage);
            } else if (taskString.startsWith("event")) {
                return handleEventGui(tasks, storage);
            } else {
                return "Wait a meow-nute... You've got me feeling purr-plexed...";
            }
        } catch (Exception e) {
            return "Something went cat-astrophically wrong: " + e.getMessage();
        }
    }

    /**
     * Parses and adds a ToDo task.
     */
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

    /**
     * Parses and adds a ToDo task for GUI mode.
     */
    private String handleTodoGui(TaskList tasks, Storage storage) {
        String desc = taskString.length() > 4 ? taskString.substring(4).trim() : "";
        if (desc.isEmpty()) {
            return "Nyat today! Give me a description too please!";
        }
        ToDo todo = new ToDo(desc);
        tasks.addTask(todo);

        String saveResult = saveToStorageGui(tasks, storage);
        if (saveResult != null) {
            return saveResult;
        }

        return "Nya-ice! I've added: " + todo + "\n" + getTunaMessage(tasks.getTaskCount());
    }

    /**
     * Parses and adds a Deadline task.
     */
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

    /**
     * Parses and adds a Deadline task for GUI mode.
     */
    private String handleDeadlineGui(TaskList tasks, Storage storage) {
        String deadlineArgs = taskString.length() > 8 ? taskString.substring(8).trim() : "";
        String[] parts = deadlineArgs.split(" /by ", 2);

        String description = parts.length > 0 ? parts[0].trim() : "";
        String by = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty() || by.isEmpty()) {
            return "Aren't you furrgetting something? Please provide description and a due date!\n" +
                    "Format: deadline <description> /by <yyyy-MM-dd HHmm>\n" +
                    "Example: deadline return book /by 2024-12-02 1800";
        }

        try {
            Deadline deadline = Deadline.createFromString(description, by);
            tasks.addTask(deadline);

            String saveResult = saveToStorageGui(tasks, storage);
            if (saveResult != null) {
                return saveResult;
            }

            return "Nya-ice! I've added: " + deadline + "\n" + getTunaMessage(tasks.getTaskCount());
        } catch (DateTimeParseException e) {
            return "Meow-ch! That date format doesn't look right!\n" +
                    "Please use: yyyy-MM-dd HHmm (e.g., 2024-12-02 1800)";
        }
    }

    /**
     * Parses and adds an Event task.
     */
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

    /**
     * Parses and adds an Event task for GUI mode.
     */
    private String handleEventGui(TaskList tasks, Storage storage) {
        String eventArgs = taskString.length() > 5 ? taskString.substring(5).trim() : "";

        int fromIndex = eventArgs.indexOf(" /from ");
        if (fromIndex == -1) {
            return "Events need a description, start, and end time, meow...\n" +
                    "Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n" +
                    "Example: event project meeting /from 2024-08-06 1400 /to 2024-08-06 1600";
        }

        String description = eventArgs.substring(0, fromIndex).trim();
        String timeString = eventArgs.substring(fromIndex + 7);

        int toIndex = timeString.indexOf(" /to ");
        if (toIndex == -1) {
            return "Events need a description, start, and end time, meow...\n" +
                    "Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n" +
                    "Example: event project meeting /from 2024-08-06 1400 /to 2024-08-06 1600";
        }

        String start = timeString.substring(0, toIndex).trim();
        String end = timeString.substring(toIndex + 5).trim();

        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            return "Events need a description, start, and end time, meow...\n" +
                    "Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n" +
                    "Example: event project meeting /from 2024-08-06 1400 /to 2024-08-06 1600";
        }

        try {
            Event event = Event.createFromString(description, start, end);
            tasks.addTask(event);

            String saveResult = saveToStorageGui(tasks, storage);
            if (saveResult != null) {
                return saveResult;
            }

            return "Nya-ice! I've added: " + event + "\n" + getTunaMessage(tasks.getTaskCount());
        } catch (DateTimeParseException e) {
            return "Meow-ch! That date format doesn't look right!\n" +
                    "Please use: yyyy-MM-dd HHmm (e.g., 2024-12-02 1800)";
        }
    }

    /**
     * Saves the current task list to persistent storage.
     */
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

    /**
     * Saves the current task list to persistent storage for GUI mode.
     *
     * @return error message if save failed, null if successful
     */
    private String saveToStorageGui(TaskList tasks, Storage storage) {
        try {
            Task[] taskArray = new Task[tasks.getTaskCount()];
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                taskArray[i] = tasks.getTask(i);
            }
            storage.save(taskArray, tasks.getTaskCount());
            return null;
        } catch (StorageException e) {
            return "Oh no! Failed to save tasks: " + e.getMessage();
        }
    }

    /**
     * Returns a tuna-themed message about the task count.
     */
    private String getTunaMessage(int taskCount) {
        return "If I had a can of tuna for every task you have to do, I'd have... "
                + taskCount + ". Yum!";
    }
}