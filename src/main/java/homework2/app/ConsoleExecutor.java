package homework2.app;

import homework2.command.Command;
import homework2.command.CommandFactory;
import homework2.exception.InvalidArgumentsException;

import java.util.Arrays;

/**
 * Class which runs commands and print result to console
 */
public class ConsoleExecutor {

    /**
     * Run command, which name must be first in args list, other args will be args of command
     *
     * @param args                 command name and args of command
     * @param versionControlSystem current version control system
     */
    public void run(String[] args, VersionControlSystem versionControlSystem) {
        try {
            if (args.length == 0) {
                throw new InvalidArgumentsException("No command specified!", args);
            }

            Command commandByName = CommandFactory.getCommandByName(args[0]);
            String result = commandByName.execute(versionControlSystem, Arrays.copyOfRange(args, 1, args.length));

            System.out.println(result);

            versionControlSystem.getRepositoryLoader().saveRepository(versionControlSystem.getRepository());
        } catch (Exception e) {
//                e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
