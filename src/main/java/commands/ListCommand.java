package commands;

import storage.Storage;

import task.TaskList;
import ui.Ui;

/**
 * Represents a command to list all tasks.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command by displaying all tasks.
     *
     * @param tasks the task list to display
     * @param ui the UI to display the tasks
     * @param storage the storage (not used)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getAllTasks());
    }

    /**
     * Executes the list command for GUI mode and returns the task list as a string.
     *
     * @param tasks the task list to display
     * @param storage the storage (not used)
     * @return a formatted string of all tasks
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        if (tasks.isEmpty()) {
            return "Litter box is empty...";
        }

        String[] allTasks = tasks.getAllTasks();
        StringBuilder response = new StringBuilder("All your work is tiring ME-OWT! Take a look...\n\n");

        for (String task : allTasks) {
            response.append(task).append("\n");
        }

        return response.toString().trim();
    }
}