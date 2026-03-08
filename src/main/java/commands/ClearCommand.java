package commands;

import storage.Storage;
import storage.StorageException;
import task.Task;
import task.TaskList;
import ui.Ui;

/**
 * Represents a command to delete all tasks from the task list.
 * Usage: clear
 */
public class ClearCommand extends Command {

    /**
     * Executes the clear command for CLI mode.
     *
     * @param tasks   the task list to clear
     * @param ui      the UI to display messages
     * @param storage the storage to save the empty list
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showError(clearAll(tasks, storage));
    }

    /**
     * Executes the clear command for GUI mode.
     *
     * @param tasks   the task list to clear
     * @param storage the storage to save the empty list
     * @return the response message
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        return clearAll(tasks, storage);
    }

    /**
     * Core logic: clears all tasks and saves the empty list.
     *
     * @return a response message describing the outcome.
     */
    private String clearAll(TaskList tasks, Storage storage) {
        if (tasks.isEmpty()) {
            return "The litter box is already empty, meow!";
        }

        int count = tasks.getTaskCount();
        tasks.clearAll();

        try {
            storage.save(new Task[0], 0);
        } catch (StorageException e) {
            return "Oh no! Failed to save after clearing: " + e.getMessage();
        }

        return "Poof! All " + count + " task(s) have been swept away. Fresh litter box!";
    }
}