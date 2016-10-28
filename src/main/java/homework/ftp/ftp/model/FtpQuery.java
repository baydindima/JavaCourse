package homework.ftp.ftp.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Mark interface for query to server.
 */
public interface FtpQuery {

    /**
     * Type of query.
     *
     * @return type of query
     */
    @NotNull FtpQueryType getType();

    /**
     * Types of query.
     */
    enum FtpQueryType {
        /**
         * Get query type.
         */
        GetType(1),
        /**
         * List query type.
         */
        ListType(2);

        /**
         * Id of query type.
         */
        @Getter
        private final int value;

        /**
         * Create new instance of query type emun.
         *
         * @param id id of type.
         */
        FtpQueryType(final int id) {
            this.value = id;
        }
    }

}
