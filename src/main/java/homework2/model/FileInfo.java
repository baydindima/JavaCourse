package homework2.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
@Data
public class FileInfo implements Serializable {
    private final String path;
    private final long lastUpdated;
}
