package logic;

import parser.CommandParser;
import parser.ParsedCommand;
import storage.Storage;
import storage.StorageException;
import task.Task;
import task.TaskList;
import task.ToDo;
import task.Deadline;
import task.Event;
import app.Ui;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Chatbot that manages tasks set by a user.
 * Tasks are automatically saved to and loaded from disk.
 * Supports date and time parsing for deadlines and events.
 */
public class ChatBot {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a ChatBot with the specified file path.
     * Loads existing tasks from storage if available.
     *
     * @param filePath path to the data file
     */
    public ChatBot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            Task[] loadedTasks = new Task[100];
            int count = storage.load(loadedTasks);
            tasks = new TaskList();
            for (int i = 0; i < count; i++) {
                tasks.addTask(loadedTasks[i]);
            }
            ui.showTasksLoaded(count);
        } catch (StorageException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main chatbot loop.
     */
    public void run() {
        ui.showWelcome();
        ui.showInstructions();

        boolean isRunning = true;
        while (isRunning) {
            String userCommand = ui.readCommand();

            // Skip empty lines
            if (userCommand == null || userCommand.trim().isEmpty()) {
                continue;
            }

            ParsedCommand command = CommandParser.parse(userCommand);

            switch (command.getType()) {
                case Bye:
                    isRunning = false;
                    break;

                case List:
                    executeList();
                    break;

                case Add:
                    executeAdd(command.getArgument());
                    break;

                case Mark:
                    executeMark(command.getArgument());
                    break;

                case Unmark:
                    executeUnmark(command.getArgument());
                    break;

                case Delete:
                    executeDelete(command.getArgument());
                    break;

                case Find:
                    executeFind(command.getArgument());
                    break;
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    private void executeList() {
        ui.showTaskList(tasks.getAllTasks());
    }

    private void executeAdd(String argument) {
        try {
            if (argument.startsWith("todo")) {
                handleTodo(argument);
            } else if (argument.startsWith("deadline")) {
                handleDeadline(argument);
            } else if (argument.startsWith("event")) {
                handleEvent(argument);
            } else {
                ui.showError("Wait a meow-nute... You've got me feeling purr-plexed...");
            }
        } catch (Exception e) {
            ui.showError("Something went cat-astrophically wrong: " + e.getMessage());
        }
    }

    private void handleTodo(String argument) {
        String desc = argument.length() > 4 ? argument.substring(4).trim() : "";
        if (desc.isEmpty()) {
            ui.showError("Nyat today! Give me a description too please!");
            return;
        }
        ToDo todo = new ToDo(desc);
        tasks.addTask(todo);
        saveTasks();
        ui.showTaskAdded(todo, tasks.getTaskCount());
    }

    private void handleDeadline(String argument) {
        String deadlineArgs = argument.length() > 8 ? argument.substring(8).trim() : "";
        String[] parts = deadlineArgs.split(" /by ", 2);

        String description = parts.length > 0 ? parts[0].trim() : "";
        String by = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty() || by.isEmpty()) {
            ui.showDeadlineFormatHelp();
            return;
        }

        try {
            Deadline deadline = Deadline.createFromString(description, by);
            tasks.addTask(deadline);
            saveTasks();
            ui.showTaskAdded(deadline, tasks.getTaskCount());
        } catch (DateTimeParseException e) {
            ui.showDateFormatError();
        }
    }

    private void handleEvent(String argument) {
        String eventArgs = argument.length() > 5 ? argument.substring(5).trim() : "";

        int fromIndex = eventArgs.indexOf(" /from ");
        if (fromIndex == -1) {
            ui.showEventFormatHelp();
            return;
        }

        String description = eventArgs.substring(0, fromIndex).trim();
        String timeString = eventArgs.substring(fromIndex + 7);

        int toIndex = timeString.indexOf(" /to ");
        if (toIndex == -1) {
            ui.showEventFormatHelp();
            return;
        }

        String start = timeString.substring(0, toIndex).trim();
        String end = timeString.substring(toIndex + 5).trim();

        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            ui.showEventFormatHelp();
            return;
        }

        try {
            Event event = Event.createFromString(description, start, end);
            tasks.addTask(event);
            saveTasks();
            ui.showTaskAdded(event, tasks.getTaskCount());
        } catch (DateTimeParseException e) {
            ui.showDateFormatError();
        }
    }

    private void executeMark(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("Meow? Which task do you want to mark?");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task task = tasks.markTask(taskNumber);
            saveTasks();
            ui.showTaskMarked(task);
        } catch (NumberFormatException e) {
            ui.showError("That doesn't look like a number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("No such task to mark, meow!");
        }
    }

    private void executeUnmark(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("Meow? Which task do you want to unmark?");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task task = tasks.unmarkTask(taskNumber);
            saveTasks();
            ui.showTaskUnmarked(task);
        } catch (NumberFormatException e) {
            ui.showError("That doesn't look like a number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("No such task to unmark, meow!");
        }
    }

    private void executeDelete(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            ui.showError("You didn't tell ME-ow which task to delete!");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task removedTask = tasks.deleteTask(taskNumber);
            saveTasks();
            ui.showTaskDeleted(removedTask, tasks.getTaskCount());
        } catch (NumberFormatException e) {
            ui.showError("That's not a valid task number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("No task with that number, meow!");
        } catch (Exception e) {
            ui.showError("Something went cat-astrophically wrong: " + e.getMessage());
        }
    }

    private void executeFind(String argument) {
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

    private void saveTasks() {
        try {
            Task[] taskArray = new Task[tasks.getTaskCount()];
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                taskArray[i] = tasks.getTask(i);
            }
            storage.save(taskArray, tasks.getTaskCount());
        } catch (StorageException e) {
            ui.showError("HISS! I failed to save tasks: " + e.getMessage());
        }
    }
}