package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Response to get query
 */
@Data
public class GetResponse implements Serializable, Response {
    /**
     * File's size
     */
    private final int size;
    /**
     * File's content
     */
    private final byte[] content;
}
