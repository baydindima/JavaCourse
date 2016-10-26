package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Response to get query
 */
@Data
public class ListResponse implements Serializable, Response {
    /**
     * Count of files in directory
     */
    private final int size;
    /**
     * List of files in directory
     */
    private final List<FileInfo> fileInfoList;

    /**
     * Info about file on server
     */
    @Data
    public static final class FileInfo implements Serializable {
        /**
         * True if file is a directory, false otherwise
         */
        private final boolean isDirectory;
        /**
         * Name of file
         */
        private final String name;
    }
}
