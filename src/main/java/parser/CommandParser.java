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
        Bye, List, Add, Mark, Unmark, Delete
    }

    /**
     * Returns a ParsedCommand representing the type of command
     * and any associated argument extracted from the given user input.
     *
     * @param userCommand Raw command input by user.
     * @return ParseCommand containing the command type and argument.
     */
    public static ParsedCommand parse(String userCommand) {
        String lowerCaseUserCommand = userCommand.trim().toLowerCase();

        if (lowerCaseUserCommand.startsWith("bye")) {
            return new ParsedCommand(CommandType.Bye, "");
        } else if (lowerCaseUserCommand.startsWith("list")) {
            return new ParsedCommand(CommandType.List, "");
        } else if (lowerCaseUserCommand.startsWith("mark ")) {  // Note the space
            String arg = lowerCaseUserCommand.length() > 5
                    ? lowerCaseUserCommand.substring(5).trim() : "";
            return new ParsedCommand(CommandType.Mark, arg);
        } else if (lowerCaseUserCommand.startsWith("unmark ")) {
            String arg = lowerCaseUserCommand.length() > 7
                    ? lowerCaseUserCommand.substring(7).trim() : "";
            return new ParsedCommand(CommandType.Unmark, arg);
        } else if (lowerCaseUserCommand.startsWith("delete ")) {
            String arg = lowerCaseUserCommand.length() > 7
                    ? lowerCaseUserCommand.substring(7).trim() : "";
            return new ParsedCommand(CommandType.Delete, arg);
        } else {
            return new ParsedCommand(CommandType.Add, lowerCaseUserCommand);
        }
    }
}
