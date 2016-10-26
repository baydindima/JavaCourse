package homework2.exception;

/**
 * Unchecked exception for working with file system
 */
public class FileSystemIOException extends RuntimeException {

    /**
     * Unchecked exception for working with file system
     */
    public FileSystemIOException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Unchecked exception for working with file system
     */
    public FileSystemIOException(String message) {
        super(message);
    }
}
