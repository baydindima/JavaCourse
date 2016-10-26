package homework4.ftp.server;

import homework4.ftp.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * FTP server
 */
@Slf4j
public class FtpServer extends Server {

    private final Path dirPath;

    /**
     * Create new instance of FTP server
     *
     * @param port    port of FTP server
     * @param dirPath path of root directory on server
     */
    public FtpServer(int port, Path dirPath) {
        super(port);
        this.dirPath = dirPath;
    }

    @Override
    protected Response processMessage(Query query) {
        if (query instanceof ListQuery) {
            return processList((ListQuery) query);
        }

        if (query instanceof GetQuery) {
            return processGet((GetQuery) query);
        }

        log.info("Query handler not found");
        return new ExceptionResponse("Unexpected query");
    }

    private Response processList(ListQuery query) {
        log.info("Start process list query");
        List<ListResponse.FileInfo> results = new ArrayList<>();
        File[] files = new File(dirPath.toFile(), query.getDirectoryPath()).listFiles();

        if (files != null) {
            for (File file : files) {
                results.add(new ListResponse.FileInfo(file.isDirectory(), file.getName()));
            }
            return new ListResponse(files.length, results);
        } else {
            return new ListResponse(0, results);
        }
    }

    private Response processGet(GetQuery query) {
        log.info("Start process get query");
        File file = new File(dirPath.toFile(), query.getFilePath());
        if (!file.exists()) {
            return new ExceptionResponse("No such file!");
        }

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new GetResponse(bytes.length, bytes);
        } catch (IOException e) {
            log.error("Exception during process get query {}", query, e);
            return new ExceptionResponse(e.getMessage());
        }
    }

}
