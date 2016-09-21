package homework2;

import homework2.model.Branch;
import homework2.model.Commit;
import homework2.model.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Baidin Dima
 */
public class Main {

    private static List<Commit> createCommits(int commitCount, Branch branch, long parentId) {
        List<Commit> result = new ArrayList<>();
        for (int i = 0; i < commitCount; i++) {
            Commit commit = Commit.newCommit(i + "-" + branch.getName(), branch.getId(), parentId);
            result.add(Commit.newCommit(i + "-" + branch.getName(), branch.getId(), parentId));
            parentId = commit.getId();
        }
        return result;
    }

    private static Branch createBranchWithCommits(String name, int commitCount, long parentId) {
        Branch branch = Branch.newBranch(name);
        branch.getCommits().addAll(createCommits(commitCount, branch, parentId));
        return branch;
    }

    private static Repository createTestRepository() {
        Branch first = createBranchWithCommits("first", 10, 0);
        return Repository.newRepository(Collections.singletonList(first),
                first.getCommits().get(first.getCommits().size() - 1).getId());
    }

    public static void main(String[] args) {
        Repository testRepository = createTestRepository();
    }

}
