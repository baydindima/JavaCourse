package homework4.ftp.client;

import homework4.ftp.model.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

/**
 * Created by Dmitriy Baidin.
 */
@Slf4j
public class FtpClient {

    @Getter
    private final InetSocketAddress hostAddress;

    public FtpClient(InetSocketAddress hostAddress) {
        this.hostAddress = hostAddress;
    }

    public ListResponse executeList(String path) throws IOException {
        SocketChannel client = SocketChannel.open(hostAddress);
        ListQuery simpleQuery = new ListQuery(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(Channels.newOutputStream(client));
        objectOutputStream.writeObject(simpleQuery);
        ObjectInputStream objectInputStream = new ObjectInputStream(Channels.newInputStream(client));
        try {
            Response response = (Response) objectInputStream.readObject();
            if (response instanceof ExceptionResponse) {
                throw new RuntimeException(((ExceptionResponse) response).getMessage());
            }
            return (ListResponse) response;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public GetResponse executeGet(String path) throws IOException {
        SocketChannel client = SocketChannel.open(hostAddress);
        GetQuery simpleQuery = new GetQuery(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(Channels.newOutputStream(client));
        objectOutputStream.writeObject(simpleQuery);
        ObjectInputStream objectInputStream = new ObjectInputStream(Channels.newInputStream(client));
        try {
            Response response = (Response) objectInputStream.readObject();
            if (response instanceof ExceptionResponse) {
                throw new RuntimeException(((ExceptionResponse) response).getMessage());
            }
            return (GetResponse) response;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
