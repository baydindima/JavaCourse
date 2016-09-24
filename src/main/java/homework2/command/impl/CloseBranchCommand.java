package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CloseBranchCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        if (args.length > 0) {
            throw new RuntimeException("Close branch doesn't take args");
        }

        backend.getRepository().closeBranch();

        return "Branch is closed";
    }
}
