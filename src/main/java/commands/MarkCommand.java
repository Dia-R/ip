package commands;

import storage.Storage;
import storage.StorageException;
import task.Task;
import task.TaskList;
import app.Ui;

/**
 * Represents a command to mark a task as done.
 */
public class MarkCommand extends Command {
    private String argument;

    /**
     * Creates a MarkCommand with the specified task number.
     *
     * @param argument the task number as a string
     */
    public MarkCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Executes the mark command by marking the specified task as done.
     *
     * @param tasks the task list to operate on
     * @param ui the UI to display messages
     * @param storage the storage to save tasks
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("Meow? Which task do you want to mark?");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task task = tasks.markTask(taskNumber);
            saveToStorage(tasks, storage, ui);
            ui.showTaskMarked(task);
        } catch (NumberFormatException e) {
            ui.showError("That doesn't look like a number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("No such task to mark, meow!");
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