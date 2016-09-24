package homework2;

import homework2.app.Backend;
import homework2.app.BackendBuilder;
import homework2.app.ConsoleExecutor;

/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class Main {

    public static void main(String[] args) {
        Backend backend = new BackendBuilder().build();
        new ConsoleExecutor().run(args, backend);
    }

}
