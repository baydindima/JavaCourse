package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class FailCommand implements Command {
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        return "No such command!";
    }
}
