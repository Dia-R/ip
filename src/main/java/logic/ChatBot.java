package logic;
import parser.CommandParser;
import parser.ParsedCommand;
import task.TaskList;
import java.util.Scanner;

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
            executeAdd(userCommand);
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

    private void executeAdd(String userCommand) {
        taskList.addTask(userCommand);
        System.out.println("Nya-ice! I've added: " + userCommand);
    }
}
