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


/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class PersistentTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final RepositoryWriter writer = new RepositoryWriter();
    private final RepositoryReader reader = new RepositoryReader();

    @Test
    public void singleCommitTest() throws IOException {
        Repository repository = new InMemoryRepository();
        repository.addCommit("first commit");

        File file = folder.newFile("oneCommit");
        writer.write(repository, new FileOutputStream(file));

        Repository repositoryFromFile = reader.read(new FileInputStream(file));

        ModelUtils.repositoryEquals(repository, repositoryFromFile);

        repositoryFromFile.addBranch("new Branch");
        repositoryFromFile.addCommit("after load commit");
    }

    @Test
    public void twoBranchTest() throws IOException {
        InMemoryRepository repository = new InMemoryRepository();
        repository.addCommit("first commit");
        repository.addCommit("second commit");
        repository.addCommit("third commit");

        repository.addBranch("second branch");
        repository.addCommit("first commit");
        repository.addCommit("second commit");
        repository.addCommit("third commit");

        File file = folder.newFile("twoBranch");
        writer.write(repository, new FileOutputStream(file));

        Repository repositoryFromFile = reader.read(new FileInputStream(file));

        ModelUtils.repositoryEquals(repository, repositoryFromFile);

        repositoryFromFile.addBranch("new Branch");
        repositoryFromFile.addCommit("after load commit");
    }

}
