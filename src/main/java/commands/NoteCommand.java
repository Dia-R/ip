package commands;

import storage.Storage;
import storage.StorageException;
import task.Task;
import task.TaskList;
import ui.Ui;

/**
 * Represents a command to attach an optional note to an existing task.
 * Usage: note <task number> <note text>
 * Example: note 1 bring umbrella just in case
 */
public class NoteCommand extends Command {

    private final String argument;

    /**
     * Creates a NoteCommand with the raw argument string (everything after "note").
     *
     * @param argument the task number and note text, e.g. "1 bring umbrella"
     */
    public NoteCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Executes the note command for CLI mode.
     *
     * @param tasks   the task list to operate on
     * @param ui      the UI to display messages
     * @param storage the storage to save tasks
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String result = addNote(tasks, storage);
        ui.showError(result); // showError is the general-purpose print method in Ui
    }

    /**
     * Executes the note command for GUI mode and returns the result.
     *
     * @param tasks   the task list to operate on
     * @param storage the storage to save tasks
     * @return the response message
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        return addNote(tasks, storage);
    }

    /**
     * Core logic: parses the argument, attaches the note to the task, and saves.
     *
     * @return a response message describing the outcome.
     */
    private String addNote(TaskList tasks, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            return "Meow? Tell me which task and what note!\n"
                    + "Format: note <task number> <note text>\n"
                    + "Example: note 1 bring umbrella just in case";
        }

        // Split into task number and note text on the first space
        String[] parts = argument.trim().split("\\s+", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            return "Nyat quite right! I need a task number AND some note text.\n"
                    + "Format: note <task number> <note text>\n"
                    + "Example: note 1 bring umbrella just in case";
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
            return "That doesn't look like a task number, furriend!";
        }

        try {
            Task task = tasks.getTask(taskNumber - 1); // getTask uses 0-based index
            String noteText = parts[1].trim();
            task.setNotes(noteText);

            String saveResult = saveToStorageGui(tasks, storage);
            if (saveResult != null) {
                return saveResult;
            }

            return "Purr-fect! Note added to task " + taskNumber + ":\n" + formatTaskWithIndex(taskNumber, task);

        } catch (IndexOutOfBoundsException e) {
            return "No such task, meow! Please pick a valid task number.";
        }
    }

    /**
     * Formats a task with its 1-based list index for display.
     */
    private String formatTaskWithIndex(int taskNumber, Task task) {
        char typeLetter = switch (task.getType()) {
            case Todo -> 'T';
            case Deadline -> 'D';
            case Event -> 'E';
        };
        return taskNumber + ".[" + typeLetter + "]" + task;
    }

    /**
     * Saves the current task list to persistent storage.
     *
     * @return error message if save failed, null if successful.
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
}