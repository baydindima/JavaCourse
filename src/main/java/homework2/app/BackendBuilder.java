package homework2.app;

import homework2.model.Repository;
import homework2.utils.FileUtils;
import homework2.utils.RepositoryUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class BackendBuilder {

    public Backend build() throws FileNotFoundException {
        FileUtils fileUtils = new FileUtils(new File("."));
        RepositoryUtils repositoryUtils = new RepositoryUtils(fileUtils);
        Repository repository = repositoryUtils.getRepository();
        return new Backend(fileUtils, repositoryUtils, repository);
    }
}
