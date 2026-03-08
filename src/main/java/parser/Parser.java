package parser;

import commands.*;

/**
 * Parses user input strings into Command objects that can be executed.
 */
public class Parser {

    private static final int MAX_SPLITS = 2;

    /**
     * Parses the user input and returns the corresponding Command.
     *
     * @param userInput the raw user input string
     * @return a Command object representing the user's intent
     */
    public static Command parse(String userInput) {
        assert userInput != null : "userInput should not be null";

        String trimmedInput = userInput.trim();
        if (trimmedInput.isEmpty()) {
            return new AddCommand(trimmedInput);
        }

        String[] tokens = trimmedInput.split("\\s+", MAX_SPLITS);
        String keyword = tokens[0].toLowerCase();
        String args = tokens.length == MAX_SPLITS ? tokens[1].trim() : "";

        switch (keyword) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(args);
        case "unmark":
            return new UnmarkCommand(args);
        case "delete":
            return new DeleteCommand(args);
        case "find":
            return new FindCommand(args);
        case "note":
            return new NoteCommand(args);
        case "cheer":
            return new CheerCommand();
        case "clear":
            return new ClearCommand();
        default:
            return new AddCommand(trimmedInput);
        }
    }
}