package commands;

import storage.Storage;
import storage.StorageException;
import task.Task;

import task.TaskList;
import ui.Ui;

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

    /**
     * Executes the unmark command for GUI mode and returns the result.
     *
     * @param tasks the task list to operate on
     * @param storage the storage to save tasks
     * @return the response message
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            return "Meow? Which task do you want to unmark?";
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task task = tasks.unmarkTask(taskNumber);

            String saveResult = saveToStorageGui(tasks, storage);
            if (saveResult != null) {
                return saveResult;
            }

            return "I was looking forward to a cat nap... but this task is not done yet:\n" + task;
        } catch (NumberFormatException e) {
            return "That doesn't look like a number, furriend!";
        } catch (IndexOutOfBoundsException e) {
            return "No such task to unmark, meow!";
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
}