package homework2.command.impl;

import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;
import homework2.app.VersionControlSystem;
import homework2.model.Commit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class CommitCommandTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public InitRepositoryRule repositoryRule = new InitRepositoryRule();


    @Test
    public void executeWithSingleCommit() throws Exception {
        repositoryRule.init(folder.getRoot());

        ArrayList<String> files = new ArrayList<>();
        files.add("first");
        files.add("folder/in");

        addFiles(files);

        VersionControlSystem versionControlSystem = BackendBuilder.build(folder.getRoot());
        List<String> addedFiles = versionControlSystem.getUtilsToRemove().getAddedFiles();

        new ConsoleExecutor().run(new String[]{"commit", "first commit"},
                versionControlSystem);

        assertEquals("Added files should be empty", 0,
                versionControlSystem.getUtilsToRemove().getAddedFiles().size());

        Commit commit = versionControlSystem.getRepository().getBranches().get(0).getCommits().get(0);

        for (String addedFile : addedFiles) {
            File file = Paths.get(
                    versionControlSystem.getUtilsToRemove()
                            .getCommitDirPath(commit)
                            .toString(),
                    addedFile).toFile();
            assertTrue("that file should exist", file.exists());
        }
    }


    @Test
    public void executeWithTwoCommit() throws Exception {
        repositoryRule.init(folder.getRoot());

        ArrayList<String> files = new ArrayList<>();
        files.add("first");
        files.add("folder/in");

        addFiles(files);

        VersionControlSystem versionControlSystem = BackendBuilder.build(folder.getRoot());
        new ConsoleExecutor().run(new String[]{"commit", "first commit"}, versionControlSystem);

        ArrayList<String> secondFiles = new ArrayList<>();
        secondFiles.add("second");
        secondFiles.add("folder/out");

        addFiles(secondFiles);

        List<String> addedFiles = versionControlSystem.getUtilsToRemove().getAddedFiles();
        new ConsoleExecutor().run(new String[]{"commit", "second commit"}, versionControlSystem);

        Commit commit = versionControlSystem.getRepository().getBranches().get(0).getCommits().get(1);

        for (String addedFile : addedFiles) {
            File file = Paths.get(
                    versionControlSystem.getUtilsToRemove()
                            .getCommitDirPath(commit)
                            .toString(),
                    addedFile).toFile();
            assertTrue("that file should exist", file.exists());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void executeWithTwoBranches() throws Exception {
        repositoryRule.init(folder.getRoot());

        ArrayList<String> files = new ArrayList<>();
        files.add("first");
        files.add("folder/in");

        addFiles(files);

        VersionControlSystem versionControlSystem = BackendBuilder.build(folder.getRoot());
        new ConsoleExecutor().run(new String[]{"commit", "first commit"}, versionControlSystem);

        new File(folder.getRoot(), "first").delete();
        new File(folder.getRoot(), "folder/in").delete();

        new ConsoleExecutor().run(new String[]{"commit", "second commit"}, versionControlSystem);

        Commit commit = versionControlSystem.getRepository().getBranches().get(0).getCommits().get(1);

        assertEquals("should have 2 removed files", 2, commit.getRemovedFiles().size());
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