package homework2.app;

import homework2.model.Repository;

import java.io.File;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class BackendBuilder {


    public static VersionControlSystem build() {
        return build(new File("."));
    }

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
