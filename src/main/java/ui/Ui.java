package ui;

import task.Task;
import java.util.Scanner;

/**
 * Handles all interactions with the user.
 * Responsible for reading user input and displaying messages.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Creates a Ui object and initializes the scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message to the user.
     */
    public void showWelcome() {
        System.out.println("Hello, furrr-iend! Do you need a helping paw?");
    }

    /**
     * Displays the goodbye message to the user.
     */
    public void showGoodbye() {
        System.out.println("Aww, see mew next time!");
    }

    /**
     * Displays the instructions for using the chatbot.
     */
    public void showInstructions() {
        System.out.println("Here's what CatBot can do for you:");
        System.out.println();
        System.out.println("  ADDING TASKS");
        System.out.println("• todo <description>                                    — adds a task with no date");
        System.out.println("• deadline <description> /by <yyyy-MM-dd HHmm>         — adds a task with a due date");
        System.out.println("• event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>  — adds a task with a time range");
        System.out.println();
        System.out.println("  MANAGING TASKS");
        System.out.println("• list                      — shows all your tasks");
        System.out.println("• mark <task number>        — marks a task as done");
        System.out.println("• unmark <task number>      — marks a task as not done");
        System.out.println("• delete <task number>      — removes a single task");
        System.out.println("• clear                     — removes ALL tasks at once");
        System.out.println("• note <task number> <text> — attaches a note to a task");
        System.out.println();
        System.out.println("  SEARCHING");
        System.out.println("• find <keyword>            — finds tasks matching a keyword");
        System.out.println("• find <yyyy-MM-dd>         — finds tasks on a specific date");
        System.out.println();
        System.out.println("  OTHER");
        System.out.println("• cheer                     — get some encouragement!");
        System.out.println("• bye                       — exits the application");
        System.out.println();
    }

    /**
     * Displays a loading error message.
     */
    public void showLoadingError() {
        System.out.println("Meow! Couldn't load saved tasks!");
        System.out.println("Starting with an empty task list...");
    }

    /**
     * Displays a message when tasks are successfully loaded.
     *
     * @param count the number of tasks loaded
     */
    public void showTasksLoaded(int count) {
        if (count > 0) {
            System.out.println("Purr-fect! I found " + count + " saved task(s)!");
        }
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks array of task strings to display
     */
    public void showTaskList(String[] tasks) {
        if (tasks.length == 0) {
            System.out.println("Litter box is empty...");
        } else {
            System.out.println("All your work is tiring ME-OWT! Take a look...");
            for (String task : tasks) {
                System.out.println(task);
            }
            System.out.println();
        }
    }

    /**
     * Displays a message when no matching tasks are found (keyword search).
     *
     * @param keyword the keyword that was searched
     */
    public void showNoMatchingTasks(String keyword) {
        System.out.println("No tasks found matching '" + keyword + "'. Meow-be try another keyword?");
    }

    /**
     * Displays a message when a task is added.
     *
     * @param task the task that was added
     * @param taskCount the total number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Nya-ice! I've added: " + task);
        System.out.println(getTunaMessage(taskCount));
    }

    /**
     * Displays a message when a task is marked as done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("You're pawsitively efficient! This task has been marked as done:");
        System.out.println(task);
    }

    /**
     * Displays a message when a task is unmarked.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("I was looking forward to a cat nap... but this task is not done yet:");
        System.out.println(task);
    }

    /**
     * Displays a message when a task is deleted.
     *
     * @param task the task that was deleted
     * @param taskCount the remaining number of tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("A smart kitty has removed this task:");
        System.out.println(task);
        System.out.println(getTunaMessage(taskCount));
    }

    /**
     * Displays error messages.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays format help for deadline command.
     */
    public void showDeadlineFormatHelp() {
        System.out.println("Aren't you furrgetting something? Please provide description and a due date!");
        System.out.println("Format: deadline <description> /by <yyyy-MM-dd HHmm>");
        System.out.println("Example: deadline return book /by 2024-12-02 1800");
    }

    /**
     * Displays format help for event command.
     */
    public void showEventFormatHelp() {
        System.out.println("Events need a description, start, and end time, meow...");
        System.out.println("Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        System.out.println("Example: event project meeting /from 2024-08-06 1400 /to 2024-08-06 1600");
    }

    /**
     * Displays date format error message.
     */
    public void showDateFormatError() {
        System.out.println("Meow-ch! That date format doesn't look right!");
        System.out.println("Please use: yyyy-MM-dd HHmm (e.g., 2024-12-02 1800)");
    }

    /**
     * Displays find command date format error message.
     */
    public void showFindDateFormatError() {
        System.out.println("Meow-ch! That date format doesn't look right!");
        System.out.println("Please use: yyyy-MM-dd (e.g., 2024-12-02)");
    }

    /**
     * Displays the header for find results.
     *
     * @param dateString the formatted date being searched
     */
    public void showFindHeader(String dateString) {
        System.out.println("Searching fur tasks on " + dateString + "...");
    }

    /**
     * Displays the deadline section header for find results.
     */
    public void showDeadlineSection() {
        System.out.println("\nDeadlines on this date:");
    }

    /**
     * Displays the event section header for find results.
     */
    public void showEventSection() {
        System.out.println("\nEvents on this date:");
    }

    /**
     * Displays a message when no tasks are found.
     */
    public void showNoTasksFound() {
        System.out.println("No tasks found on this date. Time fur a cat nap!");
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the user's input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Returns a tuna-themed message about the task count.
     *
     * @param taskCount the number of tasks
     * @return a message about the task count
     */
    private String getTunaMessage(int taskCount) {
        return "If I had a can of tuna for every task you have to do, I'd have... "
                + taskCount + ". Yum!";
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays the header for find by keyword results.
     *
     * @param keyword the keyword being searched
     */
    public void showFindByKeywordHeader(String keyword) {
        System.out.println("Here are the matching tasks in your list:");
    }

    /**
     * Displays the header for find by date results.
     *
     * @param dateString the formatted date being searched
     */
    public void showFindByDateHeader(String dateString) {
        System.out.println("Searching fur tasks on " + dateString + "...");
    }
}