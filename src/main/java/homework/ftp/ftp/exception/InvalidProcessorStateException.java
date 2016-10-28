package homework.ftp.ftp.exception;

import org.jetbrains.annotations.NonNls;

/**
 * Thrown when processor in invalid state.
 *
 * @author dmitriy
 */
public class InvalidProcessorStateException extends RuntimeException {
    /**
     * Thrown when processor in invalid state.
     *
     * @param message error message
     */
    public InvalidProcessorStateException(@NonNls final String message) {
        super(message);
    }
}
