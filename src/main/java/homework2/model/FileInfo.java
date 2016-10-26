package homework2.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Class contains path of file and last update date
 */
@Data
public class FileInfo implements Serializable {
    private final String path;
    private final long lastUpdated;
}
