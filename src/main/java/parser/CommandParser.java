package parser;
/**
 * Parses user input strings into structured commands
 * that can be executed by the chatbot.
 */
public class CommandParser {

    /**
     * Represents the supported types of user commands.
     */
    public enum CommandType {
        Bye, List, Add
    }

    /**
     * Returns a ParsedCommand representing the type of command
     * and any associated argument extracted from the given user input.
     *
     * @param userCommand Raw command input by user.
     * @return ParseCommand containing the command type and argument.
     */
    public static ParsedCommand parse(String userCommand) {
        userCommand = userCommand.trim();

        if (userCommand.equalsIgnoreCase("bye")) {
            return new ParsedCommand(CommandType.Bye, "");
        } else if (userCommand.equalsIgnoreCase("list")) {
            return new ParsedCommand(CommandType.List, "");
        } else {
            return new ParsedCommand(CommandType.Add, userCommand);
        }
    }
}
