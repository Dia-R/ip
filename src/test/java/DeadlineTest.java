import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import task.Deadline;
import task.TaskType;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * JUnit tests for Deadline class.
 * Tests deadline creation, date parsing, and string formatting.
 */
public class DeadlineTest {
    private Deadline deadline;
    private LocalDateTime testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDateTime.of(2024, 12, 15, 18, 0);
        deadline = new Deadline("return book", testDate);
    }

    /**
     * Verify that a Deadline can be constructed with valid inputs
     * and correctly stores description, type, date, and initial completion status.
     */
    @Test
    public void constructor_validInputs_createsDeadline() {
        assertEquals("return book", deadline.getUserTask());
        assertEquals(TaskType.Deadline, deadline.getType());
        assertEquals(testDate, deadline.getDeadline());
        assertFalse(deadline.isDone());
    }

    @Test
    public void createFromString_validFormat_success() {
        Deadline d = Deadline.createFromString("submit assignment", "2024-12-20 2359");
        
        assertEquals("submit assignment", d.getUserTask());
        assertEquals(LocalDateTime.of(2024, 12, 20, 23, 59), d.getDeadline());
    }

    @Test
    public void createFromString_invalidFormat_throwsException() {
        assertThrows(DateTimeParseException.class, () -> {
            Deadline.createFromString("task", "invalid date");
        });
        
        assertThrows(DateTimeParseException.class, () -> {
            Deadline.createFromString("task", "2024-12-15");  // Missing time
        });
        
        assertThrows(DateTimeParseException.class, () -> {
            Deadline.createFromString("task", "15-12-2024 1800");  // Wrong format
        });
    }

    /**
     * Verify that getDeadline() returns the correct LocalDateTime
     * with all date/time components intact.
     */
    @Test
    public void getDeadline_returnsCorrectDateTime() {
        assertEquals(testDate, deadline.getDeadline());
        assertEquals(2024, deadline.getDeadline().getYear());
        assertEquals(12, deadline.getDeadline().getMonthValue());
        assertEquals(15, deadline.getDeadline().getDayOfMonth());
        assertEquals(18, deadline.getDeadline().getHour());
        assertEquals(0, deadline.getDeadline().getMinute());
    }

    /**
     * Verify that getStorageDeadline() returns the deadline
     * in the correct storage format "yyyy-MM-dd HHmm".
     */
    @Test
    public void getDeadlineForStorage_returnsCorrectFormat() {
        String storage = deadline.getStorageDeadline();
        assertEquals("2024-12-15 1800", storage);
    }

    /**
     * Verify that toString() for a unmarked deadline shows
     * the [ ] checkbox instead of [X].
     */
    @Test
    public void toString_unmarkedDeadline_correctFormat() {
        String result = deadline.toString();

        assertTrue(result.contains("[ ]"));
        assertTrue(result.contains("return book"));
        assertTrue(result.contains("Dec 15 2024"));
        assertTrue(result.contains("6:00pm"));
    }

    /**
     * Verify that toString() for a marked deadline shows
     * the [X] checkbox instead of [ ].
     */
    @Test
    public void toString_markedDeadline_correctFormat() {
        deadline.markDone();
        String result = deadline.toString();

        assertTrue(result.contains("[X]"));
        assertTrue(result.contains("return book"));
    }

    /**
     * Verify that createFromString() correctly handles midnight time (0000).
     */
    @Test
    public void createFromString_midnightTime_success() {
        Deadline d = Deadline.createFromString("task", "2024-12-15 0000");
        assertEquals(0, d.getDeadline().getHour());
        assertEquals(0, d.getDeadline().getMinute());
    }

    @Test
    public void createFromString_noonTime_success() {
        Deadline d = Deadline.createFromString("task", "2024-12-15 1200");
        assertEquals(12, d.getDeadline().getHour());
        assertTrue(d.toString().contains("12:00pm"));
    }

    @Test
    public void markDone_changesStatusButNotDate() {
        LocalDateTime originalDate = deadline.getDeadline();
        
        deadline.markDone();
        
        assertTrue(deadline.isDone());
        assertEquals(originalDate, deadline.getDeadline());
    }
}
