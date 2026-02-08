package commands;

import storage.Storage;
import task.TaskList;
import ui.Ui;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents a command to display a random motivational quote.
 */
public class CheerCommand extends Command {
    private static final String QUOTES_FILE = "/assets/cheer.txt";
    private static List<String> quotes = null;
    private static Random random = new Random();

    /**
     * Executes the cheer command by displaying a random motivational quote.
     *
     * @param tasks the task list (not used)
     * @param ui the UI to display messages
     * @param storage the storage (not used)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (quotes == null) {
            quotes = loadQuotes();
        }

        if (quotes.isEmpty()) {
            ui.showError("Meow! I couldn't find any motivational quotes!");
            return;
        }

        displayRandomQuote();
    }

    /**
     * Loads motivational quotes from the text file in resources.
     *
     * @return list of quotes
     */
    private List<String> loadQuotes() {
        List<String> loadedQuotes = new ArrayList<>();

        InputStream inputStream = CheerCommand.class.getResourceAsStream(QUOTES_FILE);
        if (inputStream == null) {
            System.err.println("Warning: Could not load " + QUOTES_FILE);
            return loadedQuotes;
        }

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    loadedQuotes.add(line);
                }
            }
        }

        return loadedQuotes;
    }

    /**
     * Displays a random quote with formatting.
     */
    private void displayRandomQuote() {
        String quote = quotes.get(random.nextInt(quotes.size()));

        List<String> lines = wordWrap(quote, 40);
        for (String line : lines) {
            System.out.println(line);
        }
    }

    /**
     * Word wraps text to fit within a specified width.
     *
     * @param text the text to wrap
     * @param maxWidth maximum characters per line
     * @return list of wrapped lines
     */
    private List<String> wordWrap(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }
}