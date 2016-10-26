package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Commit current changes, add all added and changed files, stores information about removed files
 */
public class CommitCommand implements Command {

    /**
     * Commit current changes, add all added and changed files, stores information about removed files
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new InvalidArgumentsException("Commit should contain message", args);
        }

        if (args.length > 1) {
            throw new InvalidArgumentsException("Commit take only 1 arg", args);
        }

        String message = args[0];

        Map<FileInfo, Commit> fileInfoCommitMap = versionControlSystem.getCommitTreeCrawler()
                .collectFiles(versionControlSystem.getRepository());

        Set<FileInfo> changedFiles = new HashSet<>();
        Set<FileInfo> removedFiles = new HashSet<>();

        Path currentDirPath = versionControlSystem.getFileSystem().getCurrentDirPath();
        for (FileInfo fileInfo : fileInfoCommitMap.keySet()) {
            File file = new File(currentDirPath.toFile(), fileInfo.getPath());
            if (file.exists()) {
                if (file.lastModified() > fileInfo.getLastUpdated()) {
                    changedFiles.add(new FileInfo(fileInfo.getPath(), file.lastModified()));
                }
            } else {
                removedFiles.add(fileInfo);
            }
        }

        List<String> addedFiles = versionControlSystem.getAddedFilesManager().getAddedFiles();
        for (String addedFile : addedFiles) {
            File file = new File(currentDirPath.toFile(), addedFile);
            if (file.exists()) {
                String relativePath = currentDirPath
                        .relativize(Paths.get(file.getAbsolutePath())).toString();
                changedFiles.add(new FileInfo(relativePath, file.lastModified()));
            }

        }

        Commit commit = versionControlSystem.getRepository()
                .addCommit(message,
                        new ArrayList<>(changedFiles),
                        new ArrayList<>(removedFiles));

        versionControlSystem.getCommitPorter().copyFilesToCommitDir(commit);
        versionControlSystem.getAddedFilesManager().clearAddedFiles();

        return "Commit added";
    }
}
