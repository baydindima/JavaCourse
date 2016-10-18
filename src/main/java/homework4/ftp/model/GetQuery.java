package homework4.ftp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class GetQuery implements Serializable, Query {
    private final String filePath;
}
