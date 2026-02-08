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