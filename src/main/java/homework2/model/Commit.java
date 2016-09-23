package homework2.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class Commit implements Serializable {
    @Getter
    private final long id;
    @Getter
    private final long branchId;
    @Getter
    private final Long parentId;
    @Getter
    private final String message;

    private final List<FileInfo> files;
    private final List<FileInfo> removedFiles;

    private Commit(long id,
                   long branchId,
                   Long parentId,
                   String message,
                   List<FileInfo> files,
                   List<FileInfo> removedFiles) {
        this.id = id;
        this.branchId = branchId;
        this.parentId = parentId;
        this.message = message;
        this.files = files;
        this.removedFiles = removedFiles;
    }

    static Commit newInstance(String message,
                              long branchId,
                              Long parentId,
                              List<FileInfo> addedFiles,
                              List<FileInfo> removedFiles) {
        long commitId = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
        return new Commit(commitId,
                branchId,
                parentId,
                message,
                addedFiles,
                removedFiles);
    }

    public List<FileInfo> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public List<FileInfo> getRemovedFiles() {
        return Collections.unmodifiableList(removedFiles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return id == commit.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
