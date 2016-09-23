package homework2.utils;

import homework2.model.Commit;
import homework2.model.FileInfo;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class FileUtils {

    private FileUtils() {
    }

    static Path getPathFromCommitAndFileInfo(Commit commit, FileInfo fileInfo) {
        return Paths.get("./.vcs/" + String.valueOf(commit.getId()) + fileInfo.getPath());
    }

}
