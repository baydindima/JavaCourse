package homework2.utils;

import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class FileUtils {
    private static final String VSC_DIR_NAME = ".vcs";


    private FileUtils() {
    }

    private static Path getCurrentDirPath() {
        return Paths.get(".").toAbsolutePath().normalize();
    }

    public static Path getVcsDirPath() {
        return Paths.get("./" + VSC_DIR_NAME).toAbsolutePath().normalize();
    }

    public static void createDirs(File file) {
        if (!file.mkdirs()) {
            throw new RuntimeException("Failed to create dir");
        }
    }

    static Path getPathFromCommitAndFileInfo(Commit commit, FileInfo fileInfo) {
        return Paths.get(getVcsDirPath().toString(), String.valueOf(commit.getId()), fileInfo.getPath());
    }

}
