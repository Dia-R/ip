package task;
/**
 * Represents a fixed-size list of tasks.
 * Stores up to 100 Task objects and tracks the number of tasks currently stored.
 */
public class TaskList {
    private Task[] allTasks;
    private int taskCount;

    /**
     * Creates an empty TaskList with a maximum capacity of 100 tasks.
     */
    public TaskList() {
        this.allTasks = new Task[100];
        this.taskCount = 0;
    }

    /**
     * Adds a new task to the task list if there is available capacity.
     *
     * @param taskDescription The description of the task to be added.
     */
    public void addTask(String taskDescription) {
        if (taskCount < 100) {
            allTasks[taskCount] = new Task(taskDescription);
            taskCount++;
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

    public String[] getAllTasks() {
        String[] totalTasks = new String[taskCount];
        for (int i = 0; i < taskCount; i++) {
            totalTasks[i] = (i + 1) + "." + allTasks[i].toString();
        }

        return totalTasks;
    }

    public boolean isEmpty() {
        return taskCount == 0;
    }
}
