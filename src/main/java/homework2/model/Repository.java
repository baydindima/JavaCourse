package homework2.model;


import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Baidin Dima
 */
@Getter
public class Repository {
    private final Map<Long, Branch> branches;

    private transient final Map<Long, Commit> commits;

    private long currentRevision;

    private Repository(Map<Long, Branch> branches,
                       Map<Long, Commit> commits,
                       long currentRevision) {
        this.branches = branches;
        this.commits = commits;
        this.currentRevision = currentRevision;
    }

    public static Repository newRepository(List<Branch> branches,
                                           long currentRevision) {
        return new Repository(
                branches.stream()
                        .collect(Collectors.toMap(Branch::getId, branch -> branch)),
                branches.stream()
                        .flatMap(b -> b.getCommits().stream())
                        .collect(Collectors.toMap(Commit::getId, commit -> commit)),
                currentRevision
        );
    }

}
