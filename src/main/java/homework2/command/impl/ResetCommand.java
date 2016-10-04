package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

/**
 * @author Dmitriy Baidin on 10/4/2016.
 */
public class ResetCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Nothing to add");
        }

        Map<FileInfo, Commit> fileInfoCommitMap = versionControlSystem.getCommitTreeCrawler()
                .collectFiles(versionControlSystem.getRepository());

        Map<String, Commit> pathToCommit = new HashMap<>();
        for (Map.Entry<FileInfo, Commit> entry : fileInfoCommitMap.entrySet()) {
            pathToCommit.put(entry.getKey().getPath(), entry.getValue());
        }

        Path currentDirPath = versionControlSystem.getFileSystem().getCurrentDirPath();
        for (String filePath : args) {
            if (!new File(currentDirPath.toString(), filePath).exists()) {
                throw new RuntimeException(String.format("No such file:%s", filePath));
            }
            Commit commit = pathToCommit.get(filePath);
            if (commit == null) {
                throw new RuntimeException(String.format("No previous version for file: %s", filePath));
            }

            try {
                //noinspection ResultOfMethodCallIgnored
                new File(currentDirPath.toString(), filePath).delete();

                Path commitDirPath = versionControlSystem.getCommitPorter().getCommitDirPath(commit);

                Files.copy(Paths.get(currentDirPath.toString(), filePath),
                        Paths.get(commitDirPath.toString(), filePath), COPY_ATTRIBUTES);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return "File has been resetted";
    }
}
