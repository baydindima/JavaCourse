package homework4.ftp.exception;

/**
 * Invalid format of response
 */
public class InvalidResponseFormat extends RuntimeException {
    public InvalidResponseFormat(Throwable cause) {
        super(cause);
    }
}
