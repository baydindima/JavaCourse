package homework2.command;

import homework2.app.Backend;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public interface Command {

    String execute(Backend backend, String[] args);

}
