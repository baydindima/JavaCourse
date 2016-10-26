package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Response of exception on server side
 */
@Data
public class ExceptionResponse implements Serializable, Response {
    /**
     * Message of exception
     */
    private final String message;
}
