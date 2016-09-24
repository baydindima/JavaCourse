package homework2;

import homework2.app.ConsoleExecutor;

/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class Main {

    public static void main(String[] args) {
        String[] initCommand = new String[]{"init"};
        new ConsoleExecutor().run(initCommand);
    }

}
