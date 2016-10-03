package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;

import java.io.File;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class InitCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        if (args.length > 0) {
            throw new RuntimeException("Init doesn't take args");
        }

        File vcsDir = versionControlSystem.getFileSystem().getVcsDirPath().toFile();
        if (vcsDir.exists()) {
            throw new RuntimeException("Repository already has been init");
        }

        versionControlSystem.getFileSystem().createDirs(vcsDir);
        return "Repository has been init";
    }
}
