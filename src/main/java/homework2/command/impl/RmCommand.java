package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Dmitriy Baidin on 10/4/2016.
 */
public class RmCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Nothing to delete");
        }

        Path currentDirPath = versionControlSystem.getFileSystem().getCurrentDirPath();
        for (String filePath : args) {
            if (!new File(currentDirPath.toString(), filePath).exists()) {
                throw new RuntimeException(String.format("No such file:%s", filePath));
            }
            //noinspection ResultOfMethodCallIgnored
            new File(currentDirPath.toString(), filePath).delete();
        }

        return "File deleted";
    }
}
