package homework2.command.impl;

import homework2.command.Command;
import homework2.model.Branch;
import homework2.model.Commit;
import homework2.model.Repository;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class LogCommand implements Command {
    @Override
    public String execute(Repository repository, String[] args) {
        Branch branch = repository.getBranchById(repository.getCurrentBranchId());

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
