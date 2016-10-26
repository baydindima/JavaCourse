package homework2.model;

import static org.junit.Assert.*;

/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class ModelUtils {

    private ModelUtils() {
    }

    public static void commitEquals(Commit expected, Commit actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getBranchId(), actual.getBranchId());
        assertEquals(expected.getParentId(), actual.getParentId());
        assertEquals(expected.getFiles().size(), actual.getFiles().size());
        for (int i = 0; i < expected.getFiles().size(); i++) {
            assertEquals(expected.getFiles().get(i), actual.getFiles().get(i));
        }
        assertEquals(expected.getRemovedFiles().size(), actual.getRemovedFiles().size());
        for (int i = 0; i < expected.getRemovedFiles().size(); i++) {
            assertEquals(expected.getRemovedFiles().get(i), actual.getRemovedFiles().get(i));
        }
    }

    public static void branchEquals(Branch expected, Branch actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getParentId(), actual.getParentId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCommits().size(), actual.getCommits().size());
        for (int i = 0; i < expected.getCommits().size(); i++) {
            commitEquals(expected.getCommits().get(i), actual.getCommits().get(i));
        }
    }


    public static void repositoryEquals(Repository expected, Repository actual) {
        assertEquals(expected.getCurrentRevisionId(), actual.getCurrentRevisionId());
        assertEquals(expected.getCurrentBranchId(), actual.getCurrentBranchId());
        assertEquals(expected.getBranches().size(), actual.getBranches().size());
        for (int i = 0; i < expected.getBranches().size(); i++) {
            branchEquals(expected.getBranches().get(i), actual.getBranches().get(i));
        }
    }
}
