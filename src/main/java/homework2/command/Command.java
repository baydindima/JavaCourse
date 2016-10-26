package homework2.command;

import homework2.app.VersionControlSystem;

/**
 * Base class for all commands
 */
public interface Command {


    /**
     * Execute command
     *
     * @param versionControlSystem current VCS, context
     * @param args                 args of command
     * @return string result
     */
    String execute(VersionControlSystem versionControlSystem, String[] args);

}
