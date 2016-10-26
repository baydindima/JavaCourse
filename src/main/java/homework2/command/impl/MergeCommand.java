package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;
import homework2.exception.RepositoryException;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Merge changes from 2 other commits
 */
public class MergeCommand implements Command {

    /**
     * Merge changes from 2 other commits
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        checkArgs(args);

        long firstId = versionControlSystem.getRepository().getCurrentRevisionId();
        Commit firstCommit = getCommit(firstId, versionControlSystem);

        long secondId = Long.valueOf(args[1]);
        Commit secondCommit = getCommit(secondId, versionControlSystem);

        if (firstId == secondId) {
            return "Merge completed";
        }

        if (firstCommit.getBranchId() == secondCommit.getBranchId()) {
            return "Both of commits placed in one branch";
        }

        List<Commit> firstPath = versionControlSystem.getCommitTreeCrawler()
                .getCommitPath(versionControlSystem.getRepository(), firstId);
        List<Commit> secondPath = versionControlSystem.getCommitTreeCrawler()
                .getCommitPath(versionControlSystem.getRepository(), secondId);

        int i = firstPath.size() - 1;
        int j = secondPath.size() - 1;
        while (firstPath.get(i).equals(secondPath.get(j))) {
            if (i > 0) i--;
            if (j > 0) j--;
        }

        Commit leastCommonAncestor;
        if (i == 0 || j == 0) {
            return "One commit ahead another";
        } else {
            leastCommonAncestor = firstPath.get(i + 1);
        }

        Map<FileInfo, Commit> commonCommitMap = versionControlSystem.getCommitTreeCrawler()
                .collectFiles(versionControlSystem.getRepository(), leastCommonAncestor.getId());

        Map<FileInfo, Commit> firstCommitMap = versionControlSystem.getCommitTreeCrawler()
                .getFilesUnionFromCommits(firstPath.subList(0, i));
        Map<FileInfo, Commit> secondCommitMap = versionControlSystem.getCommitTreeCrawler()
                .getFilesUnionFromCommits(secondPath.subList(0, j));

        Map<String, FileInfoWithCommit> firstPathCommit = getCommitMap(firstCommitMap);

        Map<String, FileInfoWithCommit> secondPathCommit = getCommitMap(secondCommitMap);

        updateRepository(firstPathCommit, secondPathCommit, commonCommitMap, versionControlSystem);


        return "Merged to leastCommonAncestor";
    }

    private Map<String, FileInfoWithCommit> getCommitMap(Map<FileInfo, Commit> fileInfoCommitMap) {
        return fileInfoCommitMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getPath(), e -> new FileInfoWithCommit(e.getKey(), e.getValue())));
    }


    private void checkArgs(String[] args) {
        if (args.length != 2) {
            throw new InvalidArgumentsException("Merge should contain 1 args", args);
        }
    }

    private Commit getCommit(long id, VersionControlSystem versionControlSystem) {
        Commit commit = versionControlSystem.getRepository().getCommitById(id);
        if (commit == null) {
            throw new RepositoryException(String.format("No commit with such id %d", id));
        }
        return commit;
    }

    private void updateRepository(Map<String, FileInfoWithCommit> firstPathCommit,
                                  Map<String, FileInfoWithCommit> secondPathCommit,
                                  Map<FileInfo, Commit> commonCommitMap,
                                  VersionControlSystem versionControlSystem) {
        Scanner scanner = new Scanner(System.in);
        for (String path : firstPathCommit.keySet()) {
            if (secondPathCommit.containsKey(path)) {
                System.out.println("To select file " + path + " from first commit press 1");
                String s = scanner.nextLine();

                FileInfoWithCommit fileInfoWithCommit;
                if ("1".equals(s.trim())) {
                    fileInfoWithCommit = firstPathCommit.get(path);
                } else {
                    fileInfoWithCommit = secondPathCommit.get(path);
                }
                commonCommitMap.put(fileInfoWithCommit.fileInfo, fileInfoWithCommit.commit);

            } else {
                FileInfoWithCommit fileInfoWithCommit = firstPathCommit.get(path);
                commonCommitMap.put(fileInfoWithCommit.fileInfo, fileInfoWithCommit.commit);
            }
        }

        secondPathCommit.keySet().stream().filter(path -> !firstPathCommit.containsKey(path)).forEach(path -> {
            FileInfoWithCommit fileInfoWithCommit = secondPathCommit.get(path);
            commonCommitMap.put(fileInfoWithCommit.fileInfo, fileInfoWithCommit.commit);
        });


        versionControlSystem.getFileSystem().clearProject();
        versionControlSystem.getCommitPorter().copyFilesFromCommitDirs(commonCommitMap);
    }

    private class FileInfoWithCommit {
        private final FileInfo fileInfo;
        private final Commit commit;

        private FileInfoWithCommit(FileInfo fileInfo, Commit commit) {
            this.fileInfo = fileInfo;
            this.commit = commit;
        }
    }
}
