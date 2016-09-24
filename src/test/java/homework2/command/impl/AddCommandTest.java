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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void execute() throws Exception {

        File root = folder.newFolder();

        Backend initBackend = new BackendBuilder().build(root);
        new ConsoleExecutor().run(new String[]{"init"}, initBackend);

        new File(root, "first file").createNewFile();
        File newFolder = new File(root, "folder");
        newFolder.mkdirs();
        new File(newFolder, "in").createNewFile();

        Backend addBackend = new BackendBuilder().build(root);
        new ConsoleExecutor().run(new String[]{"add", "first file", "folder/in"},
                addBackend);

        List<String> addedFiles = addBackend.getRepositoryUtils().getAddedFiles();
        assertEquals("should add 2 files", 2, addedFiles.size());

        for (String addedFile : addedFiles) {
            File file = Paths.get(addBackend.getFileUtils().getCurrentDirPath().toString(), addedFile).toFile();
            assertTrue("that file should exist", file.exists());
        }
    }

}