package homework2.utils;

import homework2.model.Commit;
import homework2.model.FileInfo;
import homework2.model.InMemoryRepository;
import homework2.model.Repository;
import homework2.storage.RepositoryReader;
import homework2.storage.RepositoryWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static homework2.utils.FileUtils.ADDED_FILES_FILE_NAME;
import static homework2.utils.FileUtils.REPOSITORY_INFO_NAME;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class RepositoryUtils {
    private final FileUtils fileUtils;

    public RepositoryUtils(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    public void checkRepositoryInit() {
        if (!fileUtils.getVcsDirPath().toFile().exists()) {
            throw new RuntimeException("Repository hasn't been init");
        }
    }

    public Repository loadRepository() {
        if (fileUtils.getVcsDirPath().toFile().exists()) {
            try {
                return new RepositoryReader()
                        .read(new FileInputStream(
                                new File(fileUtils.getVcsDirPath().toString(), REPOSITORY_INFO_NAME))
                        );
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            return new InMemoryRepository();
        }
    }

    public void saveRepository(Repository repository) throws IOException {
        File repositoryFile = new File(fileUtils.getVcsDirPath().toFile(), REPOSITORY_INFO_NAME);

        try (FileOutputStream outputStream = new FileOutputStream(repositoryFile, false)) {
            if (!repositoryFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                repositoryFile.createNewFile();
            }
            new RepositoryWriter().write(repository, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * @return collectFiles for current commit
     */
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
    private Map<FileInfo, Commit> getFilesUnionFromCommits(List<Commit> commitPath) {
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
    private List<Commit> getCommitPath(Repository repository, long revisionId) {
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

    public void copyFilesToCommitDir(Commit commit) {
        Path commitDirPath = getCommitDirPath(commit);
        Path currentDirPath = fileUtils.getCurrentDirPath();

        fileUtils.createDirs(commitDirPath.toFile());
        try {
            for (FileInfo fileInfo : commit.getFiles()) {
                Path commitPath = Paths.get(commitDirPath.toString(), fileInfo.getPath());
                //noinspection ResultOfMethodCallIgnored
                commitPath.getParent().toFile().mkdirs();
                Files.copy(Paths.get(currentDirPath.toString(), fileInfo.getPath()),
                        commitPath, COPY_ATTRIBUTES);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void copyFilesFromCommitDirs(Map<FileInfo, Commit> fileInfoCommitMap) {
        Path currentDirPath = fileUtils.getCurrentDirPath();

        try {
            for (Map.Entry<FileInfo, Commit> entry : fileInfoCommitMap.entrySet()) {
                Path commitPath = Paths.get(getCommitDirPath(entry.getValue()).toString(), entry.getKey().getPath());
                Path filePath = Paths.get(currentDirPath.toString(), entry.getKey().getPath());
                //noinspection ResultOfMethodCallIgnored
                filePath.getParent().toFile().mkdirs();
                Files.copy(commitPath,
                        filePath, COPY_ATTRIBUTES);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public Path getCommitDirPath(Commit commit) {
        return Paths.get(fileUtils.getVcsDirPath().toString(), String.valueOf(commit.getId()));
    }

    public void setToAddedFiles(List<String> files) {
        File addedFiles = new File(fileUtils.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);

        try (FileWriter fileWriter = new FileWriter(addedFiles, true)) {
            if (!addedFiles.exists()) {
                //noinspection ResultOfMethodCallIgnored
                addedFiles.createNewFile();
            }
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            for (String filePath : files) {
                bufferWriter.write(filePath + "\n");
            }
            bufferWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<String> getAddedFiles() {
        File addedFiles = new File(fileUtils.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);

        if (!addedFiles.exists()) {
            return Collections.emptyList();
        }

        try (FileReader fileReader = new FileReader(addedFiles)) {
            BufferedReader bufferReader = new BufferedReader(fileReader);
            return bufferReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void clearAddedFiles() {
        File addedFiles = new File(fileUtils.getVcsDirPath().toFile(), ADDED_FILES_FILE_NAME);
        if (addedFiles.exists()) {
            //noinspection ResultOfMethodCallIgnored
            addedFiles.delete();
        }
    }

}
