package storage;

/**
 * Exception thrown when there are errors with storage operations.
 */
public class StorageException extends Exception {
    /**
     * Creates a StorageException with the specified error message.
     *
     * @param message the error message.
     */
    public StorageException(String message) {
        super(message);
    }
}