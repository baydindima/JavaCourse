package homework2.utils;

import homework2.model.Commit;
import homework2.model.FileInfo;
import homework2.model.InMemoryRepository;
import homework2.model.Repository;
import homework2.storage.RepositoryReader;
import homework2.storage.RepositoryWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class RepositoryUtils {
    private static final String REPOSITORY_INFO_NAME = "info";

    private RepositoryUtils() {
    }

    public static void checkRepositoryInit() {
        if (!FileUtils.getVcsDirPath().toFile().exists()) {
            throw new RuntimeException("Repository hasn't been init");
        }
    }

    public static Repository getRepository() throws FileNotFoundException {
        if (FileUtils.getVcsDirPath().toFile().exists()) {
            return new RepositoryReader()
                    .read(new FileInputStream(
                            new File(FileUtils.getVcsDirPath().toString(), REPOSITORY_INFO_NAME))
                    );
        } else {
            return new InMemoryRepository();
        }
    }

    public static void saveRepository(Repository repository) throws IOException {
        File repositoryFile = new File(FileUtils.getVcsDirPath().toFile(), REPOSITORY_INFO_NAME);

        try (FileOutputStream outputStream = new FileOutputStream(repositoryFile, false)) {
            if (!repositoryFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                repositoryFile.createNewFile();
            }
            new RepositoryWriter().write(repository, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * @return collectFiles for current commit
     */
    public static Map<FileInfo, Commit> collectFiles(Repository repository) {
        return collectFiles(repository, repository.getCurrentRevisionId());
    }


    /**
     * @param revisionId id of commit
     * @return list of paths to files in VCS, which from the  commit
     */
    public static Map<FileInfo, Commit> collectFiles(Repository repository, long revisionId) {
        List<Commit> commits = getCommitPath(repository, revisionId);

        return getFilesUnionFromCommits(commits);
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
        Commit result = repository.getCommitById(revisionId);
        if (result == null) {
            throw new RuntimeException(String.format("No commit with id %d!", revisionId));
        }
        return result;
    }

}
