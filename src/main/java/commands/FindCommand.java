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
 * Represents a command to find tasks.
 * Can search by date (yyyy-MM-dd) or by keyword in description.
 */
public class FindCommand extends Command {
    private String argument;

    /**
     * Creates a FindCommand with the specified search argument.
     *
     * @param argument the date string (yyyy-MM-dd) or keyword to search
     */
    public FindCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Executes the find command by searching for tasks.
     * Attempts to parse as date first, then falls back to keyword search.
     *
     * @param tasks the task list to search
     * @param ui the UI to display results
     * @param storage the storage (not used)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("What should I search fur? (date or keyword)");
            return;
        }

        String searchTerm = argument.trim();

        try {
            LocalDate searchDate = LocalDate.parse(searchTerm);
            findByDate(tasks, ui, searchDate);
        } catch (DateTimeParseException e) {
            findByKeyword(tasks, ui, searchTerm);
        }
    }

    /**
     * Executes the find command for GUI mode and returns the results.
     *
     * @param tasks the task list to search
     * @param storage the storage (not used)
     * @return the search results as a formatted string
     */
    @Override
    public String executeForGui(TaskList tasks, Storage storage) {
        if (argument == null || argument.trim().isEmpty()) {
            return "What should I search fur? (date or keyword)";
        }

        String searchTerm = argument.trim();

        try {
            LocalDate searchDate = LocalDate.parse(searchTerm);
            return findByDateGui(tasks, searchDate);
        } catch (DateTimeParseException e) {
            return findByKeywordGui(tasks, searchTerm);
        }
    }

    /**
     * Finds tasks on a specific date.
     */
    private void findByDate(TaskList tasks, Ui ui, LocalDate searchDate) {
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");

        ui.showFindByDateHeader(searchDate.format(displayFormat));

        boolean foundDeadlines = false;
        boolean foundEvents = false;
        int count = 0;

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
    }

    /**
     * Finds tasks on a specific date for GUI mode.
     */
    private String findByDateGui(TaskList tasks, LocalDate searchDate) {
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");
        StringBuilder response = new StringBuilder();

        response.append("Searching fur tasks on ").append(searchDate.format(displayFormat)).append("...\n\n");

        boolean foundDeadlines = false;
        boolean foundEvents = false;
        int deadlineCount = 0;
        int eventCount = 0;

        // Find deadlines
        StringBuilder deadlineResults = new StringBuilder();
        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task task = tasks.getTask(i);

            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getDeadline().toLocalDate().equals(searchDate)) {
                    if (!foundDeadlines) {
                        foundDeadlines = true;
                    }
                    deadlineResults.append((++deadlineCount)).append(". ").append(task).append("\n");
                }
            }
        }

        // Find events
        StringBuilder eventResults = new StringBuilder();
        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task task = tasks.getTask(i);

            if (task instanceof Event) {
                Event event = (Event) task;
                if (event.getStart().toLocalDate().equals(searchDate)) {
                    if (!foundEvents) {
                        foundEvents = true;
                    }
                    eventResults.append((++eventCount)).append(". ").append(task).append("\n");
                }
            }
        }

        // Build response
        if (foundDeadlines) {
            response.append("Deadlines on this date:\n");
            response.append(deadlineResults);
        }

        if (foundEvents) {
            if (foundDeadlines) {
                response.append("\n");
            }
            response.append("Events on this date:\n");
            response.append(eventResults);
        }

        if (!foundDeadlines && !foundEvents) {
            response.append("No tasks found on this date. Time fur a cat nap!");
        }

        return response.toString().trim();
    }

    /**
     * Finds tasks containing the keyword in their description.
     */
    private void findByKeyword(TaskList tasks, Ui ui, String keyword) {
        ui.showFindByKeywordHeader(keyword);

        boolean foundAny = false;
        int count = 0;

        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task task = tasks.getTask(i);

            if (task.getUserTask().toLowerCase().contains(keyword.toLowerCase())) {
                foundAny = true;
                System.out.println((++count) + "." + task);
            }
        }

        if (!foundAny) {
            ui.showNoMatchingTasks(keyword);
        }
    }

    /**
     * Finds tasks containing the keyword in their description for GUI mode.
     */
    private String findByKeywordGui(TaskList tasks, String keyword) {
        StringBuilder response = new StringBuilder();
        response.append("Here are the matching tasks in your list:\n\n");

        boolean foundAny = false;
        int count = 0;

        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task task = tasks.getTask(i);

            if (task.getUserTask().toLowerCase().contains(keyword.toLowerCase())) {
                foundAny = true;
                response.append((++count)).append(".").append(task).append("\n");
            }
        }

        if (!foundAny) {
            return "No tasks found matching '" + keyword + "'. Meow-be try another keyword?";
        }

        return response.toString().trim();
    }
}