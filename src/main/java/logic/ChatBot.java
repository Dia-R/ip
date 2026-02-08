package logic;

import parser.CommandParser;
import parser.ParsedCommand;
import storage.Storage;
import storage.StorageException;
import task.Task;
import task.TaskList;
import java.util.Scanner;
import task.ToDo;
import task.Deadline;
import task.Event;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Represents a Chatbot that manages tasks set by a user.
 */
public class ChatBot {
    private String name;
    private boolean isRunning;
    private Scanner scanner;
    private TaskList taskList;
    private CommandParser parser;
    private Storage storage;

    /**
     * Creates a ChatBot with the given name and initializes it to run.
     * Saves and loads tasks to and from the disk.
     *
     * @param name Name of the ChatBot.
     */
    public ChatBot(String name) {
        this.name = name;
        this.isRunning = true;
        this.scanner = new Scanner(System.in);
        this.taskList = new TaskList();

        String filePath = "." + File.separator + "data" + File.separator + "Cat.txt";
        this.storage = new Storage(filePath);
        loadTasks();
    }

    private void loadTasks() {
        try {
            Task[] tempTasks = new Task[100];
            int count = storage.load(tempTasks);
            for (int i = 0; i < count; i++) {
                taskList.addTask(tempTasks[i]);
            }
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveTasks() {
        try {
            Task[] tasks = new Task[taskList.getTaskCount()];
            for (int i = 0; i < taskList.getTaskCount(); i++) {
                tasks[i] = taskList.getTask(i);
            }
            storage.save(tasks, taskList.getTaskCount());
        } catch (StorageException e) {
            System.out.println("Purr-retty sure I failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Starts the chatbot interaction loop to read, process and
     * execute user commands.
     * Terminates the chatbot when an exit command is received.
     */
    public void run(){
        helloUser();
        printInstructions();
        while (isRunning) {
            String userCommand = scanner.nextLine();
            executeCommand(userCommand);
        }
        byeUser();
    }

    private void helloUser() {
        String hello = "Hello, furrr-iend! Do you need a helping paw?";
        System.out.println(hello);
    }
    private void byeUser() {
        String hello = "Aww, see mew next time!";
        System.out.println(hello);
    }

    private void executeCommand(String userCommand) {
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

    private void executeList() {
        if (taskList.isEmpty()) {
            System.out.println("Litter box is empty...");
        }

        else {
            System.out.println("All your work is tiring ME-OWT! Take a look...");
            String[] tasks = taskList.getAllTasks();
            StringBuilder listedTasks = new StringBuilder();

            for (String task: tasks) {
                listedTasks.append(task).append("\n");
            }
            System.out.println(listedTasks.toString());
        }
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
                System.out.println("Wait a meow-nute... You've got me feeling purr-plexed...");
            }
        } catch (Exception e) {
            System.out.println("Something went cat-astrophically wrong: " + e.getMessage());
        }
    }

    private void handleTodo(String argument) {
        String desc = argument.length() > 4 ? argument.substring(4).trim() : "";
        if (desc.isEmpty()) {
            System.out.println("Nyat today! Give me a description too please!");
            return;
        }
        ToDo todo = new ToDo(desc);
        taskList.addTask(todo);
        saveTasks();
        System.out.println("Nya-ice! I've added: " + argument);
        System.out.println(tunaMessage(taskList.getTaskCount()));
    }

    private void handleDeadline(String argument) {
        String deadlineArgs = argument.length() > 8 ? argument.substring(8).trim() : "";
        String[] parts = deadlineArgs.split(" /by ", 2);

        String description = parts.length > 0 ? parts[0].trim() : "";
        String by = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty() || by.isEmpty()) {
            System.out.println("Aren't you furrgetting something? Please provide description and a due date in the correct format!");
            System.out.println("Example: deadline return book /by 2024-12-02 1800");
            return;
        }

        Deadline deadline = Deadline.createFromString(description, by);
        taskList.addTask(deadline);
        saveTasks();
        System.out.println("Nya-ice! I've added: " + argument);
        System.out.println(tunaMessage(taskList.getTaskCount()));
    }

    private void handleEvent(String argument) {
        String eventArgs = argument.length() > 5 ? argument.substring(5).trim() : "";

        int fromIndex = eventArgs.indexOf(" /from ");
        if (fromIndex == -1) {
            return;
        }

        String description = eventArgs.substring(0, fromIndex).trim();
        String timeString = eventArgs.substring(fromIndex + 7); // Skip " /from "

        int toIndex = timeString.indexOf(" /to ");
        if (toIndex == -1) {
            return;
        }

        String start = timeString.substring(0, toIndex).trim();
        String end = timeString.substring(toIndex + 5).trim(); // Skip " /to "

        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            System.out.println("Events need a description, start, and end time, meow...");
            return;
        }

        try {
            Event event = Event.createFromString(description, start, end);
            taskList.addTask(event);
            saveTasks();
            System.out.println("Nya-ice! I've added: " + event);
            System.out.println(tunaMessage(taskList.getTaskCount()));
        } catch (DateTimeParseException e) {
            System.out.println("Meow-ch! That date format doesn't look right!");
            System.out.println("Please use: yyyy-MM-dd HHmm (e.g., 2024-08-06 1400)");
        }
    }

    private void executeMark(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Meow? Which task do you want to mark?");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task task = taskList.markTask(taskNumber);
            saveTasks();
            System.out.println("You're pawsitively efficient! This task has been marked as done:");
            System.out.println(task);
        } catch (NumberFormatException e) {
            System.out.println("That doesn’t look like a number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No such task to mark, meow!");
        }
    }

    private void executeUnmark(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Meow? Which task do you want to unmark?");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task task = taskList.unmarkTask(taskNumber);
            saveTasks();
            System.out.println("I was looking forward to a cat nap... but this task is not done yet:");
            System.out.println(task);
        } catch (NumberFormatException e) {
            System.out.println("That doesn’t look like a number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No such task to unmark, meow!");
        }
    }

    private void executeDelete(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("You didn’t tell ME-ow which task to delete!");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task removedTask = taskList.deleteTask(taskNumber);
            saveTasks();
            System.out.println("A smart kitty has removed this task:");
            System.out.println(removedTask);
            System.out.println(tunaMessage(taskList.getTaskCount()));
        } catch (NumberFormatException e) {
            System.out.println("That’s not a valid task number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No task with that number, meow!");
        } catch (Exception e) {
            System.out.println("Something went cat-astrophically wrong: " + e.getMessage());
        }
    }

    private void executeFind(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Which date should I look fur?");
            return;
        }

        try {
            LocalDate searchDate = LocalDate.parse(argument.trim());
            DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");

            System.out.println("Digging up tasks on " + searchDate.format(displayFormat) + "...");

            boolean found = false;
            int count = 0;

            for (int i = 0; i < taskList.getTaskCount(); i++) {
                Task task = taskList.getTask(i);

                if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;
                    if (deadline.getDeadline().toLocalDate().equals(searchDate)) {
                        if (!found) {
                            System.out.println("\nDeadlines on this date:");
                            found = true;
                        }
                        System.out.println((++count) + ". " + task);
                    }
                } else if (task instanceof Event) {
                    Event event = (Event) task;
                    LocalDate eventDate = event.getStart().toLocalDate();
                    if (eventDate.equals(searchDate)) {
                        if (!found) {
                            System.out.println("\nEvents on this date:");
                            found = true;
                        } else if (count == 0) {
                            System.out.println("\nEvents on this date:");
                        }
                        System.out.println((++count) + ". " + task);
                    }
                }
            }

            if (!found) {
                System.out.println("No tasks found on this date. Take a cat nap!");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Meow-ch, that doesn't look right!");
            System.out.println("Please use yyyy-MM-dd (e.g., 2024-12-02)!!");
        }
    }

    private void printInstructions() {
        System.out.println("Here's what CatBot can do for you:");
        System.out.println("• todo <description>");
        System.out.println("• deadline <description> /by <yyyy-MM-dd HHmm>");
        System.out.println("• event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        System.out.println("• list");
        System.out.println("• mark <task number>");
        System.out.println("• unmark <task number>");
        System.out.println("• delete <task number>");
        System.out.println("• find <yyyy-MM-dd>");
        System.out.println("• bye");
        System.out.println();
    }

    private String tunaMessage(int taskCount) {
        return "If I had a can of tuna for every task you have to do, I'd have... "
                + taskCount + ". Yum!";
    }
}
