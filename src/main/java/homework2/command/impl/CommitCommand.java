package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

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
    public String execute(Backend backend, String[] args) {
        backend.getRepositoryUtils().checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Commit should contain message");
        }

        if (args.length > 1) {
            throw new RuntimeException("Commit take only 1 arg");
        }

        String message = args[0];

        Map<FileInfo, Commit> fileInfoCommitMap = backend.getRepositoryUtils()
                .collectFiles(backend.getRepository());

        List<FileInfo> changedFiles = new ArrayList<>();
        List<FileInfo> removedFiles = new ArrayList<>();

        Path currentDirPath = backend.getFileUtils().getCurrentDirPath();
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

        List<String> addedFiles = backend.getRepositoryUtils().getAddedFiles();
        for (String addedFile : addedFiles) {
            File file = new File(currentDirPath.toFile(), addedFile);
            if (file.exists()) {
                String relativePath = currentDirPath
                        .relativize(Paths.get(file.getAbsolutePath())).toString();
                changedFiles.add(new FileInfo(relativePath, file.lastModified()));
            }

        }

        Commit commit = backend.getRepository().addCommit(message, changedFiles, removedFiles);

        backend.getRepositoryUtils().copyFilesToCommitDir(commit);

        return "Commit added";
    }
}
