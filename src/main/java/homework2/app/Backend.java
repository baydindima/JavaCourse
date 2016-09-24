package homework2.app;

import homework2.model.Repository;
import homework2.utils.FileUtils;
import homework2.utils.RepositoryUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
@Getter
@AllArgsConstructor
public class Backend {
    private final FileUtils fileUtils;
    private final RepositoryUtils repositoryUtils;
    private final Repository repository;
}
