package homework2.command.impl;

import homework2.command.Command;
import homework2.model.Repository;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CloseBranchCommand implements Command {
    @Override
    public String execute(Repository repository, String[] args) {
        if (args.length > 0) {
            throw new RuntimeException("Close branch doesn't take args");
        }

        repository.closeBranch();

        return "Branch is closed";
    }
}
