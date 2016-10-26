package homework2.app;

import homework2.model.Repository;

import java.io.File;

/**
 * Class for building context of VersionControlSystem
 */
public class BackendBuilder {


    /**
     * Build version control system in current directory
     *
     * @return context
     */
    public static VersionControlSystem build() {
        return build(new File("."));
    }

    /**
     * Build version control system in specified directory
     * @param rootDir path of root directory
     * @return context
     */
    public static VersionControlSystem build(File rootDir) {
        FileSystem fileSystem = new FileSystem(rootDir);
        AddedFilesManager addedFilesManager = new AddedFilesManager(fileSystem);
        CommitPorter commitPorter = new CommitPorter(fileSystem);
        CommitTreeCrawler commitTreeCrawler = new CommitTreeCrawler();
        RepositoryLoader repositoryLoader = new RepositoryLoader(fileSystem);
        Repository repository = repositoryLoader.loadRepository();

        return new VersionControlSystem(
                addedFilesManager,
                commitPorter,
                commitTreeCrawler,
                fileSystem,
                repositoryLoader,
                repository);
    }


}
