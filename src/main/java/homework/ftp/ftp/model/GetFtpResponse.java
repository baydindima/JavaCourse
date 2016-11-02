package homework.ftp.ftp.model;

import homework.ftp.ftp.exception.InvalidProcessorStateException;
import homework.ftp.ftp.model.reader.LongReader;
import homework.torrent.model.reader.ObjectReader;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FtpResponse to get query.
 */
@Data
public class GetFtpResponse implements FtpResponse {

    /**
     * File's size.
     */
    private final long size;


    /**
     * Type of response.
     *
     * @return type of response
     */
    @NotNull
    @Override
    public final FtpResponseType getType() {
        return FtpResponseType.GetType;
    }

    /**
     * ObjectReader for get FTP response.
     */
    public static class GetFtpResponseReader
            implements ObjectReader<GetFtpResponse> {
        /**
         * Writer of size.
         */
        @NotNull
        private final LongReader sizeReader = new LongReader();
        /**
         * File channel for writing.
         */
        @NotNull
        private final FileChannel writer;
        /**
         * Result object.
         */
        @Nullable
        private GetFtpResponse result;
        /**
         * Current written length.
         */
        private long curLength = 0;

        /**
         * Create new instance of get FTP response reader.
         *
         * @param file file to write result.
         */
        public GetFtpResponseReader(@NotNull final File file) {
            try {
                this.writer = new FileOutputStream(file).getChannel();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        /**
         * Add data to processing object.
         *
         * @param byteBuffer buffer which contains data
         * @return count of read bytes
         */
        @Override
        public final int read(@NotNull final ByteBuffer byteBuffer) {
            int readCountTotal = 0;
            readCountTotal += sizeReader.read(byteBuffer);
            if (sizeReader.isReady()) {
                try {
                    int readCount = 0;
                    readCount += writer.write(byteBuffer);
                    curLength += readCount;
                    readCountTotal += readCount;
                } catch (IOException e) {
                    try {
                        this.writer.close();
                    } catch (IOException e1) {
                        e.addSuppressed(e1);
                        throw new UncheckedIOException(e);
                    }
                    throw new UncheckedIOException(e);
                }
            }
            return readCountTotal;
        }

        /**
         * Reader is ready to get result.
         *
         * @return true if ready, false otherwise
         */
        @Override
        public final boolean isReady() {
            return sizeReader.isReady() && sizeReader.getResult() == curLength;
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
        public final GetFtpResponse getResult() {
            if (!isReady()) {
                throw new InvalidProcessorStateException(
                        "Get ftp response is not ready!");
            }
            if (result == null) {
                try {
                    this.writer.close();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                result = new GetFtpResponse(sizeReader.getResult());
            }
            return result;
        }
    }

}
