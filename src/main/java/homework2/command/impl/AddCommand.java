package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * Add files in args to list of added files
 */
public class AddCommand implements Command {

    /**
     * Add files in args to list of added files
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new InvalidArgumentsException("Nothing to add", args);
        }

        Path currentDirPath = versionControlSystem.getFileSystem().getCurrentDirPath();
        for (String filePath : args) {
            if (!new File(currentDirPath.toString(), filePath).exists()) {
                throw new InvalidArgumentsException(String.format("No such file:%s", filePath), args);
            }
        }

        List<String> addedFiles = versionControlSystem.getAddedFilesManager().getAddedFiles();

        Set<String> newFiles = new HashSet<>(addedFiles);
        newFiles.addAll(Arrays.asList(args));

        versionControlSystem.getAddedFilesManager().setAddedFiles(new ArrayList<>(newFiles));

        return "Files added";
    }
}
