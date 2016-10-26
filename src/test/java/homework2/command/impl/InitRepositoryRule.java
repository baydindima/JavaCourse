package homework2.command.impl;

import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;
import homework2.app.VersionControlSystem;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
class InitRepositoryRule implements TestRule {

    public void init(File folder) {
        VersionControlSystem initVersionControlSystem = BackendBuilder.build(folder);
        new ConsoleExecutor().run(new String[]{"init"}, initVersionControlSystem);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }
}
