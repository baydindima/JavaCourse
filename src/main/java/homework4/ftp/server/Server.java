package homework4.ftp.server;


import homework4.ftp.model.Query;
import homework4.ftp.model.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.nio.channels.CompletionHandler;

/**
 * Created by Dmitriy Baidin.
 */
@Slf4j
public abstract class Server implements Closeable {
    @Getter
    private final int port;
    @Getter
    private boolean isStarted;
    @Getter
    private boolean isClosed;

    private AsynchronousServerSocketChannel serverSocketChannel;

    Server(int port) {
        this.port = port;
    }


    public void start() throws IOException {
        if (isStarted) {
            throw new InvalidStateException("Server already started!");
        }
        if (isClosed) {
            throw new InvalidStateException("Server already closed!");
        }
        isStarted = true;
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));

        log.info("Server listening {}", port);

        ServerAttachment serverAttachment = new ServerAttachment(serverSocketChannel);


        serverSocketChannel.accept(serverAttachment, new CompletionHandler<AsynchronousSocketChannel, ServerAttachment>() {
            @Override
            public void completed(AsynchronousSocketChannel client, ServerAttachment attachment) {
                log.info("Connection established with client {}", getRemoteAddress(client));
                attachment.serverSocketChannel.accept(attachment, this);

                try (ObjectInputStream objectInputStream = new ObjectInputStream(Channels.newInputStream(client));
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(Channels.newOutputStream(client))) {
                    Object query = objectInputStream.readObject();
                    log.info("Getting message {} from {}", query, getRemoteAddress(client));
                    Object response = processMessage((Query) query);
                    log.info("Send response {} to {}", response, getRemoteAddress(client));
                    objectOutputStream.writeObject(response);
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Exception occurred while processing message from {}!", getRemoteAddress(client), e);
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void failed(Throwable exc, ServerAttachment attachment) {
                log.error("Exception occurred while trying establish connection!", exc);
            }
        });
    }

    protected abstract Response processMessage(Query query);

    private SocketAddress getRemoteAddress(AsynchronousSocketChannel client) {
        try {
            return client.getRemoteAddress();
        } catch (IOException e) {
            log.error("Exception occurred while getting remote address", e);
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        isClosed = true;
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
        }
    }


    private static class ServerAttachment {
        private final AsynchronousServerSocketChannel serverSocketChannel;

        private ServerAttachment(AsynchronousServerSocketChannel serverSocketChannel) {
            this.serverSocketChannel = serverSocketChannel;
        }
    }

}
