package homework2.command.impl;

import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;
import homework2.app.VersionControlSystem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitriy Baidin on 10/4/2016.
 */
public class CleanCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public InitRepositoryRule repositoryRule = new InitRepositoryRule();


    @Test
    public void execute() throws Exception {
        repositoryRule.init(folder.getRoot());

        ArrayList<String> files = new ArrayList<>();
        files.add("first");
        addFiles(files);

        VersionControlSystem versionControlSystem = BackendBuilder.build(folder.getRoot());
        List<String> addedFiles = versionControlSystem.getAddedFilesManager().getAddedFiles();
        new ConsoleExecutor().run(new String[]{"commit", "first commit"}, versionControlSystem);

        folder.newFile("second");

        new ConsoleExecutor().run(new String[]{"clean"}, versionControlSystem);

        assertFalse("that file shouldn't exist", Paths.get(
                versionControlSystem.getFileSystem()
                        .getCurrentDirPath()
                        .toString(),
                "second").toFile().exists());

        for (String addedFile : addedFiles) {
            File file = Paths.get(
                    versionControlSystem.getFileSystem()
                            .getCurrentDirPath()
                            .toString(),
                    addedFile).toFile();
            assertTrue("that file should exist", file.exists());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void addFiles(List<String> fileNames) throws IOException {
        String[] args = new String[fileNames.size() + 1];
        args[0] = "add";

        int i = 0;
        for (String fileName : fileNames) {
            File file = new File(folder.getRoot(), fileName);
            file.getParentFile().mkdirs();
            file.createNewFile();
            args[++i] = fileName;
        }

        VersionControlSystem versionControlSystem = BackendBuilder.build(folder.getRoot());
        new ConsoleExecutor().run(args, versionControlSystem);
    }

}
