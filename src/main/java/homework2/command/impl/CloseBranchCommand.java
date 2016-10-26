package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;

/**
 * Close branch
 */
public class CloseBranchCommand implements Command {
    /**
     * Close branch
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length > 0) {
            throw new InvalidArgumentsException("Close branch doesn't take args", args);
        }

        versionControlSystem.getRepository().closeBranch();

        return "Branch is closed";
    }
}
