package homework2.model;

import java.util.List;
import java.util.Optional;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public interface Repository {

    Branch addBranch(String name);

    Commit addCommit(String name);

    List<Branch> getBranches();

    void changeRevision(long revisionId);

    Long getCurrentRevisionId();

    Long getCurrentBranchId();

    Optional<Commit> getCommitById(long commitId);

    Optional<Branch> getBranchById(long branchId);

    void closeBranch();

}
