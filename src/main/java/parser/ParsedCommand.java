package parser;
/**
 * Represents a parsed user command consisting of a command type
 * and an optional argument extracted from user input.
 */
public class ParsedCommand {
    private CommandParser.CommandType type;
    private String argument;

    /**
     * Creates a ParsedCommand with the specified command type
     * and associated argument.
     *
     * @param type
     * @param argument
     */
    public ParsedCommand(CommandParser.CommandType type, String argument) {
        this.type = type;
        this.argument = argument;
    }

    public CommandParser.CommandType getType() {
        return type;
    }

    public String getArgument() {
        return argument;
    }

    /**
     * Returns whether the parsed command contains a non-empty argument.
     *
     * @return True if an argument exists, false otherwise.
     */
    public boolean hasArgument() {
        return argument != null && !argument.isEmpty();
    }
}
