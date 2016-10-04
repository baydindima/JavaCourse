package homework2.app;

import homework2.model.Repository;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This is class-holder of all util classes
 */
@Getter
@AllArgsConstructor
public class VersionControlSystem {
    private final AddedFilesManager addedFilesManager;
    private final CommitPorter commitPorter;
    private final CommitTreeCrawler commitTreeCrawler;
    private final FileSystem fileSystem;
    private final RepositoryLoader repositoryLoader;
    private final Repository repository;
}
