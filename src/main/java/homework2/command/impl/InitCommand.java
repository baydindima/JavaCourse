package homework2.command.impl;

import homework2.app.Backend;
import homework2.command.Command;

import java.io.File;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class InitCommand implements Command {
    @Override
    public String execute(Backend backend, String[] args) {
        if (args.length > 0) {
            throw new RuntimeException("Init doesn't take args");
        }

        File vcsDir = backend.getFileUtils().getVcsDirPath().toFile();
        if (vcsDir.exists()) {
            throw new RuntimeException("Repository already has been init");
        }

        backend.getFileUtils().createDirs(vcsDir);
        return "Repository has been init";
    }
}
