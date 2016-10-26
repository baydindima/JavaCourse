package homework2.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Branch of repository
 */
public class Branch implements Serializable {
    @Getter
    private final long id;
    @Getter
    private final String name;
    @Getter
    private final Long parentId;
    private final List<Commit> commits;

    @Getter
    private boolean isClosed;

    private Branch(long id, String name, Long parentId, List<Commit> commits) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.commits = commits;
    }

    static Branch newInstance(String name, Long parentId) {
        long branchId = ThreadLocalRandom.current().nextLong(0, java.lang.Long.MAX_VALUE);
        return new Branch(branchId,
                name,
                parentId,
                new ArrayList<>());
    }

    static void isNotEmpty(Branch branch) {
        if (branch.getCommits().size() == 0) {
            throw new RuntimeException("Branch is empty!");
        }
    }

    static void isNotNull(Branch branch) {
        if (branch == null) {
            throw new RuntimeException("Branch isn't exist!");
        }
    }

    static void isNotClose(Branch branch) {
        if (branch.isClosed()) {
            throw new RuntimeException("Current branch is closed!");
        }
    }

    void closeBranch() {
        isClosed = true;
    }

    Commit addCommit(String message,
                     Long parentId,
                     List<FileInfo> addedFiles,
                     List<FileInfo> removedFiles) {
        Commit commit = Commit.newInstance(message,
                id,
                parentId,
                addedFiles,
                removedFiles);

        commits.add(commit);
        return commit;
    }

    /**
     * Get all commits of current branch
     *
     * @return all commits of current branch
     */
    public List<Commit> getCommits() {
        return Collections.unmodifiableList(commits);
    }

    /**
     * Get last commit of current branch
     *
     * @return last commit of current branch
     */
    public Commit getLastCommit() {
        Branch.isNotEmpty(this);
        return commits.get(commits.size() - 1);
    }

    /**
     * Get first commit of current branch
     *
     * @return first commit of current branch
     */
    public Commit getFirstCommit() {
        Branch.isNotEmpty(this);
        return commits.get(0);
    }

    /**
     * @return true if no commits in current branch, false otherwise
     */
    public boolean isEmpty() {
        return commits.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return id == branch.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
