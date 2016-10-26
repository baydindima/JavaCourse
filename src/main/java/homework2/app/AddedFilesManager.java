package homework2.app;

import homework2.exception.FileSystemIOException;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static homework2.app.FileSystem.ADDED_FILES_FILE_NAME;

/**
 * This class works with files, which has been added but not committed
 */
public class AddedFilesManager {
    private final FileSystem fileSystem;

    AddedFilesManager(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Return all added files, but not commited files
     */
    public List<String> getAddedFiles() {
        File addedFiles = new File(fileSystem.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);

        if (!addedFiles.exists()) {
            return Collections.emptyList();
        }

        try (FileReader fileReader = new FileReader(addedFiles)) {
            BufferedReader bufferReader = new BufferedReader(fileReader);
            return bufferReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileSystemIOException(e.getMessage(), e);
        }
    }


    /**
     * Set new list of added files
     *
     * @param files list of files' paths that will be new added files
     */
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
            throw new FileSystemIOException(e.getMessage(), e);
        }
    }

    /**
     * Clear list of added files
     */
    public void clearAddedFiles() {
        File addedFiles = new File(fileSystem.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);
        if (addedFiles.exists()) {
            //noinspection ResultOfMethodCallIgnored
            addedFiles.delete();
        }
    }
}
