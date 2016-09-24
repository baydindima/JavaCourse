package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class MergeCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        backend.getRepositoryUtils().checkRepositoryInit();

        if (args.length != 2) {
            throw new RuntimeException("Merge should contain 2 args");
        }

        long firstId = Long.valueOf(args[0]);
        Commit firstCommit = backend.getRepository().getCommitById(firstId);
        if (firstCommit == null) {
            throw new RuntimeException(String.format("No commit with such id%d", firstId));
        }

        long secondId = Long.valueOf(args[1]);
        Commit secondCommit = backend.getRepository().getCommitById(secondId);
        if (secondCommit == null) {
            throw new RuntimeException("No commit with such id" + secondId);
        }

        List<Commit> firstPath = backend.getRepositoryUtils()
                .getCommitPath(backend.getRepository(), firstId);
        List<Commit> secondPath = backend.getRepositoryUtils()
                .getCommitPath(backend.getRepository(), secondId);

        int i = firstPath.size() - 1;
        int j = secondPath.size() - 1;
        while (firstPath.get(i).equals(secondPath.get(j))) {
            if (i > 0) i--;
            if (j > 0) j--;
        }

        Commit lca;
        if (i == 0 || j == 0) {
            lca = i == 0 ? firstPath.get(i) : secondPath.get(j);
        } else {
            lca = firstPath.get(i + 1);
        }

        backend.getFileUtils().clearProject();

        Map<FileInfo, Commit> fileInfoCommitMap = backend.getRepositoryUtils()
                .collectFiles(backend.getRepository(), lca.getId());

        backend.getRepositoryUtils().copyFilesFromCommitDirs(fileInfoCommitMap);

        return "Merged to lca";
    }
}
