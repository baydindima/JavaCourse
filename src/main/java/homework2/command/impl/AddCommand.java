package homework2.command.impl;

import homework2.command.Command;
import homework2.utils.FileUtils;
import homework2.utils.RepositoryUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class AddCommand implements Command {
    @Override
    public String execute(String[] args) {
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

        File addedFiles = new File(FileUtils.getVcsDirPath().toFile(), "added_files");

        try {
            if (!addedFiles.exists()) {
                //noinspection ResultOfMethodCallIgnored
                addedFiles.createNewFile();
            }

            FileWriter fileWritter = new FileWriter(addedFiles.getName(), true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            for (String filePath : args) {
                bufferWritter.write(filePath + "/n");
            }
            bufferWritter.close();

            return "Files added";
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }


    }
}
