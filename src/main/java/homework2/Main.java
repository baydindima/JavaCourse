package homework2;

import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;
import homework2.app.VersionControlSystem;

/**
 * Main class for application
 */
public class Main {

    public static void main(String[] args) {
        VersionControlSystem versionControlSystem = BackendBuilder.build();
        new ConsoleExecutor().run(args, versionControlSystem);
    }

}
