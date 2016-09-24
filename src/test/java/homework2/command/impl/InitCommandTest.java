package homework2.command.impl;

import homework2.app.Backend;
import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class InitCommandTest {


    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void execute() throws Exception {
        Backend backend = new BackendBuilder().build(folder.getRoot());
        new ConsoleExecutor().run(new String[]{"init"}, backend);

        backend.getRepositoryUtils().checkRepositoryInit();
    }

}