package homework2.utils;

import homework2.model.Commit;
import homework2.model.FileInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class FileUtils {
    private static final String VSC_DIR_NAME = ".vcs";


    private FileUtils() {
    }

    public static Path getCurrentDirPath() {
        return Paths.get(".").toAbsolutePath().normalize();
    }

    public static Path getVcsDirPath() {
        return Paths.get("./" + VSC_DIR_NAME).toAbsolutePath().normalize();
    }

    public static void createDirs(File file) {
        if (!file.mkdirs()) {
            throw new RuntimeException("Failed to create dir");
        }
    }

    public static void copyFilesToCommitDir(Commit commit) {
        Path commitDirPath = getCommitDirPath(commit);
        Path currentDirPath = getCurrentDirPath();

        createDirs(commitDirPath.toFile());
        try {
            for (FileInfo fileInfo : commit.getFiles()) {
                Files.copy(Paths.get(currentDirPath.toString(), fileInfo.getPath()),
                        Paths.get(commitDirPath.toString(), fileInfo.getPath()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    static Path getCommitDirPath(Commit commit) {
        return Paths.get(getVcsDirPath().toString(), String.valueOf(commit.getId()));
    }

    static Path getPathFromCommitAndFileInfo(Commit commit, FileInfo fileInfo) {
        return Paths.get(getVcsDirPath().toString(), String.valueOf(commit.getId()), fileInfo.getPath());
    }

    public static void addToAddedFiles(List<String> files) {
        File addedFiles = new File(FileUtils.getVcsDirPath().toFile(), "added_files");

        try (FileWriter fileWriter = new FileWriter(addedFiles.getName(), true)) {
            if (!addedFiles.exists()) {
                //noinspection ResultOfMethodCallIgnored
                addedFiles.createNewFile();
            }
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            for (String filePath : files) {
                bufferWriter.write(filePath + "/n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<String> getAddedFiels() {
        File addedFiles = new File(FileUtils.getVcsDirPath().toFile(), "added_files");

        if (!addedFiles.exists()) {
            return new ArrayList<>();
        }
        try (FileReader fileReader = new FileReader(addedFiles.getName())) {
            BufferedReader bufferReader = new BufferedReader(fileReader);
            return bufferReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
