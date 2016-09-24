package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.util.Map;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CheckoutCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        backend.getRepositoryUtils().checkRepositoryInit();

        if (args.length == 0) {
            throw new RuntimeException("Should contain revision id");
        }

        if (args.length > 1) {
            throw new RuntimeException("Checkout takes only one arg");
        }

        long revisionId = Long.valueOf(args[0]);

        backend.getRepository().changeRevision(revisionId);

        Map<FileInfo, Commit> fileInfoCommitMap =
                backend.getRepositoryUtils().collectFiles(backend.getRepository());

        backend.getFileUtils().clearProject();
        backend.getRepositoryUtils().copyFilesFromCommitDirs(fileInfoCommitMap);

        return String.format("Checkout to %d", revisionId);
    }
}
