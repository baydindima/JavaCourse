package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Query to get files in directory
 */
@Data
public class ListQuery implements Serializable, Query {
    /**
     * Path of directory
     */
    private final String directoryPath;
}
