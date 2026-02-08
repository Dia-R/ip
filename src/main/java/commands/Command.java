package commands;

import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Represents an abstract command that can be executed.
 * All specific command types inherit from this class.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @param tasks the task list to operate on
     * @param ui the UI to interact with the user
     * @param storage the storage to save/load tasks
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /**
     * Returns whether this command should exit the application.
     *
     * @return true if the application should exit, false otherwise
     */
    public boolean isExit() {
        return false;
    }
}