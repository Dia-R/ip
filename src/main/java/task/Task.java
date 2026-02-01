package task;
/**
 * Represents a task provided by a user.
 * Stores the task description and allows access to it.
 */
public abstract class Task {
    private final String userTaskDescription;
    private boolean isDone;

    /**
     * Creates a Task with the given description.
     *
     * @param userTask Description of the task.
     */
    public Task (String userTask){
        this.userTaskDescription = userTask;
    }

    public String getType() {
        return null;
    }

    /**
     * Returns the description of the user's task.
     *
     * @return userTaskDescription.
     */
    public String getUserTask() {
        return userTaskDescription;
    }

    /**
     * Returns the status and description of the task.
     *
     * @return a string containing the status and description of the task.
     */
    @Override
    public String toString() {
        return "[" + getType() + "][" + (isDone ? "X" : " ") + "] " + userTaskDescription;
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
     * @return true is the task is completed and false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

}

