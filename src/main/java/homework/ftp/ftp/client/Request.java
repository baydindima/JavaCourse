package homework.ftp.ftp.client;

import homework.ftp.ftp.model.ObjectReader;
import homework.ftp.ftp.model.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Class for client request.
 * Request execute in blocking way.
 *
 * @param <T>              result type
 * @param <QueryWriter>    query writer type
 * @param <ResponseReader> response reader type
 */
@Slf4j
class Request<T,
        QueryWriter extends ObjectWriter,
        ResponseReader extends ObjectReader<T>> {
    /**
     * Default size of byte buffer.
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Execute blocking request.
     *
     * @param writer  query writer
     * @param reader  response reader
     * @param address inet address of server
     * @return result
     * @throws IOException if IO exception occurs
     */
    T request(@NotNull final QueryWriter writer,
              @NotNull final ResponseReader reader,
              @NotNull final InetSocketAddress address) throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open(address)) {
            log.info("Connection established with server {}", address);
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (!writer.isComplete()) {
                writer.write(byteBuffer);
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
            socketChannel.shutdownOutput();
            log.info("Query was sent to {}", address);

            while (!reader.isReady()) {
                socketChannel.read(byteBuffer);
                byteBuffer.flip();
                reader.read(byteBuffer);
                byteBuffer.clear();
            }
            log.info("Respond received from {}", address);
            return reader.getResult();
        } catch (IOException e) {
            log.error("IOException during request to {}", address, e);
            throw e;
        }
    }
}
