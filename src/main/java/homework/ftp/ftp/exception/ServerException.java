package homework.ftp.ftp.exception;

import homework.ftp.ftp.model.ExceptionFtpResponse;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown if exception occurs on server-side.
 *
 * @author dmitriy
 */
public class ServerException extends RuntimeException {
    /**
     * Thrown if exception occurs on server-side.
     *
     * @param exceptionResponse response with message of exception
     */
    public ServerException(
            @NotNull final ExceptionFtpResponse exceptionResponse) {
        super(exceptionResponse.getMessage());
    }
}
