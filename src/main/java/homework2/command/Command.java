package homework2.command;

import homework2.model.Repository;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public interface Command {

    String execute(Repository repository, String[] args);

}
