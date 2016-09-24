package homework2.command.impl;

import homework2.app.Backend;
import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class AddCommandTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Rule
    public InitRepositoryRule repositoryRule = new InitRepositoryRule();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void execute() throws Exception {
        repositoryRule.init(folder.getRoot());

        new File(folder.getRoot(), "first file").createNewFile();
        new File(folder.newFolder("folder"), "in").createNewFile();

        Backend backend = new BackendBuilder().build(folder.getRoot());
        new ConsoleExecutor().run(new String[]{"add", "first file", "folder/in"},
                backend);

        List<String> addedFiles = backend.getRepositoryUtils().getAddedFiles();
        assertEquals("should add 2 files", 2, addedFiles.size());

        for (String addedFile : addedFiles) {
            File file = Paths.get(backend.getFileUtils().getCurrentDirPath().toString(), addedFile).toFile();
            assertTrue("that file should exist", file.exists());
        }
    }

}