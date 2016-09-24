package homework2.command.impl;

import homework2.app.ConsoleExecutor;
import org.junit.Test;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class InitCommandTest {


    @Test
    public void execute() throws Exception {
        new ConsoleExecutor().run(new String[]{"init"});
    }

}