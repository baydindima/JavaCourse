package homework2.model;

import java.util.List;

/**
 * Interface of repository
 */
public interface Repository {

    /**
     * Add new branch to repository
     *
     * @param name name of branch
     * @return new branch
     */
    Branch addBranch(String name);


    /**
     * Add new commit to repository
     *
     * @param name         message of commit
     * @param addedFiles   new files of commit
     * @param removedFiles removed files of commit
     * @return new commit
     */
    Commit addCommit(String name, List<FileInfo> addedFiles, List<FileInfo> removedFiles);


    /**
     * Get all branches of repository
     *
     * @return all branches of repository
     */
    List<Branch> getBranches();

    /**
     * Change current revision by id of commit or branch
     *
     * @param revisionId id of commit or branch
     */
    void changeRevision(long revisionId);

    /**
     * Change current revision by name of branch
     *
     * @param branchName name of branch
     */
    void changeRevision(String branchName);

    /**
     * Get current revision
     *
     * @return current revision id
     */
    Long getCurrentRevisionId();

    /**
     * Get current branch id
     *
     * @return current branch id
     */
    Long getCurrentBranchId();


    /**
     * Get commit of current repository by commit id
     *
     * @param commitId id of commit
     * @return commit
     */
    Commit getCommitById(long commitId);

    /**
     * Get branch of current repository by id of branch
     *
     * @param branchId id of branch
     * @return branch
     */
    Branch getBranchById(long branchId);

    /**
     * Close current branch
     */
    void closeBranch();

}
