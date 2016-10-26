package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;
import homework2.model.Branch;
import homework2.model.Commit;

/**
 * Return history of all commits from current commit to root commit
 */
public class LogCommand implements Command {

    /**
     * Return history of all commits from current commit to root commit
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();
        if (args.length > 0) {
            throw new InvalidArgumentsException("Log doesn't take args", args);
        }

        Branch branch = versionControlSystem.getRepository()
                .getBranchById(versionControlSystem.getRepository().getCurrentBranchId());

        StringBuilder builder = new StringBuilder("log\n");

        for (Commit commit : branch.getCommits()) {
            builder.append(commit.getId())
                    .append(": ")
                    .append(commit.getMessage())
                    .append("\n");
        }

        return builder.toString();
    }
}
