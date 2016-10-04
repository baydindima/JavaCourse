package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
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
 * @author Dmitriy Baidin on 10/4/2016.
 */
public class CleanCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length > 0) {
            throw new RuntimeException("Clean doesn't take args");
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
            throw new RuntimeException("Failed to clear project", e);
        }

        return "Project cleaned";
    }
}
