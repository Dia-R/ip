package commands;
import storage.Storage;
import storage.StorageException;

import task.Task;
import task.TaskList;

import ui.Ui;

/**
 * Represents a command to delete a task.
 */
public class DeleteCommand extends Command {
    private String argument;

    /**
     * Creates a DeleteCommand with the specified task number.
     *
     * @param argument the task number as a string
     */
    public DeleteCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Executes the delete command by removing the specified task.
     *
     * @param tasks the task list to operate on
     * @param ui the UI to display messages
     * @param storage the storage to save tasks
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("You didn't tell ME-ow which task to delete!");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task removedTask = tasks.deleteTask(taskNumber);
            saveToStorage(tasks, storage, ui);
            ui.showTaskDeleted(removedTask, tasks.getTaskCount());
        } catch (NumberFormatException e) {
            ui.showError("That's not a valid task number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("No task with that number, meow!");
        } catch (Exception e) {
            ui.showError("Something went cat-astrophically wrong: " + e.getMessage());
        }
    }

    /**
     * Executes the delete command for GUI mode and returns the result.
     *
     * @param tasks the task list to operate on
     * @param storage the storage to save tasks
     * @return the response message
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            return "You didn't tell ME-ow which task to delete!";
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task removedTask = tasks.deleteTask(taskNumber);

            String saveResult = saveToStorageGui(tasks, storage);
            if (saveResult != null) {
                return saveResult;
            }

            return "A smart kitty has removed this task:\n" + removedTask + "\n" +
                    getTunaMessage(tasks.getTaskCount());
        } catch (NumberFormatException e) {
            return "That's not a valid task number, furriend!";
        } catch (IndexOutOfBoundsException e) {
            return "No task with that number, meow!";
        } catch (Exception e) {
            return "Something went cat-astrophically wrong: " + e.getMessage();
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