import org.junit.jupiter.api.Test;
import storage.StorageException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for StorageException class.
 * Tests exception creation and message handling.
 * Includes both positive and negative test cases.
 */
public class StorageExceptionTest {

    /**
     * Verify that a StorageException correctly stores and retrieves
     * a standard error message.
     */
    @Test
    public void constructor_withMessage_storesMessage() {
        String errorMessage = "File not found";
        StorageException exception = new StorageException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Verify that StorageException can be thrown and caught properly
     * in a try-catch block.
     */
    @Test
    public void exception_canBeThrown_andCaught() {
        try {
            throw new StorageException("Test error");
        } catch (StorageException e) {
            assertEquals("Test error", e.getMessage());
        }
    }

    /**
     * Verify that StorageException handles an empty string message
     * without errors.
     */
    @Test
    public void constructor_withEmptyMessage_storesEmptyString() {
        StorageException exception = new StorageException("");
        assertEquals("", exception.getMessage());
    }

    /**
     * Verify that StorageException handles a null message
     * without throwing a NullPointerException.
     */
    @Test
    public void constructor_withNullMessage_handlesNull() {
        StorageException exception = new StorageException(null);
        assertNull(exception.getMessage());
    }
}