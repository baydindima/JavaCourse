package homework2.app;

import homework2.model.Repository;
import homework2.utils.FileUtils;
import homework2.utils.RepositoryUtils;

import java.io.File;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class BackendBuilder {


    public Backend build() {
        return build(new File("."));
    }

    public Backend build(File rootDir) {
        FileUtils fileUtils = new FileUtils(rootDir);
        RepositoryUtils repositoryUtils = new RepositoryUtils(fileUtils);
        Repository repository = repositoryUtils.loadRepository();
        return new Backend(fileUtils, repositoryUtils, repository);
    }


}
