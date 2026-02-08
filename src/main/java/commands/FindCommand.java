package commands;

import storage.Storage;

import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;

import ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a command to find tasks on a specific date.
 */
public class FindCommand extends Command {
    private String argument;

    /**
     * Creates a FindCommand with the specified date string.
     *
     * @param argument the date string in yyyy-MM-dd format
     */
    public FindCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Executes the find command by searching for tasks on the specified date.
     *
     * @param tasks the task list to search
     * @param ui the UI to display results
     * @param storage the storage (not used)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("What date should I search fur? (yyyy-MM-dd)");
            return;
        }

        try {
            LocalDate searchDate = LocalDate.parse(argument.trim());
            DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");

            ui.showFindHeader(searchDate.format(displayFormat));

            boolean foundDeadlines = false;
            boolean foundEvents = false;
            int count = 0;

            // First pass: find deadlines
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                Task task = tasks.getTask(i);

                if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;
                    if (deadline.getDeadline().toLocalDate().equals(searchDate)) {
                        if (!foundDeadlines) {
                            ui.showDeadlineSection();
                            foundDeadlines = true;
                        }
                        System.out.println((++count) + ". " + task);
                    }
                }
            }

            // Second pass: find events
            count = 0;
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                Task task = tasks.getTask(i);

                if (task instanceof Event) {
                    Event event = (Event) task;
                    if (event.getStart().toLocalDate().equals(searchDate)) {
                        if (!foundEvents) {
                            ui.showEventSection();
                            foundEvents = true;
                        }
                        System.out.println((++count) + ". " + task);
                    }
                }
            }

            if (!foundDeadlines && !foundEvents) {
                ui.showNoTasksFound();
            }

        } catch (DateTimeParseException e) {
            ui.showFindDateFormatError();
        }
    }
}