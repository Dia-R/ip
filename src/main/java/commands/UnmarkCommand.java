package commands;

import storage.Storage;
import storage.StorageException;
import task.Task;
import task.TaskList;
import app.Ui;

/**
 * Represents a command to unmark a task (mark as not done).
 */
public class UnmarkCommand extends Command {
    private String argument;

    /**
     * Creates an UnmarkCommand with the specified task number.
     *
     * @param argument the task number as a string
     */
    public UnmarkCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Executes the unmark command by marking the specified task as not done.
     *
     * @param tasks the task list to operate on
     * @param ui the UI to display messages
     * @param storage the storage to save tasks
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("Meow? Which task do you want to unmark?");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task task = tasks.unmarkTask(taskNumber);
            saveToStorage(tasks, storage, ui);
            ui.showTaskUnmarked(task);
        } catch (NumberFormatException e) {
            ui.showError("That doesn't look like a number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("No such task to unmark, meow!");
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