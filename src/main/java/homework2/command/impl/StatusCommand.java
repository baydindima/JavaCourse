package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * Return current status of repository.
 * All changed, added and removed files.
 */
public class StatusCommand implements Command {


    /**
     * Return current status of repository.
     * All changed, added and removed files.
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

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

        printCollection("Changed files", changedFiles);
        printCollection("Removed files", removedFiles);
        printCollection("Added files", addedFiles);

        return "";
    }

    private void printCollection(String collectionName, Collection<?> collection) {
        if (collection.size() > 0) {
            System.out.println(collectionName + ":");
            collection.forEach(System.out::println);
        }
    }

}
