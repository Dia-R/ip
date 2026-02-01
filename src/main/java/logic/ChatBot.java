package logic;
import parser.CommandParser;
import parser.ParsedCommand;
import task.Task;
import task.TaskList;
import java.util.Scanner;
import task.ToDo;
import task.Deadline;
import task.Event;


/**
 * Represents a Chatbot that manages tasks set by a user.
 */
public class ChatBot {
    private String name;
    private boolean isRunning;
    private Scanner scanner;
    private TaskList taskList;
    private CommandParser parser;

    /**
     * Creates a ChatBot with the given name and initializes it to run.
     *
     * @param name Name of the ChatBot.
     */
    public ChatBot(String name) {
        this.name = name;
        this.isRunning = true;
        this.scanner = new Scanner(System.in);
        this.taskList = new TaskList();
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
            executeMark(Integer.parseInt(command.getArgument()));
            break;

        case Unmark:
            executeUnmark(Integer.parseInt(command.getArgument()));
            break;

        case Delete:
            executeDelete(command.getArgument());
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
        System.out.println("Nya-ice! I've added: " + argument);
    }

    private void handleDeadline(String argument) {
        String deadlineArgs = argument.length() > 8 ? argument.substring(8).trim() : "";
        String[] parts = deadlineArgs.split(" /by ", 2);

        String description = parts.length > 0 ? parts[0].trim() : "";
        String by = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty() || by.isEmpty()) {
            System.out.println("Aren't you furrgetting something? Please provide description and a due date!");
            return;
        }

        Deadline deadline = new Deadline(description, by);
        taskList.addTask(deadline);
        System.out.println("Nya-ice! I've added: " + argument);
    }

    private void handleEvent(String argument) {
        String eventArgs = argument.length() > 5 ? argument.substring(5).trim() : "";
        String[] parts = eventArgs.split(" /from | /to ");

        String description = parts.length > 0 ? parts[0].trim() : "";
        String start = parts.length > 1 ? parts[1].trim() : "";
        String end = parts.length > 2 ? parts[2].trim() : "";

        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            System.out.println("Events need a description, start, and end time, meow...");
            return;
        }

        Event event = new Event(description, start, end);
        taskList.addTask(event);
        System.out.println("Nya-ice! I've added: " + argument);
    }

    private void executeMark(int argument) {
        Task task = taskList.markTask(argument);
        System.out.println("You're pawsitively efficient! This task has been marked as done:");
        System.out.println(task);
    }

    private void executeUnmark(int argument) {
        Task task = taskList.unmarkTask(argument);
        System.out.println("I was looking forward to a cat nap... but this task is not done yet:");
        System.out.println(task);
    }

    private void executeDelete(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Oops! You didn’t tell me which task to delete, meow!");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument.trim());
            Task removedTask = taskList.deleteTask(taskNumber);
            System.out.println("Noted. I've removed this task:");
            System.out.println(removedTask);
        } catch (NumberFormatException e) {
            System.out.println("That’s not a valid task number, furriend!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No task with that number, meow!");
        } catch (Exception e) {
            System.out.println("Something went cat-astrophically wrong: " + e.getMessage());
        }
    }

    private void printInstructions() {
        System.out.println("Here’s what CatBot can do for you:");
        System.out.println("• todo <description>");
        System.out.println("• deadline <description> /by <date>");
        System.out.println("• event <description> /from <start> /to <end>");
        System.out.println("• list");
        System.out.println("• mark <task number>");
        System.out.println("• unmark <task number>");
        System.out.println("• bye");
        System.out.println();
    }

}
