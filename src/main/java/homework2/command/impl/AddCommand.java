package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class AddCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        backend.getRepositoryUtils().checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Nothing to add");
        }

        Path currentDirPath = backend.getFileUtils().getCurrentDirPath();
        for (String filePath : args) {
            if (!new File(currentDirPath.toString(), filePath).exists()) {
                throw new RuntimeException(String.format("No such file:%s", filePath));
            }
        }

        List<String> addedFiles = backend.getRepositoryUtils().getAddedFiles();

        Set<String> newFiles = new HashSet<>(addedFiles);
        newFiles.addAll(Arrays.asList(args));

        backend.getRepositoryUtils().setToAddedFiles(new ArrayList<>(newFiles));

        return "Files added";
    }
}
