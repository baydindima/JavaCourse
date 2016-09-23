package homework2.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class InMemoryRepository implements Serializable, Repository {
    private final List<Branch> branches = new ArrayList<>();

    private transient Map<Long, Branch> idToBranch;
    private transient Map<Long, Commit> idToCommit;

    public InMemoryRepository() {
        addBranch("master").getId();
    }

    @Getter
    private Long currentRevisionId;

    @Getter
    private Long currentBranchId;

    @Override
    public Branch addBranch(String name) {
        Branch parentBranch = currentBranch();

        Long parentId = null;
        if (parentBranch != null) {
            Branch.isNotClose(parentBranch);
            Branch.isNotEmpty(parentBranch);
            parentId = parentBranch.getId();
        }

        Branch branch = Branch.newInstance(name, parentId);
        updateWithNewBranch(branch);
        return branch;
    }

    private Branch currentBranch() {
        return getIdToBranch().get(currentBranchId);
    }

    private void updateWithNewBranch(Branch branch) {
        branches.add(branch);
        getIdToBranch().put(branch.getId(), branch);
        changeRevision(branch.getId());
    }

    @Override
    public Commit addCommit(String name) {
        Branch currentBranch = currentBranch();
        Branch.isNotNull(currentBranch);
        Branch.isNotClose(currentBranch);

        Commit commit = currentBranch.addCommit(name, currentRevisionId);
        updateWithNewCommit(commit);
        return commit;
    }

    private void updateWithNewCommit(Commit commit) {
        getIdToCommit().put(commit.getId(), commit);
        changeRevision(commit.getId());
    }

    @Override
    public void changeRevision(long revisionId) {
        Commit commit = getIdToCommit().get(revisionId);
        if (commit != null) {
            currentRevisionId = commit.getId();
            currentBranchId = commit.getBranchId();
            return;
        }

        Branch branch = getIdToBranch().get(revisionId);
        if (branch != null) {
            currentRevisionId = null;
            if (!branch.isEmpty()) {
                currentRevisionId = branch.getLastCommit().getId();
            }
            currentBranchId = branch.getId();
            return;
        }

        throw new RuntimeException("No such revision " + revisionId);
    }

    @Override
    public List<Branch> getBranches() {
        return Collections.unmodifiableList(branches);
    }

    private Map<Long, Branch> getIdToBranch() {
        if (idToBranch == null) {
            idToBranch = new HashMap<>();
            for (Branch branch : branches) {
                idToBranch.put(branch.getId(), branch);
            }
        }
        return idToBranch;
    }

    private Map<Long, Commit> getIdToCommit() {
        if (idToCommit == null) {
            idToCommit = new HashMap<>();
            for (Branch branch : branches) {
                for (Commit commit : branch.getCommits()) {
                    idToCommit.put(commit.getId(), commit);
                }
            }
        }
        return idToCommit;
    }

    @Override
    public Optional<Commit> getCommitById(long commitId) {
        return Optional.ofNullable(getIdToCommit().get(commitId));
    }

    @Override
    public Optional<Branch> getBranchById(long branchId) {
        return Optional.ofNullable(getIdToBranch().get(branchId));
    }

    @Override
    public void closeBranch() {
        Branch.isNotNull(currentBranch());
        Branch.isNotClose(currentBranch());

        currentBranch().closeBranch();
    }
}
