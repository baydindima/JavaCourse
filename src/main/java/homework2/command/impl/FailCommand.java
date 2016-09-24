package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class FailCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        return "No such command!";
    }
}
