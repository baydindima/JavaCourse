package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitriy Baidin on 10/4/2016.
 */
public class StatusCommand implements Command {

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

        System.out.println("Changed files:");
        for (FileInfo changedFile : changedFiles) {
            System.out.println(changedFile.getPath());
        }

        System.out.println("Removed files:");
        for (FileInfo removedFile : removedFiles) {
            System.out.println(removedFile.getPath());
        }

        System.out.println("Added files:");
        addedFiles.forEach(System.out::println);

        return null;
    }
}
