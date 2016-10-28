package homework.ftp.ftp.model;

import homework.ftp.ftp.exception.InvalidProcessorStateException;
import homework.ftp.ftp.model.reader.BooleanReader;
import homework.ftp.ftp.model.reader.IntReader;
import homework.ftp.ftp.model.reader.StringReader;
import homework.ftp.ftp.model.writer.BooleanWriter;
import homework.ftp.ftp.model.writer.IntWriter;
import homework.ftp.ftp.model.writer.StringWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * FtpResponse to get query.
 */
@Data
public class ListFtpResponse implements FtpResponse {
    /**
     * Count of files in directory.
     */
    private final int size;
    /**
     * List of files in directory.
     */
    @NotNull
    private final List<FileInfo> fileInfoList;

    /**
     * Type of response.
     *
     * @return type of response
     */
    @Override
    @NotNull
    public final FtpResponseType getType() {
        return FtpResponseType.ListType;
    }

    /**
     * Info about file on server.
     */
    @Data
    public static final class FileInfo implements Serializable {
        /**
         * True if file is a directory, false otherwise.
         */
        private final boolean isDirectory;
        /**
         * Name of file.
         */
        private final String name;

        /**
         * ObjectReader for FileInfo.
         */
        static class FileInfoReader implements ObjectReader<FileInfo> {
            /**
             * Is directory mark reader.
             */
            @NotNull
            private final BooleanReader
                    isDirectoryReader = new BooleanReader();
            /**
             * File name reader.
             */
            @NotNull
            private final StringReader nameReader = new StringReader();
            /**
             *
             */
            @Nullable
            private FileInfo result;

            /**
             * Add data to processing object.
             *
             * @param byteBuffer buffer which contains data
             * @return count of read bytes
             */
            @Override
            public final int read(@NotNull final ByteBuffer byteBuffer) {
                return nameReader.read(byteBuffer)
                        + isDirectoryReader.read(byteBuffer);
            }

            /**
             * Reader is ready to get result.
             *
             * @return true if ready, false otherwise
             */
            @Override
            public final boolean isReady() {
                return result != null || (nameReader.isReady()
                        && isDirectoryReader.isReady());
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
            public final ListFtpResponse.FileInfo getResult() {
                if (!isReady()) {
                    throw new InvalidProcessorStateException(
                            "File info is not ready!");
                }
                if (result == null) {
                    result = new FileInfo(isDirectoryReader.getResult(),
                            nameReader.getResult());
                }
                return result;
            }
        }

        /**
         * ObjectWriter for FileInfo.
         */
        class FileInfoWriter implements ObjectWriter {
            /**
             * Is directory mark writer.
             */
            @NotNull
            private BooleanWriter isDirectoryWriter =
                    new BooleanWriter(isDirectory);
            /**
             * File name writer.
             */
            @NotNull
            private StringWriter nameWriter = new StringWriter(name);

            /**
             * Write to byteBuffer min of remaining object size or buffer space.
             *
             * @param byteBuffer buffer for writing
             * @return count of written bytes
             */
            @Override
            public final int write(@NotNull final ByteBuffer byteBuffer) {
                return nameWriter.write(byteBuffer)
                        + isDirectoryWriter.write(byteBuffer);
            }

            /**
             * Is writer completed writing of object.
             *
             * @return true if writing completed, false otherwise
             */
            @Override
            public final boolean isComplete() {
                return nameWriter.isComplete()
                        && isDirectoryWriter.isComplete();
            }
        }

    }

    /**
     * ObjectReader of list FTP response.
     */
    public static class ListFtpResponseReader
            implements ObjectReader<ListFtpResponse> {
        /**
         * Size reader.
         */
        @NotNull
        private final IntReader sizeReader = new IntReader();
        /**
         * List of file info.
         */
        @Nullable
        private List<FileInfo> fileInfoList;
        /**
         * Reader for file info.
         */
        @Nullable
        private FileInfo.FileInfoReader fileInfoReader;
        /**
         * Result of reader.
         */
        @Nullable
        private ListFtpResponse result;

        /**
         * Add data to processing object.
         *
         * @param byteBuffer buffer which contains data
         * @return count of read bytes
         */
        @Override
        public final int read(@NotNull final ByteBuffer byteBuffer) {
            int readCount = 0;
            readCount += sizeReader.read(byteBuffer);
            if (sizeReader.isReady()) {
                if (fileInfoList == null) {
                    fileInfoList = new ArrayList<>(sizeReader.getResult());
                }
                while (byteBuffer.hasRemaining()
                        && fileInfoList.size() < sizeReader.getResult()) {
                    if (fileInfoReader == null) {
                        fileInfoReader = new FileInfo.FileInfoReader();
                    }
                    readCount += fileInfoReader.read(byteBuffer);
                    if (fileInfoReader.isReady()) {
                        fileInfoList.add(fileInfoReader.getResult());
                        fileInfoReader = null;
                    }
                }
            }
            return readCount;
        }

        /**
         * Reader is ready to get result.
         *
         * @return true if ready, false otherwise
         */
        @Override
        public final boolean isReady() {
            return result != null || (sizeReader.isReady()
                    && fileInfoList != null
                    && fileInfoList.size() == sizeReader.getResult());
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
        public final ListFtpResponse getResult() {
            if (!isReady()) {
                throw new InvalidProcessorStateException(
                        "List ftp response is not ready!");
            }
            if (result == null) {
                assert fileInfoList != null;
                result = new ListFtpResponse(sizeReader.getResult(),
                        fileInfoList);
            }
            return result;
        }
    }

    /**
     * ObjectWriter of list FTp response.
     */
    public class ListFtpResponseWriter implements ObjectWriter {
        /**
         * Iterator on file info list.
         */
        @NotNull
        private Iterator<FileInfo> iterator = fileInfoList.iterator();
        /**
         * Writer of file info list size.
         */
        @NotNull
        private IntWriter sizeWriter = new IntWriter(size);
        /**
         * File info writer.
         */
        @Nullable
        private FileInfo.FileInfoWriter fileInfoWriter;

        /**
         * Write to byteBuffer min of remaining object size or buffer space.
         *
         * @param byteBuffer buffer for writing
         * @return count of written bytes
         */
        @Override
        public final int write(@NotNull final ByteBuffer byteBuffer) {
            int writeCount = 0;
            writeCount += sizeWriter.write(byteBuffer);
            if (sizeWriter.isComplete()) {
                while (byteBuffer.hasRemaining()
                        && (iterator.hasNext() || fileInfoWriter != null)) {
                    if (fileInfoWriter == null) {
                        fileInfoWriter = iterator.next().new FileInfoWriter();
                    }
                    writeCount += fileInfoWriter.write(byteBuffer);
                    if (fileInfoWriter.isComplete()) {
                        fileInfoWriter = null;
                    }
                }
            }
            return writeCount;
        }

        /**
         * Is writer completed writing of object.
         *
         * @return true if writing completed, false otherwise
         */
        @Override
        public final boolean isComplete() {
            return sizeWriter.isComplete() && !iterator.hasNext()
                    && (fileInfoWriter == null || fileInfoWriter.isComplete());
        }
    }
}
