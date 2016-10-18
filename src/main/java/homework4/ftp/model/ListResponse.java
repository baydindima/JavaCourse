package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class ListResponse implements Serializable, Response {
    private final int size;
    private final List<FileInfo> fileInfoList;

    @Data
    public static final class FileInfo implements Serializable {
        private final boolean isDirectory;
        private final String name;
    }
}
