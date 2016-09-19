package homework2.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Baidin Dima
 */
@Getter
public class Commit {
    private final long id;
    private final long branchId;
    private final long parentId;
    private final String message;

    private final List<String> files;
    private final List<String> removedFiles;

    public Commit(long id,
                  long branchId,
                  long parentId,
                  String message,
                  List<String> files,
                  List<String> removedFiles) {
        this.id = id;
        this.branchId = branchId;
        this.parentId = parentId;
        this.message = message;
        this.files = files;
        this.removedFiles = removedFiles;
    }

    public static Commit newCommit(String message,
                                   long branchId,
                                   long parentId) {
        long commitId = ThreadLocalRandom.current().nextLong(0, java.lang.Long.MAX_VALUE);
        return new Commit(commitId,
                branchId,
                parentId,
                message,
                new ArrayList<>(),
                new ArrayList<>());
    }
}
