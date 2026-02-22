import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import task.Event;
import task.TaskType;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * JUnit tests for Event class.
 * Tests event creation, date parsing, and time range handling.
 */
public class EventTest {
    private Event event;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    public void setUp() {
        startTime = LocalDateTime.of(2024, 12, 15, 14, 0);
        endTime = LocalDateTime.of(2024, 12, 15, 16, 0);
        event = new Event("team meeting", startTime, endTime);
    }

    @Test
    public void constructor_validInputs_createsEvent() {
        assertEquals("team meeting", event.getUserTask());
        assertEquals(TaskType.Event, event.getType());
        assertEquals(startTime, event.getStart());
        assertEquals(endTime, event.getEnd());
        assertFalse(event.isDone());
    }

    @Test
    public void createFromString_validFormat_success() {
        Event e = Event.createFromString("project meeting", 
                                         "2024-12-20 1400", 
                                         "2024-12-20 1600");
        
        assertEquals("project meeting", e.getUserTask());
        assertEquals(LocalDateTime.of(2024, 12, 20, 14, 0), e.getStart());
        assertEquals(LocalDateTime.of(2024, 12, 20, 16, 0), e.getEnd());
    }

    @Test
    public void createFromString_invalidFormat_throwsException() {
        assertThrows(DateTimeParseException.class, () -> {
            Event.createFromString("event", "invalid", "2024-12-15 1600");
        });
        
        assertThrows(DateTimeParseException.class, () -> {
            Event.createFromString("event", "2024-12-15 1400", "invalid");
        });
        
        assertThrows(DateTimeParseException.class, () -> {
            Event.createFromString("event", "2024-12-15", "2024-12-15 1600");
        });
    }

    @Test
    public void getStart_returnsCorrectDateTime() {
        assertEquals(startTime, event.getStart());
        assertEquals(14, event.getStart().getHour());
        assertEquals(0, event.getStart().getMinute());
    }

    @Test
    public void getEnd_returnsCorrectDateTime() {
        assertEquals(endTime, event.getEnd());
        assertEquals(16, event.getEnd().getHour());
        assertEquals(0, event.getEnd().getMinute());
    }

    @Test
    public void getStartForStorage_returnsCorrectFormat() {
        assertEquals("2024-12-15 1400", event.getStorageStart());
    }

    @Test
    public void getEndForStorage_returnsCorrectFormat() {

        assertEquals("2024-12-15 1600", event.getStorageEnd());
    }

    @Test
    public void toString_unmarkedEvent_correctFormat() {
        String result = event.toString();
        System.out.println(result);

        assertTrue(result.contains("[ ]"));
        assertTrue(result.contains("team meeting"));
        assertTrue(result.contains("Dec 15 2024"));
        assertTrue(result.contains("2:00pm"));
        assertTrue(result.contains("4:00pm"));
        assertTrue(result.contains("from:"));
        assertTrue(result.contains("to:"));
    }

    @Test
    public void toString_markedEvent_correctFormat() {
        event.markDone();
        String result = event.toString();

        assertTrue(result.contains("[X]"));
        assertTrue(result.contains("team meeting"));
    }

    @Test
    public void createFromString_multiDayEvent_success() {
        Event e = Event.createFromString("conference", 
                                         "2024-12-15 0900", 
                                         "2024-12-17 1700");
        
        assertEquals(15, e.getStart().getDayOfMonth());
        assertEquals(17, e.getEnd().getDayOfMonth());
    }

    @Test
    public void createFromString_overnightEvent_success() {
        Event e = Event.createFromString("night shift", 
                                         "2024-12-15 2200", 
                                         "2024-12-16 0600");
        
        assertEquals(22, e.getStart().getHour());
        assertEquals(6, e.getEnd().getHour());
        assertEquals(15, e.getStart().getDayOfMonth());
        assertEquals(16, e.getEnd().getDayOfMonth());
    }

    @Test
    public void createFromString_sameDayEvent_success() {
        Event e = Event.createFromString("lunch meeting", 
                                         "2024-12-15 1200", 
                                         "2024-12-15 1300");
        
        assertEquals(e.getStart().toLocalDate(), e.getEnd().toLocalDate());
    }

    @Test
    public void markDone_changesStatusButNotTimes() {
        LocalDateTime originalStart = event.getStart();
        LocalDateTime originalEnd = event.getEnd();
        
        event.markDone();
        
        assertTrue(event.isDone());
        assertEquals(originalStart, event.getStart());
        assertEquals(originalEnd, event.getEnd());
    }
}
