package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;
import homework2.model.Branch;
import homework2.model.Commit;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class LogCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        backend.getRepositoryUtils().checkRepositoryInit();
        if (args.length > 0) {
            throw new RuntimeException("Log doesn't take args");
        }

        Branch branch = backend.getRepository()
                .getBranchById(backend.getRepository().getCurrentBranchId());

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
