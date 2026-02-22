package logic;

import commands.Command;

import parser.Parser;

import storage.Storage;
import storage.StorageException;

import task.Task;
import task.TaskList;
import ui.Ui;

/**
 * Represents a Chatbot that manages tasks using the Command pattern.
 * Each user action is represented as a Command object that can be executed.
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
        assert filePath != null : "File path should not be null";
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
        assert tasks != null : "Task list should be initialized";
    }

    /**
     * Runs the main chatbot loop using the Command pattern.
     * Reads user input, parses it into a Command, and executes it.
     */
    public void run() {
        ui.showWelcome();
        ui.showInstructions();

        boolean isRunning = true;
        while (isRunning) {
            try {
                String userInput = ui.readCommand();

                if (userInput == null || userInput.trim().isEmpty()) {
                    continue;
                }

                Command command = Parser.parse(userInput);

                command.execute(tasks, ui, storage);

                if (command.isExit()) {
                    isRunning = false;
                }

            } catch (Exception e) {
                ui.showError("Something went cat-astrophically wrong: " + e.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Generates a response for the user's chat message for GUI mode.
     *
     * @param input the user's input string
     * @return the chatbot's response
     */
    public String getResponse(String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                return "Meow? Did you say something?";
            }

            Command command = Parser.parse(input);
            return command.executeForGui(tasks, storage);

        } catch (Exception e) {
            return "Something went cat-astrophically wrong: " + e.getMessage();
        }
    }
}