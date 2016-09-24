package homework2.command.impl;

import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;
import homework2.model.Repository;
import homework2.utils.FileUtils;
import homework2.utils.RepositoryUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CommitCommand implements Command {
    @Override
    public String execute(Repository repository, String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Commit should contain message");
        }

        if (args.length > 1) {
            throw new RuntimeException("Commit take only 1 arg");
        }

        String message = args[0];

        Map<FileInfo, Commit> fileInfoCommitMap = RepositoryUtils.collectFiles(repository);

        List<FileInfo> changedFiles = new ArrayList<>();
        List<FileInfo> removedFiles = new ArrayList<>();

        Path currentDirPath = FileUtils.getCurrentDirPath();
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

        List<String> addedFiles = FileUtils.getAddedFiels();
        for (String addedFile : addedFiles) {
            File file = new File(currentDirPath.toFile(), addedFile);
            if (file.exists()) {
                String relativePath = currentDirPath.relativize(Paths.get(file.getAbsolutePath())).toString();
                changedFiles.add(new FileInfo(relativePath, file.lastModified()));
            }

        }

        repository.addCommit(message, changedFiles, removedFiles);
        return "Commit added";
    }
}
