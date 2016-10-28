package homework.ftp.ftp.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Mark interface of response from server.
 */
public interface FtpResponse {

    /**
     * Type of response.
     *
     * @return type of response
     */
    @NotNull FtpResponseType getType();

    /**
     * Types of responses.
     */
    enum FtpResponseType {
        /**
         * Get response type.
         */
        GetType(1),
        /**
         * List response type.
         */
        ListType(2),
        /**
         * Exception response type.
         */
        ExceptionType(-1);

        /**
         * Id of type.
         */
        @Getter
        private final int value;

        /**
         * Create new instance of type enum.
         *
         * @param id id of type
         */
        FtpResponseType(final int id) {
            this.value = id;
        }
    }

}
