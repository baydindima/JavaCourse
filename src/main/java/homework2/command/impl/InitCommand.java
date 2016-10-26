package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.InvalidArgumentsException;
import homework2.exception.RepositoryException;

import java.io.File;

/**
 * Init repository in current directory
 */
public class InitCommand implements Command {

    /**
     * Init repository in current directory
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        if (args.length > 0) {
            throw new InvalidArgumentsException("Init doesn't take args", args);
        }

        File vcsDir = versionControlSystem.getFileSystem().getVcsDirPath().toFile();
        if (vcsDir.exists()) {
            throw new RepositoryException("Repository already has been init");
        }

        versionControlSystem.getFileSystem().createDirs(vcsDir);
        return "Repository has been init";
    }
}
