package homework2.command.impl;

import homework2.app.Backend;
import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;
import homework2.model.Commit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class CheckoutCommandTest {

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

        Backend backend = new BackendBuilder().build(folder.getRoot());
        new ConsoleExecutor().run(new String[]{"commit", "first commit"}, backend);

        ArrayList<String> secondFiles = new ArrayList<>();
        secondFiles.add("second");
        secondFiles.add("folder/out");

        addFiles(secondFiles);
        List<String> removedFiles = backend.getRepositoryUtils().getAddedFiles();
        new ConsoleExecutor().run(new String[]{"commit", "second commit"}, backend);

        Commit commit = backend.getRepository().getBranches().get(0).getCommits().get(0);

        new ConsoleExecutor().run(new String[]{"checkout", String.valueOf(commit.getId())}, backend);

        for (String removedFile : removedFiles) {
            File file = Paths.get(
                    backend.getFileUtils()
                            .getCurrentDirPath()
                            .toString(),
                    removedFile).toFile();
            assertTrue("that file shouldn't exist", !file.exists());
        }

        for (String addedFile : removedFiles) {
            File file = Paths.get(
                    backend.getFileUtils()
                            .getCurrentDirPath()
                            .toString(),
                    addedFile).toFile();
            assertTrue("that file shouldn't exist", !file.exists());
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

        Backend backend = new BackendBuilder().build(folder.getRoot());
        new ConsoleExecutor().run(args, backend);
    }

}