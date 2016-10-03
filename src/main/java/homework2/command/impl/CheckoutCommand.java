package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.util.Map;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CheckoutCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Should contain revision id");
        }

        if (args.length > 1) {
            throw new RuntimeException("Checkout takes only one arg");
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
