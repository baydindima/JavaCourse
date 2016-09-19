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
    private final Map<java.lang.Long, Branch> branches;
    private final Map<java.lang.Long, Commit> commits;

    private Commit currentRevision;

    private Repository(Map<java.lang.Long, Branch> branches,
                       Map<java.lang.Long, Commit> commits,
                       Commit currentRevision) {
        this.branches = branches;
        this.commits = commits;
        this.currentRevision = currentRevision;
    }

    public static Repository newRepository(List<Branch> branches,
                                           List<Commit> aCommits,
                                           Commit currentRevision) {
        return new Repository(
                branches.stream().collect(Collectors.toMap(Branch::getId, branch -> branch)),
                aCommits.stream().collect(Collectors.toMap(Commit::getId, commit -> commit)),
                currentRevision
        );
    }

}
