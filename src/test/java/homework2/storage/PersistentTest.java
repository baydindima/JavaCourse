package homework2.storage;

import homework2.model.InMemoryRepository;
import homework2.model.ModelUtils;
import homework2.model.Repository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class PersistentTest {

    private final RepositoryWriter writer = new RepositoryWriter();
    private final RepositoryReader reader = new RepositoryReader();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void singleCommitTest() throws IOException {
        Repository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());

        File file = folder.newFile("oneCommit");
        writer.write(repository, new FileOutputStream(file));

        Repository repositoryFromFile = reader.read(new FileInputStream(file));

        ModelUtils.repositoryEquals(repository, repositoryFromFile);

        repositoryFromFile.addBranch("new Branch");
        repositoryFromFile.addCommit("after load commit", new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void twoBranchTest() throws IOException {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        repository.addCommit("second commit", new ArrayList<>(), new ArrayList<>());
        repository.addCommit("third commit", new ArrayList<>(), new ArrayList<>());

        repository.addBranch("second branch");
        repository.addCommit("first commit", new ArrayList<>(), new ArrayList<>());
        repository.addCommit("second commit", new ArrayList<>(), new ArrayList<>());
        repository.addCommit("third commit", new ArrayList<>(), new ArrayList<>());

        File file = folder.newFile("twoBranch");
        writer.write(repository, new FileOutputStream(file));

        Repository repositoryFromFile = reader.read(new FileInputStream(file));

        ModelUtils.repositoryEquals(repository, repositoryFromFile);

        repositoryFromFile.addBranch("new Branch");
        repositoryFromFile.addCommit("after load commit", new ArrayList<>(), new ArrayList<>());
    }

}
