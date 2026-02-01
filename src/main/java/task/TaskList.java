package task;
/**
 * Represents a fixed-size list of tasks.
 * Stores up to 100 Task objects and tracks the number of tasks currently stored.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private Task[] allTasks;
    private int taskCount;

    /**
     * Creates an empty TaskList with a maximum capacity of 100 tasks.
     */
    public TaskList() {
        this.allTasks = new Task[MAX_TASKS];
        this.taskCount = 0;
    }

    /**
     * Adds a new task to the task list if there is available capacity.
     *
     * @param task The task to be added.
     * @throws IllegalStateException if the task list is full.
     */
    public void addTask(Task task) {
        if (taskCount < MAX_TASKS) {
            allTasks[taskCount++] = task;
        } else {
            throw new IllegalStateException("The task list is full.");
        }
    }

    /**
     * Returns the task stored at the specified index.
     *
     * @param index The position of task.
     * @return The task at the given index.
     */
    public Task getTask(int index) {
        if (index < 0 || index >= taskCount) {
            throw new IndexOutOfBoundsException("This task does not exist.");
        }
        return allTasks[index];
    }

    /**
     * Returns all tasks in the list as an array of strings,
     * with each task numbered starting from 1.
     *
     * @return an array of strings representing all tasks with numbering
     *
     */
    public String[] getAllTasks() {
        String[] totalTasks = new String[taskCount];
        for (int i = 0; i < taskCount; i++) {
            totalTasks[i] = (i + 1) + "." + allTasks[i].toString();
        }

        return totalTasks;
    }

    /**
     * Checks whether the task list is empty.
     *
     * @return true if there are no tasks in the list, false otherwise
     */
    public boolean isEmpty() {
        return taskCount == 0;
    }

    /**
     * Marks task as done.
     *
     * @param taskNumber 1-based task index as seen by the user.
     * @return task
     */
    public Task markTask(int taskNumber) {
        Task task = getTask(taskNumber - 1);
        task.markDone();
        return task;
    }

    /**
     * Marks task as not done.
     *
     * @param taskNumber 1-based task index as seen by the user.
     * @return task
     */
    public Task unmarkTask(int taskNumber) {
        Task task = getTask(taskNumber - 1);
        task.unmarkDone();
        return task;
    }

    public int getTaskCount() {
        return taskCount;
    }

    /**
     * Deletes a task from the task list.
     *
     * @param taskNumber 1-based index of the task to delete.
     * @return the task that was removed.
     */
    public Task deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new IndexOutOfBoundsException("No such task to delete.");
        }

        Task removedTask = allTasks[taskNumber - 1];
        for (int i = taskNumber - 1; i < taskCount - 1; i++) {
            allTasks[i] = allTasks[i + 1];
        }
        allTasks[taskCount - 1] = null;
        taskCount--;

        return removedTask;
    }
}
