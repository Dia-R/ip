package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event task, which has a start and end time.
 * An Event is a type of Task that occurs within a specific time frame.
 */
public class Event extends Task {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructs an Event with the given description, start time, and end time.
     *
     * @param userTaskDescription the description of the event
     * @param start the start date and time of the event
     * @param end the end date and time of the event
     */
    public Event(String userTaskDescription, LocalDateTime start, LocalDateTime end) {
        super(userTaskDescription);

        assert start != null : "Start time should not be null";
        assert end != null : "End time should not be null";
        
        this.start = start;
        this.end = end;
    }

    /**
     * Creates an Event by parsing date-time strings.
     *
     * @param description the description of the event
     * @param startStr the start time string in format "yyyy-MM-dd HHmm"
     * @param endStr the end time string in format "yyyy-MM-dd HHmm"
     * @return a new Event object
     * @throws java.time.format.DateTimeParseException if the format is invalid
     */
    public static Event createFromString(String description, String startStr, String endStr) {
        LocalDateTime parsedStart = LocalDateTime.parse(startStr, INPUT_FORMAT);
        LocalDateTime parsedEnd = LocalDateTime.parse(endStr, INPUT_FORMAT);
        return new Event(description, parsedStart, parsedEnd);
    }

    /**
     * Returns the task type as Event.
     *
     * @return the TaskType enum value Event.
     */
    @Override
    public TaskType getType() {
        return TaskType.Event;
    }

    /**
     * Returns the start date and time of the event.
     *
     * @return Start {@code LocalDateTime}
     */
    public LocalDateTime getStart() {
        return start;
    }


    /**
     * Returns the end date and time of the event.
     *
     * @return End {@code LocalDateTime}
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Returns the start time formatted for storage.
     *
     * @return start time in "yyyy-MM-dd HHmm" format
     */
    public String getStorageStart() {
        return start.format(INPUT_FORMAT);
    }

    /**
     * Returns the end time formatted for storage.
     *
     * @return end time in "yyyy-MM-dd HHmm" format
     */
    public String getStorageEnd() {
        return end.format(INPUT_FORMAT);
    }

    /**
     * Returns a string representation of the event, including its description,
     * completion status, and start and end times.
     *
     * @return a formatted string describing the event
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + start.format(OUTPUT_FORMAT)
                + " to: " + end.format(OUTPUT_FORMAT) + ")";
    }
}