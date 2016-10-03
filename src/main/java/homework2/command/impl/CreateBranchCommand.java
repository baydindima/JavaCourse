package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.model.Branch;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CreateBranchCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Should contain branch name");
        }

        if (args.length > 1) {
            throw new RuntimeException("Should contain only 1 arg");
        }

        Branch branch = versionControlSystem.getRepository().addBranch(args[0]);

        return "Branch " + branch.getId() + " created";
    }
}
