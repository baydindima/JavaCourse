package homework.torrent.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when response's format is invalid.
 *
 * @author dmitriy
 */
public class InvalidResponseFormat extends RuntimeException {

    /**
     * Thrown when response's format is invalid.
     *
     * @param message error message
     */
    public InvalidResponseFormat(@NotNull final String message) {
        super(message);
    }

    /**
     * Thrown when response's format is invalid.
     *
     * @param cause the cause
     */
    public InvalidResponseFormat(@NotNull final Throwable cause) {
        super(cause);
    }
}
