package homework2.model;

import java.util.List;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public interface Repository {

    Branch addBranch(String name);

    Commit addCommit(String name, List<FileInfo> addedFiles, List<FileInfo> removedFiles);

    List<Branch> getBranches();

    void changeRevision(long revisionId);

    void changeRevision(String branchName);

    Long getCurrentRevisionId();

    Long getCurrentBranchId();

    Commit getCommitById(long commitId);

    Branch getBranchById(long branchId);

    void closeBranch();

}
