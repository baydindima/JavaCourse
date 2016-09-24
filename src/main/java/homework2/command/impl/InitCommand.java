package homework2.command.impl;

import homework2.command.Command;
import homework2.utils.FileUtils;

import java.io.File;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class InitCommand implements Command {
    @Override
    public String execute(String[] args) {
        if (args.length > 0) {
            throw new RuntimeException("Init doesn't take args");
        }

        File vcsDir = new File(FileUtils.getVcsDirPath().toString());
        if (vcsDir.exists()) {
            throw new RuntimeException("Repository already has been init");
        }

        FileUtils.createDirs(vcsDir);
        return "Repository has been init";
    }
}
