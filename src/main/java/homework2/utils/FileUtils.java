package homework2.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class FileUtils {
    static final String ADDED_FILES_FILE_NAME = "added_files";
    static final String REPOSITORY_INFO_NAME = "info";
    private static final String VSC_DIR_NAME = ".vcs";
    private final File curDir;

    public FileUtils(File curDir) {
        this.curDir = curDir;
    }

    public Path getCurrentDirPath() {
        return Paths.get(curDir.getAbsolutePath()).toAbsolutePath().normalize();
    }

    public Path getVcsDirPath() {
        return Paths.get(getCurrentDirPath().toString(), VSC_DIR_NAME).toAbsolutePath().normalize();
    }

    public void createDirs(File file) {
        if (!file.mkdirs()) {
            throw new RuntimeException("Failed to create dir");
        }
    }

}
