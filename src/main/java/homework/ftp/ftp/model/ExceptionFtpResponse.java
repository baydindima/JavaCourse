package homework.ftp.ftp.model;

import homework.ftp.ftp.exception.InvalidProcessorStateException;
import homework.ftp.ftp.exception.InvalidResponseFormat;
import homework.ftp.ftp.model.reader.IntReader;
import homework.ftp.ftp.model.reader.StringReader;
import homework.ftp.ftp.model.writer.IntWriter;
import homework.ftp.ftp.model.writer.StringWriter;
import homework.torrent.model.reader.ObjectReader;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * FtpResponse of exception on server side.
 */
@Data
public class ExceptionFtpResponse implements FtpResponse {
    /**
     * Message of exception.
     */
    private final String message;

    /**
     * Type of response.
     *
     * @return type of response
     */
    @NotNull
    @Override
    public final FtpResponseType getType() {
        return FtpResponseType.ExceptionType;
    }

    /**
     * ObjectReader for FTP exception response.
     */
    public static class ExceptionFtpResponseReader
            implements ObjectReader<ExceptionFtpResponse> {
        /**
         * Type reader.
         */
        @NotNull
        private final IntReader typeReader = new IntReader();
        /**
         * Message reader.
         */
        @NotNull
        private final StringReader messageReader = new StringReader();
        /**
         * Result of reading.
         */
        @Nullable
        private ExceptionFtpResponse result;

        /**
         * Add data to processing object.
         *
         * @param byteBuffer buffer which contains data
         * @return count of read bytes
         */
        @Override
        public final int read(@NotNull final ByteBuffer byteBuffer) {
            return typeReader.read(byteBuffer) + messageReader.read(byteBuffer);
        }

        /**
         * Reader is ready to get result.
         *
         * @return true if ready, false otherwise
         */
        @Override
        public final boolean isReady() {
            return typeReader.isReady() && messageReader.isReady();
        }

        /**
         * Return reader result.
         *
         * @return completed object
         * @throws InvalidProcessorStateException if processor not ready
         *                                        for creation of object
         */
        @NotNull
        @Override
        public final ExceptionFtpResponse getResult() {
            if (!isReady()) {
                throw new InvalidProcessorStateException(
                        "FTP exception response is not ready!");
            }
            if (result == null) {
                if (typeReader.getResult()
                        != FtpResponseType.ExceptionType.getValue()) {
                    throw new InvalidResponseFormat(
                            "Try to parse message as exception but type of "
                                    + "message doesn't equal to error type");
                }
                result = new ExceptionFtpResponse(messageReader.getResult());
            }
            return result;
        }
    }

    /**
     * Writer of ftp exception response.
     */
    public class ExceptionFtpResponseWriter implements ObjectWriter {

        /**
         * Type writer.
         */
        @NotNull
        private final IntWriter typeWriter =
                new IntWriter(FtpResponseType.ExceptionType.getValue());
        /**
         * Message writer.
         */
        @NotNull
        private final StringWriter stringWriter = new StringWriter(message);

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
