package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.model.Commit;
import homework2.model.FileInfo;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class MergeCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        versionControlSystem.getRepositoryLoader().checkRepositoryInit();

        if (args.length != 2) {
            throw new RuntimeException("Merge should contain 1 args");
        }

        long firstId = versionControlSystem.getRepository().getCurrentRevisionId();
        Commit firstCommit = versionControlSystem.getRepository().getCommitById(firstId);
        if (firstCommit == null) {
            throw new RuntimeException(String.format("No commit with such id %d", firstId));
        }

        long secondId = Long.valueOf(args[1]);
        Commit secondCommit = versionControlSystem.getRepository().getCommitById(secondId);
        if (secondCommit == null) {
            throw new RuntimeException(String.format("No commit with such id %d", secondId));
        }

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

        Commit lca;
        if (i == 0 || j == 0) {
            return "One commit ahead another";
        } else {
            lca = firstPath.get(i + 1);
        }

        Map<FileInfo, Commit> commonCommitMap = versionControlSystem.getCommitTreeCrawler()
                .collectFiles(versionControlSystem.getRepository(), lca.getId());

        Map<FileInfo, Commit> firstCommitMap = versionControlSystem.getCommitTreeCrawler()
                .getFilesUnionFromCommits(firstPath.subList(0, i));
        Map<FileInfo, Commit> secondCommitMap = versionControlSystem.getCommitTreeCrawler()
                .getFilesUnionFromCommits(secondPath.subList(0, j));

        class FileInfoWithCommit {
            private final FileInfo fileInfo;
            private final Commit commit;

            private FileInfoWithCommit(FileInfo fileInfo, Commit commit) {
                this.fileInfo = fileInfo;
                this.commit = commit;
            }
        }

        Map<String, FileInfoWithCommit> firstPathCommit = firstCommitMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getPath(), e -> new FileInfoWithCommit(e.getKey(), e.getValue())));

        Map<String, FileInfoWithCommit> secondPathCommit = secondCommitMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getPath(), e -> new FileInfoWithCommit(e.getKey(), e.getValue())));


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

        return "Merged to lca";
    }
}
