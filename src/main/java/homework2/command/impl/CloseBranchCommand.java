package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CloseBranchCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length > 0) {
            throw new RuntimeException("Close branch doesn't take args");
        }

        versionControlSystem.getRepository().closeBranch();

        return "Branch is closed";
    }
}
