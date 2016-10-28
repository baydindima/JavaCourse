package homework.ftp.ftp.model;

import homework.ftp.ftp.model.writer.IntWriter;
import homework.ftp.ftp.model.writer.StringWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * FtpQuery to get file.
 */
@Data
public class GetFtpQuery implements FtpQuery {
    /**
     * Path of file.
     */
    @NotNull
    private final String filePath;

    /**
     * Type of query.
     *
     * @return type of query
     */
    @NotNull
    @Override
    public final FtpQueryType getType() {
        return FtpQueryType.GetType;
    }

    /**
     * Writer of GetFtpQuery.
     */
    public class GetFtpQueryWriter implements ObjectWriter {
        /**
         * Writer of type.
         */
        @NotNull
        private final IntWriter typeWriter =
                new IntWriter(FtpQueryType.GetType.getValue());
        /**
         * Writer of path.
         */
        @NotNull
        private final StringWriter stringWriter =
                new StringWriter(filePath);

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



