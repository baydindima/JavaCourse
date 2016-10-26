package homework2.app;

import homework2.exception.FileSystemIOException;
import homework2.exception.RepositoryException;
import homework2.model.InMemoryRepository;
import homework2.model.Repository;
import homework2.storage.RepositoryReader;
import homework2.storage.RepositoryWriter;

import java.io.*;

import static homework2.app.FileSystem.REPOSITORY_INFO_NAME;

/**
 * This class loads and saves rep
 */
public class RepositoryLoader {
    private final FileSystem fileSystem;

    RepositoryLoader(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Check initialization of repository in current directory
     */
    public void checkRepositoryInit() {
        if (!fileSystem.getVcsDirPath().toFile().exists()) {
            throw new RepositoryException("Repository hasn't been init");
        }
    }

    /**
     * Try load repository info from vcs directory, or else create new repository
     *
     * @return repository instance
     */
    public Repository loadRepository() {
        if (fileSystem.getVcsDirPath().toFile().exists()) {
            try {
                return new RepositoryReader()
                        .read(new FileInputStream(
                                new File(fileSystem.getVcsDirPath().toString(), REPOSITORY_INFO_NAME))
                        );
            } catch (FileNotFoundException e) {
                throw new FileSystemIOException(e.getMessage(), e);
            }
        } else {
            return new InMemoryRepository();
        }
    }

    /**
     * Save repository status to file system
     *
     * @param repository repository for save
     */
    public void saveRepository(Repository repository) {
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
