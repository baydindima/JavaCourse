package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class GetResponse implements Serializable, Response {
    private final int size;
    private final byte[] content;
}
