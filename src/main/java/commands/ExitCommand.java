package commands;

import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Represents a command to exit the application.
 */
public class ExitCommand extends Command {

    /**
     * Executes the exit command by displaying goodbye message.
     *
     * @param tasks the task list (not used)
     * @param ui the UI to display goodbye message
     * @param storage the storage (not used)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Executes the exit command by displaying goodbye message on GUI.
     *
     * @param tasks the task list to operate on
     * @param storage the storage to save/load tasks
     * @return
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        return "Aww, see mew next time!";
    }

    /**
     * Returns true to indicate the application should exit.
     *
     * @return true
     */
    @Override
    public boolean isExit() {
        return true;
    }
}