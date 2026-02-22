package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a specific date or time.
 * A Deadline is a type of Task with an associated due date or time.
 */
public class Deadline extends Task {
    private final LocalDateTime deadline;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Creates a Deadline with the given description and deadline.
     *
     * @param description the description of the task.
     * @param deadline the due date or time for the task.
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        assert deadline != null : "Deadline date should not be null";
        this.deadline = deadline;
    }

    /**
     * Creates a Deadline by parsing a date-time string.
     *
     * @param description the description of the task.
     * @param deadlineStr the deadline string in format "yyyy-MM-dd HHmm".
     * @return a new Deadline object.
     * @throws java.time.format.DateTimeParseException if the format is invalid.
     */
    public static Deadline createFromString(String description, String deadlineStr) {
        LocalDateTime parsedDate = LocalDateTime.parse(deadlineStr, INPUT_FORMAT);
        return new Deadline(description, parsedDate);
    }

    /**
     * Returns the type of this task as "Deadline".
     *
     * @return a string representing the task type.
     */
    @Override
    public TaskType getType() {
        return TaskType.Deadline;
    }

    /**
     * Returns the deadline formatted for storage.
     *
     * @return deadline in "yyyy-MM-dd HHmm" format
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Returns the deadline formatted for storage.
     *
     * @return deadline in "yyyy-MM-dd HHmm" format
     */
    public String getStorageDeadline() {
        return deadline.format(INPUT_FORMAT);
    }

    /**
     * Returns a string representation of the deadline task, including
     * its completion status, description, and due date.
     *
     * @return a formatted string describing the deadline task.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline.format(OUTPUT_FORMAT) + ")";
    }
}

