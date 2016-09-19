package homework2.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Baidin Dima
 */
@Getter
public class Branch {
    private final long id;
    private final String name;
    private final List<Commit> commitsId;

    public Branch(long id, String name, List<Commit> commitsId) {
        this.id = id;
        this.name = name;
        this.commitsId = commitsId;
    }

    public static Branch newBranch(String name) {
        long branchId = ThreadLocalRandom.current().nextLong(0, java.lang.Long.MAX_VALUE);
        return new Branch(branchId,
                name,
                new ArrayList<>());
    }
}
