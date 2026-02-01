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
        String additionMessage = ("Nya-ice! I've added: " + argument);
        if (argument.startsWith("todo ")) {
            ToDo todo = new ToDo(argument.substring(5).trim());
            taskList.addTask(todo);
            System.out.println(additionMessage);

        } else if (argument.startsWith("deadline ")) {
            String[] parts = argument.substring(9).split(" /by ", 2);
            Deadline deadline = new Deadline(parts[0].trim(), parts[1].trim());
            taskList.addTask(deadline);
            System.out.println(additionMessage);

        } else if (argument.startsWith("event ")) {
            String[] parts = argument.substring(6).split(" /from | /to ");
            Event event = new Event(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim()
            );
            taskList.addTask(event);
            System.out.println(additionMessage);
        }
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

    /**
     * Prints the list of available commands for the user.
     * This message is shown once at the start of the chatbot session.
     */
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
