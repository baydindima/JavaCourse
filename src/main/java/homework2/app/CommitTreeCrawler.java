package homework2.app;

import homework2.model.Commit;
import homework2.model.FileInfo;
import homework2.model.Repository;

import java.util.*;

/**
 * @author Dmitriy Baidin on 10/3/2016.
 */
public class CommitTreeCrawler {

    public Map<FileInfo, Commit> collectFiles(Repository repository) {
        if (repository.getCurrentRevisionId() == null) {
            return Collections.emptyMap();
        }
        return collectFiles(repository, repository.getCurrentRevisionId());
    }

    /**
     * @param revisionId id of commit
     * @return list of paths to files in VCS, which from the  commit
     */
    public Map<FileInfo, Commit> collectFiles(Repository repository, long revisionId) {
        List<Commit> commits = getCommitPath(repository, revisionId);

        return getFilesUnionFromCommits(commits);
    }

    /**
     * @param commitPath path of commits from last commit to root
     * @return map of fileInfo to commit which contains this file
     */
    public Map<FileInfo, Commit> getFilesUnionFromCommits(List<Commit> commitPath) {
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
    public List<Commit> getCommitPath(Repository repository, long revisionId) {
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
    private Commit getCommitFromRep(Repository repository, long revisionId) {
        Commit result = repository.getCommitById(revisionId);
        if (result == null) {
            throw new RuntimeException(String.format("No commit with id %d!", revisionId));
        }
        return result;
    }

}
