package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;
import homework2.model.Branch;

/**
 * Create new branch
 */
public class CreateBranchCommand implements Command {

    /**
     * Create new branch
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new InvalidArgumentsException("Should contain branch name", args);
        }

        if (args.length > 1) {
            throw new InvalidArgumentsException("Should contain only 1 arg", args);
        }

        Branch branch = versionControlSystem.getRepository().addBranch(args[0]);

        return "Branch " + branch.getId() + " created";
    }
}
