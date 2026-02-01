package task;

/**
 * Represents a task without a specific date or time.
 * A ToDo is a type of Task that only has a description and completion status.
 */
public class ToDo extends Task {

    /**
     * Creates a ToDo with the given description.
     *
     * @param userTaskDescription the description of the task.
     */
    public ToDo(String userTaskDescription) {
        super(userTaskDescription);
    }

    /**
     * Returns the type of this task as "ToDo".
     *
     * @return a string representing the task type.
     */
    @Override
    public String getType() {
        return "ToDo";
    }
}

