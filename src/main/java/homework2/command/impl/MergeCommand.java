package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class MergeCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        backend.getRepositoryUtils().checkRepositoryInit();
        return null;
    }
}
