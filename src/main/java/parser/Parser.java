package parser;

import commands.Command;
import commands.AddCommand;
import commands.DeleteCommand;
import commands.ExitCommand;
import commands.ListCommand;
import commands.MarkCommand;
import commands.UnmarkCommand;
import commands.FindCommand;

/**
 * Parses user input strings into Command objects that can be executed.
 */
public class Parser {

    /**
     * Parses the user input and returns the corresponding Command.
     *
     * @param userInput the raw user input string
     * @return a Command object representing the user's intent
     */
    public static Command parse(String userInput) {
        String trimmedInput = userInput.trim();
        String lowerCaseInput = trimmedInput.toLowerCase();

        if (lowerCaseInput.startsWith("bye")) {
            return new ExitCommand();
        } else if (lowerCaseInput.startsWith("list")) {
            return new ListCommand();
        } else if (lowerCaseInput.startsWith("mark ")) {
            String arg = trimmedInput.substring(5).trim();
            return new MarkCommand(arg);
        } else if (lowerCaseInput.startsWith("unmark ")) {
            String arg = trimmedInput.substring(7).trim();
            return new UnmarkCommand(arg);
        } else if (lowerCaseInput.startsWith("delete ")) {
            String arg = trimmedInput.substring(7).trim();
            return new DeleteCommand(arg);
        } else if (lowerCaseInput.startsWith("find ")) {
            String arg = trimmedInput.substring(5).trim();
            return new FindCommand(arg);
        } else if (lowerCaseInput.startsWith("cheer")) {
            return new CheerCommand();
        } else {
            return new AddCommand(lowerCaseInput);
        }
    }
}