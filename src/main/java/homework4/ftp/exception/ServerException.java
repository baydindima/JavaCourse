package homework4.ftp.exception;

import homework4.ftp.model.ExceptionResponse;

/**
 * Exception on server-side
 */
public class ServerException extends RuntimeException {
    public ServerException(ExceptionResponse exceptionResponse) {
        super(exceptionResponse.getMessage());
    }
}
