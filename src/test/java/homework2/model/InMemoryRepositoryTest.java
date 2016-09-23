package homework2.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class InMemoryRepositoryTest {
    @Test
    public void closeBranch() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();

        repository.closeBranch();

        //noinspection OptionalGetWithoutIsPresent
        assertTrue("should be closed",
                repository.getBranchById(repository.getCurrentBranchId()).get().isClosed());
    }

    @Test
    public void initBranch() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();

        assertEquals("should have 1 branch on start", 1, repository.getBranches().size());
        Branch masterBranch = repository.getBranches().get(0);
        assertEquals("should have branch master", "master", masterBranch.getName());
        assertTrue("should have no commit", masterBranch.isEmpty());

        assertNull("should have null as current revision id", repository.getCurrentRevisionId());

        assertEquals("should have master branch id as current branch id",
                (Long) masterBranch.getId(), repository.getCurrentBranchId());

        assertNull("should have null as parent id", masterBranch.getParentId());
    }

    @Test(expected = RuntimeException.class)
    public void addBranchToEmptyBranch() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addBranch("should fail");
    }

    @Test(expected = RuntimeException.class)
    public void addBranchToClosedBranch() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        repository.closeBranch();
        repository.addBranch("should fail");
    }

    @Test
    public void addBranch() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());

        Branch secondBranch = repository.addBranch("second Branch");

        assertEquals("should have second branch id as current branch id",
                repository.getCurrentBranchId(), (Long) secondBranch.getId());

        assertNull("should have null as current revision id", repository.getCurrentRevisionId());
    }

    @Test
    public void addCommit() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        Commit firstCommit = repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());

        assertEquals("should have first commit id as current revision id",
                repository.getCurrentRevisionId(), (Long) firstCommit.getId());
    }

    @Test(expected = RuntimeException.class)
    public void addCommitToClosedBranch() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.closeBranch();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
    }

    @Test(expected = RuntimeException.class)
    public void changeRevisionToNonExistedRevision() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        repository.changeRevision(-1);
    }

    @Test
    public void changeRevisionToCommit() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        Commit firstCommit = repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        repository.addCommit("second commit", new ArrayList<>(), new ArrayList<>());

        repository.changeRevision(firstCommit.getId());

        assertEquals("should have first commit id as current revision id",
                (Long) firstCommit.getId(), repository.getCurrentRevisionId());
        assertEquals("should have master branch id as current branch id",
                (Long) firstCommit.getBranchId(), repository.getCurrentBranchId());
    }

    @Test
    public void changeRevisionToBranch() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        Commit firstCommit = repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());

        repository.addBranch("second branch");
        repository.addCommit("second commit", new ArrayList<>(), new ArrayList<>());

        repository.changeRevision(firstCommit.getBranchId());

        assertEquals("should have first commit id as current revision id",
                (Long) firstCommit.getId(), repository.getCurrentRevisionId());
        assertEquals("should have master branch id as current branch id",
                (Long) firstCommit.getBranchId(), repository.getCurrentBranchId());
    }

    @Test
    public void commitAfterChangeRevision() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        Commit firstCommit = repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        repository.addCommit("second commit", new ArrayList<>(), new ArrayList<>());

        repository.changeRevision(firstCommit.getId());

        Commit thirdCommit = repository.addCommit("third commit", new ArrayList<>(), new ArrayList<>());

        assertEquals("should have first commit id as parent commit id",
                (Long) firstCommit.getId(), thirdCommit.getParentId());
        assertEquals("should have master branch id as branch id",
                (Long) firstCommit.getBranchId(), (Long) thirdCommit.getBranchId());
    }

    @Test
    public void getBranches() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        Branch secondBranch = repository.addBranch("second branch");
        repository.addCommit("second commit", new ArrayList<>(), new ArrayList<>());
        Branch thirdBranch = repository.addBranch("third branch");

        assertEquals("should have 3 branches", 3, repository.getBranches().size());
        ModelUtils.branchEquals(secondBranch, repository.getBranches().get(1));
        ModelUtils.branchEquals(thirdBranch, repository.getBranches().get(2));
    }

    @Test
    public void getCommitById() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        Commit commit = repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        Optional<Commit> result = repository.getCommitById(commit.getId());
        assertTrue("should be present", result.isPresent());
        ModelUtils.commitEquals(commit, result.get());
    }

    @Test
    public void getCommitByNonExistId() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        Optional<Commit> result = repository.getCommitById(-1);
        assertTrue("should be not present", !result.isPresent());
    }

    @Test
    public void getBranchById() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        Optional<Branch> result = repository.getBranchById(repository.getCurrentBranchId());
        assertTrue("should be present", result.isPresent());
    }

    @Test
    public void getBranchByNonExistId() throws Exception {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        Optional<Branch> result = repository.getBranchById(-1);
        assertTrue("should be not present", !result.isPresent());
    }

}