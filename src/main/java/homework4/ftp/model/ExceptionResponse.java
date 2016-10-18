package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class ExceptionResponse implements Serializable, Response {
    private final String message;
}
