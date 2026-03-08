package task;

/**
 * Represents a task provided by a user.
 * Stores the task description, completion status, and an optional note.
 */
public abstract class Task {
    private final String userTaskDescription;
    private boolean isDone;
    private String notes;

    /**
     * Creates a Task with the given description.
     *
     * @param userTask Description of the task.
     */
    public Task(String userTask) {
        this.userTaskDescription = userTask;
        this.notes = "";
    }

    /**
     * Returns the type of this task.
     *
     * @return the TaskType enum of this task
     */
    public abstract TaskType getType();

    /**
     * Returns the description of the user's task.
     *
     * @return userTaskDescription.
     */
    public String getUserTask() {
        return userTaskDescription;
    }

    /**
     * Returns the optional note attached to this task.
     *
     * @return the note string, or an empty string if none is set.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the note for this task.
     *
     * @param notes the note to attach; may be empty but not null.
     */
    public void setNotes(String notes) {
        assert notes != null : "Notes should not be null (use empty string instead)";
        this.notes = notes;
    }

    /**
     * Returns whether this task has a note attached.
     *
     * @return true if a non-empty note exists.
     */
    public boolean hasNotes() {
        return notes != null && !notes.isEmpty();
    }

    /**
     * Returns the status and description of the task,
     * including the note on a new line if one is set.
     *
     * @return a formatted string describing the task.
     */
    @Override
    public String toString() {
        String base = (isDone ? "[X] " : "[ ] ") + userTaskDescription;
        if (hasNotes()) {
            base += "\n   Note: " + notes;
        }
        return base;
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void unmarkDone() {
        isDone = false;
    }

    /**
     * Returns whether the task is completed.
     *
     * @return true if the task is completed and false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }
}