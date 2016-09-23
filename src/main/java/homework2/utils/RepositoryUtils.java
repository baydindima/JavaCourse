package homework2.utils;

import homework2.model.Commit;
import homework2.model.FileInfo;
import homework2.model.Repository;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class RepositoryUtils {

    private RepositoryUtils() {
    }


    /**
     * @return collectFiles for current commit
     */
    public static List<Path> collectFiles(Repository repository) {
        return collectFiles(repository, repository.getCurrentRevisionId());
    }


    /**
     * @param revisionId id of commit
     * @return list of paths to files in VCS, which from the  commit
     */
    public static List<Path> collectFiles(Repository repository, long revisionId) {
        List<Commit> commits = getCommitPath(repository, revisionId);

        Map<FileInfo, Commit> fileInfoCommitMap = getFilesUnionFromCommits(commits);

        return fileInfoCommitMap.entrySet()
                .stream()
                .map(entry ->
                        FileUtils.getPathFromCommitAndFileInfo(
                                entry.getValue(),
                                entry.getKey())
                )
                .collect(Collectors.toList());
    }

    /**
     * @param commitPath path of commits from last commit to root
     * @return map of fileInfo to commit which contains this file
     */
    private static Map<FileInfo, Commit> getFilesUnionFromCommits(List<Commit> commitPath) {
        Map<FileInfo, Commit> fileInfoCommitMap = new HashMap<>();
        for (int i = commitPath.size() - 1; i >= 0; i--) {
            Commit commit = commitPath.get(i);
            commit.getRemovedFiles().forEach(fileInfoCommitMap::remove);

            for (FileInfo fileInfo : commit.getFiles()) {
                fileInfoCommitMap.put(fileInfo, commit);
            }
        }
        return fileInfoCommitMap;
    }

    /**
     * @return commits on path from revision commit and first commit,
     * start from revision commit
     */
    private static List<Commit> getCommitPath(Repository repository, long revisionId) {
        Commit commit = getCommitFromRep(repository, revisionId);

        List<Commit> commits = new ArrayList<>();
        commits.add(commit);
        while (commit.getParentId() != null) {
            commit = getCommitFromRep(repository, revisionId);
            commits.add(commit);
        }
        return commits;
    }


    /**
     * Wrapping on repository.getCommitById,
     * which throws Runtime Error if commit didn't find
     *
     * @return commit or throws RuntimeException
     */
    private static Commit getCommitFromRep(Repository repository, long revisionId) {
        Optional<Commit> result = repository.getCommitById(revisionId);
        if (!result.isPresent()) {
            throw new RuntimeException(String.format("No commit with id %d!", revisionId));
        }
        return result.get();
    }

    private static class FileInfoWithCommit {
        private final FileInfo fileInfo;
        private final Commit commit;

        public FileInfoWithCommit(FileInfo fileInfo, Commit commit) {
            this.fileInfo = fileInfo;
            this.commit = commit;
        }
    }

}
