package homework.ftp.ftp.model;

import homework.ftp.ftp.model.writer.IntWriter;
import homework.ftp.ftp.model.writer.StringWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * FtpQuery to get files in directory.
 */
@Data
public class ListFtpQuery implements FtpQuery {
    /**
     * Path of directory.
     */
    private final String directoryPath;

    /**
     * Type of query.
     *
     * @return type of query
     */
    @NotNull
    @Override
    public final FtpQueryType getType() {
        return FtpQueryType.ListType;
    }

    /**
     * Writer of ListFtpQuery.
     */
    public class ListFtpQueryWriter implements ObjectWriter {
        /**
         * Writer of type.
         */
        @NotNull
        private final IntWriter typeWriter = new IntWriter(
                FtpQueryType.ListType.getValue());
        /**
         * Writer of message.
         */
        @NotNull
        private final StringWriter stringWriter =
                new StringWriter(directoryPath);

        /**
         * Write to byteBuffer min of remaining object size or buffer space.
         *
         * @param byteBuffer buffer for writing
         * @return count of written bytes
         */
        @Override
        public final int write(@NotNull final ByteBuffer byteBuffer) {
            return typeWriter.write(byteBuffer)
                    + stringWriter.write(byteBuffer);
        }

        /**
         * Is writer completed writing of object.
         *
         * @return true if writing completed, false otherwise
         */
        @Override
        public final boolean isComplete() {
            return typeWriter.isComplete() && stringWriter.isComplete();
        }
    }
}
