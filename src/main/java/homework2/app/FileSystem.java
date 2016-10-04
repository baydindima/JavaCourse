package homework2.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This class working with file system
 */
public class FileSystem {
    static final String ADDED_FILES_FILE_NAME = "added_files";
    static final String REPOSITORY_INFO_NAME = "info";
    private static final String VSC_DIR_NAME = ".vcs";
    private final File curDir;

    public FileSystem(File curDir) {
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

    /**
     * Delete all files in directory except vcs directory
     */
    public void clearProject() {
        try {
            Files.walkFileTree(getCurrentDirPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.toFile().getName().equals(VSC_DIR_NAME)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (!dir.equals(getCurrentDirPath())) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear project", e);
        }
    }

}
