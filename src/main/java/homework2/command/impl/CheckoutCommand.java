package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.util.Map;

/**
 * Checkout to commit, or branch by commit id or branch name in args
 */
public class CheckoutCommand implements Command {

    /**
     * Checkout to commit, or branch by commit id or branch name in args
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new InvalidArgumentsException("Should contain revision id", args);
        }

        if (args.length > 1) {
            throw new InvalidArgumentsException("Checkout takes only one arg", args);
        }

        try {
            versionControlSystem.getRepository().changeRevision(Long.valueOf(args[0]));
        } catch (NumberFormatException e) {
            versionControlSystem.getRepository().changeRevision(args[0]);
        }


        Map<FileInfo, Commit> fileInfoCommitMap =
                versionControlSystem.getCommitTreeCrawler().collectFiles(versionControlSystem.getRepository());

        versionControlSystem.getFileSystem().clearProject();
        versionControlSystem.getCommitPorter().copyFilesFromCommitDirs(fileInfoCommitMap);

        return String.format("Checkout to %s", args[0]);
    }
}
