package homework2.command;

import homework2.app.VersionControlSystem;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public interface Command {

    String execute(VersionControlSystem versionControlSystem, String[] args);

}
