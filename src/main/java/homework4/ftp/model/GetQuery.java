package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Query to get file
 */
@Data
public class GetQuery implements Serializable, Query {
    /**
     * Path of file
     */
    private final String filePath;
}
