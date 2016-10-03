package homework2.app;

import homework2.model.InMemoryRepository;
import homework2.model.Repository;
import homework2.storage.RepositoryReader;
import homework2.storage.RepositoryWriter;

import java.io.*;

import static homework2.app.FileSystem.REPOSITORY_INFO_NAME;

/**
 * @author Dmitriy Baidin on 10/3/2016.
 */
public class RepositoryLoader {
    private final FileSystem fileSystem;

    public RepositoryLoader(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public void checkRepositoryInit() {
        if (!fileSystem.getVcsDirPath().toFile().exists()) {
            throw new RuntimeException("Repository hasn't been init");
        }
    }

    public Repository loadRepository() {
        if (fileSystem.getVcsDirPath().toFile().exists()) {
            try {
                return new RepositoryReader()
                        .read(new FileInputStream(
                                new File(fileSystem.getVcsDirPath().toString(), REPOSITORY_INFO_NAME))
                        );
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            return new InMemoryRepository();
        }
    }

    public void saveRepository(Repository repository) throws IOException {
        File repositoryFile = new File(fileSystem.getVcsDirPath().toFile(), REPOSITORY_INFO_NAME);

        try (FileOutputStream outputStream = new FileOutputStream(repositoryFile, false)) {
            if (!repositoryFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                repositoryFile.createNewFile();
            }
            new RepositoryWriter().write(repository, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
