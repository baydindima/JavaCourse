package homework2.app;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static homework2.app.FileSystem.ADDED_FILES_FILE_NAME;

/**
 * @author Dmitriy Baidin on 10/3/2016.
 */
public class AddedFilesManager {
    private final FileSystem fileSystem;

    public AddedFilesManager(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public List<String> getAddedFiles() {
        File addedFiles = new File(fileSystem.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);

        if (!addedFiles.exists()) {
            return Collections.emptyList();
        }

        try (FileReader fileReader = new FileReader(addedFiles)) {
            BufferedReader bufferReader = new BufferedReader(fileReader);
            return bufferReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void setAddedFiles(List<String> files) {
        File addedFiles = new File(fileSystem.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);

        try (FileWriter fileWriter = new FileWriter(addedFiles, true)) {
            if (!addedFiles.exists()) {
                //noinspection ResultOfMethodCallIgnored
                addedFiles.createNewFile();
            }
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            for (String filePath : files) {
                bufferWriter.write(filePath + "\n");
            }
            bufferWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void clearAddedFiles() {
        File addedFiles = new File(fileSystem.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);
        if (addedFiles.exists()) {
            //noinspection ResultOfMethodCallIgnored
            addedFiles.delete();
        }
    }
}
