package task;


/**
 * Represents an Event task, which has a start and end time.
 * An Event is a type of Task that occurs within a specific time frame.
 */
public class Event extends Task {
    private final String start;
    private final String end;

    /**
     * Constructs an Event with the given user description, start time, and end time.
     *
     * @param userTaskDescription the description of the event
     * @param start the start time of the event
     * @param end the end time of the event
     */
    public Event(String userTaskDescription, String start, String end) {
        super(userTaskDescription);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the task type as Event.
     *
     * @return the string "Event".
     */
    @Override
    public String getType() { return "Event"; }

    /**
     * Returns a string representation of the event, including its description,
     * completion status, and start and end times.
     *
     * @return a formatted string describing the event
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + start + " to: " + end + ")";
    }
}
