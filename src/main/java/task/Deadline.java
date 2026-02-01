package task;

/**
 * Represents a task that must be completed by a specific date or time.
 * A Deadline is a type of Task with an associated due date or time.
 */
public class Deadline extends Task {
    private final String deadline;

    /**
     * Creates a Deadline with the given description and deadline.
     *
     * @param description the description of the task.
     * @param deadline the due date or time for the task.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Returns the type of this task as "Deadline".
     *
     * @return a string representing the task type.
     */
    @Override
    public String getType() { return "Deadline"; }

    /**
     * Returns a string representation of the deadline task, including
     * its completion status, description, and due date.
     *
     * @return a formatted string describing the deadline task.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline + ")";
    }
}

