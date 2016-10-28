package homework.ftp.ftp.server;


import homework.ftp.ftp.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * FTP server.
 */
@Slf4j
public class FtpServer extends Server<FtpQuery, FtpQueryReader, ObjectWriter> {

    /**
     * Path of root directory on server.
     */
    @NotNull
    private final Path dirPath;
    /**
     * Handler for processing after reading from file.
     */
    @NotNull
    private final CompletionHandler<Integer, Attachment> afterFileRead =
            new CompletionHandler<Integer, Attachment>() {
                @Override
                public void completed(@NotNull final Integer result,
                                      @NotNull final Attachment attachment) {
                    log.debug("File reading with result: {}", result);
                    if (result != -1) {
                        attachment.position += result;
                        attachment.buffer.flip();
                        attachment.socketChannel.write(
                                attachment.buffer,
                                attachment,
                                afterSocketWrite);
                    } else {
                        closeConnection(attachment.socketChannel);
                    }
                }

                @Override
                public void failed(@NotNull final Throwable exc,
                                   @NotNull final Attachment attachment) {
                    log.error("Error while reading file!", exc);
                    closeConnection(attachment.socketChannel);
                }
            };
    /**
     * Handler for processing after writing in socket.
     */
    @NotNull
    private final CompletionHandler<Integer, Attachment> afterSocketWrite =
            new CompletionHandler<Integer, Attachment>() {
                @Override
                public void completed(@NotNull final Integer result,
                                      @NotNull final Attachment attachment) {
                    log.debug("Socket writing with result: {}", result);
                    if (attachment.position < attachment.fileSize) {
                        attachment.buffer.clear();
                        attachment.fileChannel.read(attachment.buffer,
                                attachment.position, attachment, afterFileRead);
                    } else {
                        closeConnection(attachment.socketChannel);
                    }
                }

                @Override
                public void failed(@NotNull final Throwable exc,
                                   @NotNull final Attachment attachment) {
                    log.error("Error while socket writing!", exc);
                    closeConnection(attachment.socketChannel);
                }
            };

    /**
     * Create new instance of FTP server.
     *
     * @param port          port of FTP server
     * @param directoryPath path of root directory on server
     */
    public FtpServer(final int port, @NotNull final Path directoryPath) {
        super(port);
        this.dirPath = directoryPath;
    }

    @NotNull
    @Override
    protected final Supplier<@NotNull FtpQueryReader> getReaderSupplier() {
        return FtpQueryReader::new;
    }

    @NotNull
    @Override
    protected final Consumer<@NotNull AsynchronousSocketChannel>
    processMessage(@NotNull final FtpQuery query) {
        switch (query.getType()) {
            case GetType:
                return processGet((GetFtpQuery) query);
            case ListType:
                return defaultWrite(processList((ListFtpQuery) query)
                        .new ListFtpResponseWriter());
            default:
                return defaultWrite(
                        new ExceptionFtpResponse("Invalid command!")
                                .new ExceptionFtpResponseWriter());
        }
    }

    /**
     * Process list query.
     *
     * @param query list query
     * @return response for list query.
     */
    private ListFtpResponse processList(@NotNull final ListFtpQuery query) {
        log.info("Start read list query for directory: {}",
                query.getDirectoryPath());

        List<ListFtpResponse.FileInfo> results = new ArrayList<>();
        File[] files = new File(dirPath.toFile(),
                query.getDirectoryPath()).listFiles();

        if (files != null) {
            for (File file : files) {
                results.add(new ListFtpResponse.FileInfo(file.isDirectory(),
                        file.getName()));
            }
            return new ListFtpResponse(files.length, results);
        } else {
            return new ListFtpResponse(0, results);
        }
    }

    /**
     * Process get query.
     *
     * @param query get query
     * @return function for writing file content to socket
     */
    @NotNull
    private Consumer<@NotNull AsynchronousSocketChannel>
    processGet(@NotNull final GetFtpQuery query) {
        log.info("Start read get query for file: {}", query.getFilePath());
        File file = new File(dirPath.toFile(), query.getFilePath());
        if (!file.exists()) {
            log.error("Get query for non existing file: {}",
                    query.getFilePath());

            return defaultWrite(new ExceptionFtpResponse("No such file!")
                    .new ExceptionFtpResponseWriter());
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        Path filePath = Paths.get(file.getAbsolutePath());
        AsynchronousFileChannel fileChannel;
        try {
            fileChannel = AsynchronousFileChannel.open(filePath,
                    StandardOpenOption.READ);
            byteBuffer.putLong(fileChannel.size());
        } catch (IOException e) {
            log.error("Exception while opening file: {}",
                    query.getFilePath(), e);

            return defaultWrite(
                    new ExceptionFtpResponse(e.getMessage())
                            .new ExceptionFtpResponseWriter());
        }

        return asynchronousSocketChannel -> {
            Attachment attachment =
                    new Attachment(
                            asynchronousSocketChannel,
                            fileChannel,
                            byteBuffer,
                            0);

            attachment.fileChannel.read(
                    attachment.buffer,
                    attachment.position,
                    attachment,
                    afterFileRead);
        };
    }

    /**
     * Attachment for keeping context in asynchronous calls.
     */
    @Data
    private static class Attachment {
        /**
         * Socket channel for writing.
         */
        @NotNull
        private final AsynchronousSocketChannel socketChannel;
        /**
         * File channel for reading.
         */
        @NotNull
        private final AsynchronousFileChannel fileChannel;
        /**
         * Buffer for data transfer.
         */
        @NotNull
        private final ByteBuffer buffer;
        /**
         * Size of file.
         */
        private final long fileSize;
        /**
         * Current position in file.
         */
        private long position;
    }

}
