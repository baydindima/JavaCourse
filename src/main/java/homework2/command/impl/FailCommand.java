package homework2.command.impl;

import homework2.command.Command;
import homework2.model.Repository;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class FailCommand implements Command {
    @Override
    public String execute(Repository repository, String[] args) {
        return "No such command!";
    }
}
