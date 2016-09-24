package homework2.command.impl;

import homework2.command.Command;
import homework2.model.Repository;
import homework2.utils.FileUtils;
import homework2.utils.RepositoryUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class AddCommand implements Command {
    @Override
    public String execute(Repository repository, String[] args) {
        RepositoryUtils.checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Nothing to add");
        }

        Path currentDirPath = FileUtils.getCurrentDirPath();
        for (String filePath : args) {
            if (!new File(currentDirPath.toString(), filePath).exists()) {
                throw new RuntimeException(String.format("No such file:%s", filePath));
            }
        }

        FileUtils.addToAddedFiles(Arrays.asList(args));

        return "Files added";
    }
}
