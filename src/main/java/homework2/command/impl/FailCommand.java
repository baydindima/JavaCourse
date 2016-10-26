package homework2.command.impl;

import homework2.app.VersionControlSystem;
import homework2.command.Command;
import homework2.exception.NoSuchCommandException;

/**
 * Throw exception cause that command doesn't exist
 */
public class FailCommand implements Command {
    /**
     * Throw exception cause that command doesn't exist
     */
    @Override
    public String execute(VersionControlSystem versionControlSystem, String[] args) {
        throw new NoSuchCommandException();
    }
}
