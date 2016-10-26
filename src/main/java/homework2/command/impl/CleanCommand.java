package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.FileSystemIOException;
import homework2.exception.InvalidArgumentsException;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Remove all not trackable files from project directory
 */
public class CleanCommand implements Command {

    /**
     * Remove all not trackable files from project directory
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length > 0) {
            throw new InvalidArgumentsException("Clean doesn't take args", args);
        }

        Set<String> trackableFilesPath = new HashSet<>();

        Map<FileInfo, Commit> fileInfoCommitMap = versionControlSystem.getCommitTreeCrawler()
                .collectFiles(versionControlSystem.getRepository());

        trackableFilesPath.addAll(fileInfoCommitMap.keySet().stream().map(FileInfo::getPath).collect(Collectors.toList()));

        List<String> addedFiles = versionControlSystem.getAddedFilesManager().getAddedFiles();
        trackableFilesPath.addAll(addedFiles);

        try {
            Files.walkFileTree(versionControlSystem.getFileSystem().getCurrentDirPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!trackableFilesPath.contains(
                            versionControlSystem.getFileSystem().getCurrentDirPath().relativize(file).toString())) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.toFile().getName().equals(
                            versionControlSystem.getFileSystem().getVcsDirPath().getFileName().toString()
                    )) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new FileSystemIOException("Failed to clear project", e);
        }

        return "Project cleaned";
    }
}
