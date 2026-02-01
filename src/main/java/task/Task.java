package task;
/**
 * Represents a task provided by a user.
 * Stores the task description and allows access to it.
 */
public class Task {
    private String userTask;

    /**
     * Creates a Task with the given description.
     *
     * @param userTask Description of the task.
     */
    public Task (String userTask){
        this.userTask = userTask;
    }

    public String getUserTask() {
        return userTask;
    }

    @Override
    public String toString() {
        return userTask;
    }
}
