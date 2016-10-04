package homework2.app;

import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

/**
 * This class copy files from commit dir to project directory and back
 */
public class CommitPorter {
    private final FileSystem fileSystem;

    CommitPorter(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public void copyFilesToCommitDir(Commit commit) {
        Path commitDirPath = getCommitDirPath(commit);
        Path currentDirPath = fileSystem.getCurrentDirPath();

        fileSystem.createDirs(commitDirPath.toFile());
        try {
            for (FileInfo fileInfo : commit.getFiles()) {
                Path commitPath = Paths.get(commitDirPath.toString(), fileInfo.getPath());
                //noinspection ResultOfMethodCallIgnored
                commitPath.getParent().toFile().mkdirs();
                Files.copy(Paths.get(currentDirPath.toString(), fileInfo.getPath()),
                        commitPath, COPY_ATTRIBUTES);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void copyFilesFromCommitDirs(Map<FileInfo, Commit> fileInfoCommitMap) {
        Path currentDirPath = fileSystem.getCurrentDirPath();

        try {
            for (Map.Entry<FileInfo, Commit> entry : fileInfoCommitMap.entrySet()) {
                Path commitPath = Paths.get(getCommitDirPath(entry.getValue()).toString(), entry.getKey().getPath());
                Path filePath = Paths.get(currentDirPath.toString(), entry.getKey().getPath());
                //noinspection ResultOfMethodCallIgnored
                filePath.getParent().toFile().mkdirs();
                Files.copy(commitPath,
                        filePath, COPY_ATTRIBUTES);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public Path getCommitDirPath(Commit commit) {
        return Paths.get(fileSystem.getVcsDirPath().toString(), String.valueOf(commit.getId()));
    }
}
