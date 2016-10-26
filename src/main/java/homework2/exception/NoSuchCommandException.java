package homework2.exception;

/**
 * Unchecked exception been thrown if no such command
 */
public class NoSuchCommandException extends RuntimeException {
    /**
     * Unchecked exception been thrown if no such command
     */
    public NoSuchCommandException() {
        super("No such command!");
    }
}
