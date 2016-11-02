package homework.ftp.ftp.client;

import homework.ftp.ftp.exception.InvalidResponseFormat;
import homework.ftp.ftp.exception.ServerException;
import homework.ftp.ftp.model.*;
import homework.torrent.model.reader.ObjectReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * FTP client.
 */
@Slf4j
public class FtpClient {

    /**
     * Address of server.
     */
    @Getter
    @NotNull
    private final InetSocketAddress hostAddress;

    /**
     * Create new instance of FTP client.
     *
     * @param inetSocketAddress address of server.
     */
    public FtpClient(@NotNull final InetSocketAddress inetSocketAddress) {
        this.hostAddress = inetSocketAddress;
    }

    /**
     * Execute list query on FTP server.
     *
     * @param path path of directory
     * @return list response
     * @throws IOException if IO exception occurs
     */
    public final ListFtpResponse executeList(@NotNull final String path)
            throws IOException {
        FtpResponse response = new Request<FtpResponse,
                ListFtpQuery.ListFtpQueryWriter,
                MaybeExceptionResponseReader<ListFtpResponse>>()
                .request(new ListFtpQuery(path).new ListFtpQueryWriter(),
                        new MaybeExceptionResponseReader<ListFtpResponse>() {
                            @NotNull
                            @Override
                            protected ObjectReader<ListFtpResponse>
                            getReader() {
                                return new ListFtpResponse
                                        .ListFtpResponseReader();
                            }
                        },
                        hostAddress);
        switch (response.getType()) {
            case ListType:
                return (ListFtpResponse) response;
            case ExceptionType:
                throw new ServerException((ExceptionFtpResponse) response);
            default:
                throw new InvalidResponseFormat("Unexpected return format!");
        }
    }

    /**
     * Execute get query on GTP server.
     *
     * @param path path of file on server
     * @param file file for writing
     * @return get response
     * @throws IOException if IO exception occurs
     */
    public final GetFtpResponse
    executeGet(@NotNull final String path,
               @NotNull final File file) throws IOException {
        FtpResponse response = new Request<FtpResponse,
                GetFtpQuery.GetFtpQueryWriter,
                MaybeExceptionResponseReader<GetFtpResponse>>()
                .request(new GetFtpQuery(path).new GetFtpQueryWriter(),
                        new MaybeExceptionResponseReader<GetFtpResponse>() {
                            @NotNull
                            @Override
                            protected ObjectReader<GetFtpResponse> getReader() {
                                return new GetFtpResponse
                                        .GetFtpResponseReader(file);
                            }
                        }, hostAddress);
        switch (response.getType()) {
            case GetType:
                return (GetFtpResponse) response;
            case ExceptionType:
                throw new ServerException((ExceptionFtpResponse) response);
            default:
                throw new InvalidResponseFormat("Unexpected return format!");
        }
    }

}
