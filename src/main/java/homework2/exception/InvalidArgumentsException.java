package homework2.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Unchecked exception for working with arguments
 */
public class InvalidArgumentsException extends RuntimeException {

    /**
     * Unchecked exception for working with arguments
     */
    public InvalidArgumentsException(String message, String[] args) {
        super(message + " Args: " + Arrays.stream(args).collect(Collectors.joining(", ")));

    }
}
