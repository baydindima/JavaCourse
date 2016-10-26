package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;

import java.io.File;
import java.nio.file.Path;

/**
 * Remove file from project
 */
public class RmCommand implements Command {

    /**
     * Remove file from project
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new InvalidArgumentsException("Nothing to delete", args);
        }

        Path currentDirPath = versionControlSystem.getFileSystem().getCurrentDirPath();
        for (String filePath : args) {
            if (!new File(currentDirPath.toString(), filePath).exists()) {
                throw new InvalidArgumentsException(String.format("No such file:%s", filePath), args);
            }
            //noinspection ResultOfMethodCallIgnored
            new File(currentDirPath.toString(), filePath).delete();
        }

        return "File deleted";
    }
}
