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
    private final List<Commit> commits;

    public Branch(long id, String name, List<Commit> commits) {
        this.id = id;
        this.name = name;
        this.commits = commits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        return id == branch.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static Branch newBranch(String name) {
        long branchId = ThreadLocalRandom.current().nextLong(0, java.lang.Long.MAX_VALUE);
        return new Branch(branchId,
                name,
                new ArrayList<>());
    }
}
